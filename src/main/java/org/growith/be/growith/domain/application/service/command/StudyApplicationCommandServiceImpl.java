package org.growith.be.growith.domain.application.service.command;

import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.application.converter.StudyApplicationConverter;
import org.growith.be.growith.domain.application.dto.request.StudyApplicationRequestDTO;
import org.growith.be.growith.domain.application.entity.ApplicationStatus;
import org.growith.be.growith.domain.application.entity.StudyApplication;
import org.growith.be.growith.domain.application.repository.StudyApplicationRepository;
import org.growith.be.growith.domain.study.converter.StudyConverter;
import org.growith.be.growith.domain.study.entity.Study;
import org.growith.be.growith.domain.study.entity.UserStudy;
import org.growith.be.growith.domain.study.entity.enums.StudyRole;
import org.growith.be.growith.domain.study.repository.StudyRepository;
import org.growith.be.growith.domain.study.repository.UserStudyRepository;
import org.growith.be.growith.domain.user.entity.User;
import org.growith.be.growith.domain.user.repository.UserRepository;
import org.growith.be.growith.global.error.code.status.StudyErrorCode;
import org.growith.be.growith.global.error.code.status.UserErrorCode;
import org.growith.be.growith.global.error.exception.handler.StudyException;
import org.growith.be.growith.global.error.exception.handler.UserException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class StudyApplicationCommandServiceImpl implements StudyApplicationCommandService{

    private final StudyApplicationRepository studyApplicationRepository;
    private final StudyRepository studyRepository;
    private final UserRepository userRepository;
    private final UserStudyRepository userStudyRepository;


    // 스터디 지원
    @Transactional
    public StudyApplication createApplication(Long studyId, Long userId, StudyApplicationRequestDTO.CreateStudyApplicationDTO request) {
        // 스터디 확인
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("스터디를 찾을 수 없음"));

        // 사용자 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND));

        // 지원서 생성
        StudyApplication studyApplicationEntity = StudyApplicationConverter.toStudyApplicationEntity(request, user, study);
        // 지원서 저장
         return studyApplicationRepository.save(studyApplicationEntity);
    }

    // 지원자 승인/거절
    @Transactional
    public StudyApplication updateApplicationStatus(Long applicationId, Long userId, StudyApplicationRequestDTO.UpdateStudyApplicationDTO request) {
        StudyApplication application = studyApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new StudyException(StudyErrorCode.STUDY_APPLICATION_NOT_FOUND));

        // 사용자의 지원자 승인/거절 권한 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND));
        boolean isLeader = userStudyRepository.existsByStudyIdAndUserIdAndStudyRole(application.getStudy().getId(), user.getId(), StudyRole.LEADER);
        if (!isLeader) {
            throw new StudyException(StudyErrorCode.STUDY_APPLICATION_FORBIDDEN);
        }

        // 상태 업데이트
        application.updateStatus(request.status());

        // 만약 승인된 경우에만 UserStudy 추가
        if (request.status() == ApplicationStatus.ACCEPTED) {
            UserStudy userStudyEntity = StudyConverter.toUserStudyEntity(application.getUser(), application.getStudy(), StudyRole.MEMBER);
            userStudyRepository.save(userStudyEntity);
        }
        return application;
    }


}
