package org.growith.be.growith.domain.study.service.command;

import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.stamp.service.StampUpdateHelper;
import org.growith.be.growith.domain.study.converter.StudyConverter;
import org.growith.be.growith.domain.study.dto.request.StudyRequestDto;
import org.growith.be.growith.domain.study.dto.response.StudyResponseDto;
import org.growith.be.growith.domain.study.entity.*;
import org.growith.be.growith.domain.study.entity.enums.RuleCategory;
import org.growith.be.growith.domain.study.entity.enums.StudyRole;
import org.growith.be.growith.domain.study.entity.enums.StudyStatus;
import org.growith.be.growith.domain.study.repository.*;
import org.growith.be.growith.domain.scrap.repository.StudyScrapRepository;
import org.growith.be.growith.domain.user.entity.User;
import org.growith.be.growith.domain.user.repository.UserRepository;
import org.growith.be.growith.global.error.code.status.StudyErrorCode;
import org.growith.be.growith.global.error.code.status.UserErrorCode;
import org.growith.be.growith.global.error.exception.handler.StudyException;
import org.growith.be.growith.global.error.exception.handler.UserException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StudyCommandServiceImpl implements StudyCommandService{
    private final StudyRepository studyRepository;
    private final UserRepository userRepository;
    private final StudyFieldRepository studyFieldRepository;
    private final RuleRepository ruleRepository;
    private final UserStudyRepository userStudyRepository;
    private final StampUpdateHelper stampUpdateHelper;
    private final StudyScrapRepository studyScrapRepository;

    // 스터디 생성
    public StudyResponseDto.StudyDetail createStudy(StudyRequestDto.CreateStudyDTO request, Long userId) {
        // 작성자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND));

        // 스터디 분야 이름으로 조회 (studyFieldName)
        StudyField field = studyFieldRepository.findByName(request.studyFieldName())
                .orElseThrow(() -> new StudyException(StudyErrorCode.STUDY_NOT_FOUND));

        Study studyEntity = StudyConverter.toStudyEntity(request, user, field);
        Study savedStudy = studyRepository.save(studyEntity);
        userStudyRepository.save(StudyConverter.toUserStudyEntity(user, savedStudy, StudyRole.LEADER));

        // Request로 받은 규칙 저장
        List<Rule> rules = request.rules().stream()
                .map(rule -> StudyConverter.toRuleEntity(rule, savedStudy))
                .toList();
        ruleRepository.saveAll(rules);

        // 리더숙 스탬프 업데이트
        long createdStudyCount = studyRepository.countByUserId(userId);
        stampUpdateHelper.updateLeaderStamp(userId, (int) createdStudyCount);

         return StudyConverter.toStudyDetail(savedStudy, rules, false, true);
    }

    // 스터디 수정
    public StudyResponseDto.StudyDetail updateStudy(StudyRequestDto.UpdateStudyDTO request, Long studyId, Long userId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyException(StudyErrorCode.STUDY_NOT_FOUND));

        // 수정 권한 확인
        if (!study.getUser().getId().equals(userId)) {
            throw new StudyException(StudyErrorCode.STUDY_UPDATE_FORBIDDEN);
        }

        StudyField studyField = studyFieldRepository.findByName(request.studyFieldName())
                .orElseThrow(() -> new StudyException(StudyErrorCode.STUDY_NOT_FOUND));

        // 스터디 정보 업데이트
        study.updateStudy(request, studyField);
        Study updatedStudy = studyRepository.save(study);

        ruleRepository.deleteByStudy(updatedStudy);
        // Rule 엔티티 생성 후 저장
        List<Rule> rules = request.ruleDTO().stream()
                .map(rule -> StudyConverter.toRuleEntity(rule, updatedStudy))
                .toList();
        // 규칙 저장
        ruleRepository.saveAll(rules);

        User user = study.getUser(); 
        Boolean isScraped = studyScrapRepository.findByUserAndStudy(user, updatedStudy).isPresent();

        Boolean isMyStudy = updatedStudy.getUser().getId().equals(userId);
        return StudyConverter.toStudyDetail(updatedStudy, rules, isScraped, isMyStudy);
    }

    // 스터디 삭제
    public void deleteStudy(Long studyId, Long userId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyException(StudyErrorCode.STUDY_NOT_FOUND));

        // 수정 권한 확인
        if (!study.getUser().getId().equals(userId)) {
            throw new StudyException(StudyErrorCode.STUDY_UPDATE_FORBIDDEN);
        }

        // FK 제약조건 때문에 순서대로 삭제
        ruleRepository.deleteByStudy(study);           // 1. rule 삭제
        userStudyRepository.deleteByStudyId(studyId);  // 2. user_study 삭제
        studyRepository.deleteById(studyId);           // 3. study 삭제
    }

    public void withdrawStudy(Long studyId, Long userId) {
        // MEMBER 역할인지 확인
        boolean isMember = userStudyRepository.existsByStudyIdAndUserIdAndStudyRole(studyId, userId, StudyRole.MEMBER);
        if (!isMember){
            throw new StudyException(StudyErrorCode.STUDY_WITHDRAW_FORBIDDEN);
        }
        
        // UserStudy 조회 후 역할을 WITHDRAWN으로 변경
        UserStudy userStudy = userStudyRepository.findByStudyIdAndUserId(studyId, userId)
                .orElseThrow(() -> new StudyException(StudyErrorCode.STUDY_MEMBER_NOT_FOUND));
        
        userStudy.changeRole(StudyRole.WITHDRAWN);
        userStudyRepository.save(userStudy);
    }

    // 스터디 팀장 변경 (현재 리더 -> 멤버, 선택된 멤버 -> 리더)
    public void changeStudyLeader(Long studyId, Long currentUserId, Long newLeaderUserId) {
        // 현재 사용자가 해당 스터디의 리더인지 확인
        UserStudy currentUserStudy = userStudyRepository.findByStudyIdAndUserId(studyId, currentUserId)
                .orElseThrow(() -> new StudyException(StudyErrorCode.STUDY_MEMBER_NOT_FOUND));

        if (currentUserStudy.getStudyRole() != StudyRole.LEADER) {
            throw new StudyException(StudyErrorCode.STUDY_LEADER_FORBIDDEN);
        }

        // 새 리더가 될 사용자가 해당 스터디의 멤버인지 확인
        UserStudy newLeaderStudy = userStudyRepository.findByStudyIdAndUserId(studyId, newLeaderUserId)
                .orElseThrow(() -> new StudyException(StudyErrorCode.STUDY_MEMBER_NOT_FOUND));

        if (newLeaderStudy.getStudyRole() != StudyRole.MEMBER) {
            throw new StudyException(StudyErrorCode.STUDY_LEADER_CHANGE_INVALID);
        }

        // 역할 변경: 현재 리더 -> 멤버, 선택된 멤버 -> 리더
        currentUserStudy.changeRole(StudyRole.MEMBER);
        newLeaderStudy.changeRole(StudyRole.LEADER);

        userStudyRepository.save(currentUserStudy);
        userStudyRepository.save(newLeaderStudy);
    }

    // 스터디 상태 수정
    public StudyStatus changeStudyStatus(Long studyId, StudyStatus studyStatus, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND));

        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyException(StudyErrorCode.STUDY_NOT_FOUND));

        // 수정 권한 확인 (팀장만 가능)
        if (!study.getUser().getId().equals(userId)) {
            throw new StudyException(StudyErrorCode.STUDY_UPDATE_FORBIDDEN);
        }

        // 스터디 상태 변경
        study.changeStudyStatus(studyStatus);
        
        // CLOSED로 변경하는 경우 추가 처리
        if (studyStatus == StudyStatus.CLOSED) {
            // 1. 모든 멤버를 WITHDRAWN으로 변경
            List<UserStudy> allMembers = userStudyRepository.findByStudyId(studyId);
            allMembers.forEach(userStudy -> {
                if (userStudy.getStudyRole() != StudyRole.WITHDRAWN) {
                    userStudy.changeRole(StudyRole.WITHDRAWN);
                }
            });
            userStudyRepository.saveAll(allMembers);
            
            // 2. 모집 상태를 false로 변경
            if (study.getIsRecruiting()) {
                study.toggleIsRecruiting();
            }
        }
        
        return studyStatus;
    }

    public StudyStatus toggleStudyStatus(Long studyId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND));

        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyException(StudyErrorCode.STUDY_NOT_FOUND));

        // 수정 권한 확인
        if (!study.getUser().getId().equals(userId)) {
            throw new StudyException(StudyErrorCode.STUDY_UPDATE_FORBIDDEN);
        }

        // 현재 상태를 토글
        StudyStatus newStatus = study.getStudyStatus().toggle();
        study.changeStudyStatus(newStatus);
        return newStatus;
    }

    @Override
    public Boolean toggleIsRecruiting(Long studyId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND));

        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyException(StudyErrorCode.STUDY_NOT_FOUND));

        // 수정 권한 확인 (팀장만 가능)
        if (!study.getUser().getId().equals(userId)) {
            throw new StudyException(StudyErrorCode.STUDY_UPDATE_FORBIDDEN);
        }

        // 모집 상태 토글
        study.toggleIsRecruiting();
        return study.getIsRecruiting();
    }

    @Override
    public void updateStudyRules(Long studyId, Long userId, StudyRequestDto.UpdateRuleContentDTO request) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyException(StudyErrorCode.STUDY_NOT_FOUND));

        if (!study.getUser().getId().equals(userId)) {
            throw new StudyException(StudyErrorCode.STUDY_UPDATE_FORBIDDEN);
        }

        // 각 RuleDTO에 대해 해당 카테고리의 규칙을 찾아서 업데이트
        for (StudyRequestDto.RuleDTO ruleDTO : request.rules()) {
            Rule rule = ruleRepository.findByStudy_IdAndRuleCategory(studyId, ruleDTO.ruleCategory())
                    .orElseThrow(() -> new StudyException(StudyErrorCode.RULE_NOT_FOUND));
            
            rule.update(ruleDTO.ruleCategory(), ruleDTO.description());
        }
    }
}
