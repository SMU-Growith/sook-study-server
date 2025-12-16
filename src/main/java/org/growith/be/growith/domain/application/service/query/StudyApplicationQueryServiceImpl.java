package org.growith.be.growith.domain.application.service.query;

import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.application.converter.StudyApplicationConverter;
import org.growith.be.growith.domain.application.dto.response.StudyApplicationResponseDTO;
import org.growith.be.growith.domain.application.entity.StudyApplication;
import org.growith.be.growith.domain.application.repository.StudyApplicationRepository;
import org.growith.be.growith.domain.scrap.repository.StudyScrapRepository;
import org.growith.be.growith.domain.study.entity.Study;
import org.growith.be.growith.domain.study.entity.enums.StudyRole;
import org.growith.be.growith.domain.study.repository.StudyRepository;
import org.growith.be.growith.domain.study.repository.UserStudyRepository;
import org.growith.be.growith.domain.user.entity.User;
import org.growith.be.growith.global.error.code.status.StudyErrorCode;
import org.growith.be.growith.global.error.exception.handler.StudyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyApplicationQueryServiceImpl implements StudyApplicationQueryService{

    private final StudyApplicationRepository studyApplicationRepository;
    private final UserStudyRepository userStudyRepository;
    private final StudyRepository studyRepository;
    private final StudyScrapRepository studyScrapRepository;

    // 지원서 조회 (팀장 권한)
    @Transactional(readOnly = true)
    public List<StudyApplication> getStudyApplications(Long studyId, User user) {
        // 스터디 존재 확인
        studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyException(StudyErrorCode.STUDY_NOT_FOUND));

        // 팀장 권한 확인
        boolean isLeader = userStudyRepository.existsByStudyIdAndUserIdAndStudyRole(studyId, user.getId(), StudyRole.LEADER);
        if (!isLeader) {
            throw new StudyException(StudyErrorCode.STUDY_APPLICATION_FORBIDDEN);
        }

        return studyApplicationRepository.findByStudyId(studyId);
    }

    // 내가 지원한 지원서 목록 조회 (최신순)
    @Transactional(readOnly = true)
    public List<StudyApplicationResponseDTO.MyApplicationCardDTO> getMyApplications(User user) {
        // 사용자의 모든 지원서 조회 (최신순)
        List<StudyApplication> applications = studyApplicationRepository.findByUserIdOrderByCreatedAtDesc(user.getId());

        // 각 지원서의 스터디에 대한 스크랩 여부와 리더 닉네임 조회
        List<Boolean> scrappedList = applications.stream()
                .map(application -> {
                    Study study = application.getStudy();
                    return studyScrapRepository.findByUserAndStudy(user, study).isPresent();
                })
                .toList();

        // 각 스터디의 리더 닉네임 조회
        List<String> leaderNicknameList = applications.stream()
                .map(application -> {
                    Long studyId = application.getStudy().getId();
                    return userStudyRepository.findByStudyIdAndStudyRole(studyId, StudyRole.LEADER)
                            .stream()
                            .findFirst()
                            .map(userStudy -> userStudy.getUser().getNickName())
                            .orElse(null);
                })
                .toList();

        return StudyApplicationConverter.toMyApplicationCardDTOList(applications, scrappedList, leaderNicknameList);
    }
}

