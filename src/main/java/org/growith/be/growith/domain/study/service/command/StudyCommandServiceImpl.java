package org.growith.be.growith.domain.study.service.command;

import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.study.converter.StudyConverter;
import org.growith.be.growith.domain.study.dto.request.StudyRequestDto;
import org.growith.be.growith.domain.study.dto.response.StudyResponseDto;
import org.growith.be.growith.domain.study.entity.*;
import org.growith.be.growith.domain.study.entity.enums.StudyRole;
import org.growith.be.growith.domain.study.entity.enums.StudyStatus;
import org.growith.be.growith.domain.study.repository.*;
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

    // 스터디 생성
    public StudyResponseDto.StudyDetail createStudy(StudyRequestDto.CreateStudyDTO request, Long userId) {
        // 작성자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND));

        // 분야 조회
        StudyField field = studyFieldRepository.findById(request.studyFieldId())
                .orElseThrow(() -> new StudyException(StudyErrorCode.STUDY_NOT_FOUND));

        Study studyEntity = StudyConverter.toStudyEntity(request, user, field);
        Study savedStudy = studyRepository.save(studyEntity);
        userStudyRepository.save(StudyConverter.toUserStudyEntity(user, savedStudy, StudyRole.LEADER));

        // Rule 엔티티 생성 후 저장
        List<Rule> rules = request.ruleDTO().stream()
                .map(rule -> StudyConverter.toRuleEntity(rule, savedStudy))
                .toList();
        // 규칙 저장
        ruleRepository.saveAll(rules);
         return StudyConverter.toStudyDetail(savedStudy, rules);
    }

    // 스터디 수정
    public StudyResponseDto.StudyDetail updateStudy(StudyRequestDto.UpdateStudyDTO request, Long studyId, Long userId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyException(StudyErrorCode.STUDY_NOT_FOUND));

        // 수정 권한 확인
        if (!study.getUser().getId().equals(userId)) {
            throw new StudyException(StudyErrorCode.STUDY_UPDATE_FORBIDDEN);
        }

        StudyField studyField = studyFieldRepository.findById(request.studyFieldId())
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
        return StudyConverter.toStudyDetail(updatedStudy, rules);
    }

    // 스터디 삭제
    public void deleteStudy(Long studyId, Long userId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyException(StudyErrorCode.STUDY_NOT_FOUND));

        // 수정 권한 확인
        if (!study.getUser().getId().equals(userId)) {
            throw new StudyException(StudyErrorCode.STUDY_UPDATE_FORBIDDEN);
        }

        ruleRepository.deleteByStudy(study);
        studyRepository.deleteById(studyId);
    }

    public void withdrawStudy(Long studyId, String userId) {
        // 프론트에서 현재 로그인한 사용자 userId로 받아야함
//        studyRepository.withdraw(studyId, Long.parseLong(userId));
    }


    // 리더->팀원으로 역할 변경
    public void changeStudyLeader(Long studyId, Long currentUserId, Long newLeaderUserId) {
        // 현재 사용자가 해당 스터디의 리더인지 확인
        UserStudy currentUserStudy = userStudyRepository.findByStudyIdAndUserId(studyId, currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("스터디 멤버가 아님"));

        if (currentUserStudy.getStudyRole() != StudyRole.LEADER) {
            throw new IllegalArgumentException("리더 권한이 없음");
        }

        // 새 리더가 될 사용자가 해당 스터디의 멤버인지 확인
        UserStudy newLeaderStudy = userStudyRepository.findByStudyIdAndUserId(studyId, newLeaderUserId)
                .orElseThrow(() -> new IllegalArgumentException("새 리더가 스터디 멤버가 아님"));

        if (newLeaderStudy.getStudyRole() != StudyRole.MEMBER) {
            throw new IllegalArgumentException("새 리더가 일반 멤버가 아님");
        }

        // 역할 변경
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

        // 수정 권한 확인
        if (!study.getUser().getId().equals(userId)) {
            throw new StudyException(StudyErrorCode.STUDY_UPDATE_FORBIDDEN);
        }

        study.changeStudyStatus(studyStatus);
        return studyStatus;
    }
}
