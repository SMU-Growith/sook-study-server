package org.growith.be.growith.domain.study.service.query;

import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.journal.dto.StudySession;
import org.growith.be.growith.domain.journal.repository.StudyJournalRepository;
import org.growith.be.growith.domain.journal.service.JournalEmojiService;
import org.growith.be.growith.domain.study.converter.StudyConverter;
import org.growith.be.growith.domain.study.dto.StudyCardDto;
import org.growith.be.growith.domain.journal.dto.StudySessionCardDto;
import org.growith.be.growith.domain.study.dto.request.StudyRequestDto;
import org.growith.be.growith.domain.study.dto.response.StudyResponseDto;
import org.growith.be.growith.domain.study.entity.Rule;
import org.growith.be.growith.domain.study.entity.Study;
import org.growith.be.growith.domain.study.entity.enums.StudyStatus;
import org.growith.be.growith.domain.study.repository.*;
import org.growith.be.growith.domain.user.repository.UserRepository;
import org.growith.be.growith.global.error.code.status.StudyErrorCode;
import org.growith.be.growith.global.error.exception.handler.StudyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudyQueryServiceImpl implements StudyQueryService {
    private final StudyRepository studyRepository;
    private final UserRepository userRepository;
    private final StudyFieldRepository studyFieldRepository;
    private final RuleRepository ruleRepository;
    private final StudySessionRepository studySessionRepository;
    private final UserStudyRepository userStudyRepository;
    private final StudyJournalRepository studyJournalRepository;
    private final JournalEmojiService journalEmojiService;


    // 자신의 스터디 조회
    public List<StudyResponseDto.StudyCardDto> getMyStudies(String userId, int page, int size, String studyStatus) {
        StudyStatus status = StudyStatus.valueOf(studyStatus.toUpperCase());
        List<Study> studies = studyRepository.findMyStudies(Long.parseLong(userId), PageRequest.of(page, size), status);

/*
        studies.stream().map(study -> {
            // 멤버 수 조회
            Integer memberCount = studyRepository.countMembersByStudyId(study.getId());

            // 스터디 진행 일수 계산
            Integer studyDays = (int) java.time.temporal.ChronoUnit.DAYS.between(
                    study.getCreatedAt().toLocalDate(),
                    java.time.LocalDate.now()
            ) + 1;

            // 사용자의 스터디 내 역할 조회
            String userRole = studyRepository.findUserRoleInStudy(Long.parseLong(userId), study.getId());

            // Study -> StudyCardDto
//            return StudyConverter.toStudyCardDto(study, memberCount, studyDays);
        }
        ).toList();
        */
        return null;
    }

    public List<StudyCardDto> getPopularStudies(int page, int size) {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        List<Study> studies = studyRepository.findPopularStudies(oneMonthAgo, PageRequest.of(page, size));
        return studies.stream().map(study -> StudyCardDto.builder()
                .studyId(study.getId())
                .studyStatus(study.getStudyStatus())
                .title(study.getTitle())
                .authorId(study.getUser().getId() != null ? study.getUser().getId().toString() : null)
                .scrapCount(study.getScrapCount())
                .format(study.getStudyFormat() != null ? study.getStudyFormat().name() : null)
                .fieldName(study.getStudyField().getName())
                .styleNames(study.getStudyStyleCategory())
                .build()).toList();
    }

    public List<StudyCardDto> getNewStudies(int page, int size) {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        List<Study> studies = studyRepository.findNewStudies(oneMonthAgo, PageRequest.of(page, size));
        return studies.stream().map(study -> StudyCardDto.builder()
                .studyId(study.getId())
                .studyStatus(study.getStudyStatus())
                .title(study.getTitle())
                .authorId(study.getUser().getId() != null ? study.getUser().getId().toString() : null)
                .scrapCount(study.getScrapCount())
                .format(study.getStudyFormat() != null ? study.getStudyFormat().name() : null)
                .fieldName(study.getStudyField().getName())
                .styleNames(study.getStudyStyleCategory())
                .build()).toList();
    }

    // 스터디 상세 조회
    public StudyResponseDto.StudyDetail getStudyDetail(Long studyId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyException(StudyErrorCode.STUDY_NOT_FOUND));
        List<Rule> rules = ruleRepository.findByStudy(study);
        return StudyConverter.toStudyDetail(study, rules);
    }


    public List<Study> searchStudies(
            StudyRequestDto.SearchStudyCondition request,
            Pageable pageable
    ) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        return studyRepository.searchStudy(request, pageRequest);
    }

    public List<StudySessionCardDto> getStudySessions(Long studyId, int page, int size) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("studyId에 해당하는 스터디 없음"));

        // 전체 세션 수 (페이징 전 전체 카운트)
        List<StudySession> allSessions = study.getStudySessions();
        Integer totalCount = allSessions.size();

        // 페이징 처리
        return allSessions.stream()
                .skip((long) page * size)
                .limit(size)
                .map(session -> {
                    Integer submittedCount = studySessionRepository.countSubmittedBySessionId(session.getId());
                    return StudySessionCardDto.builder()
                            .sessionId(session.getId())
                            .sessionNumber(session.getNumber())
                            .title(session.getTitle())
                            .submittedCount(submittedCount)
                            .totalCount(totalCount)
                            .build();
                }).toList();
    }

    /*
    // 스터디 멤버 조회
    public List<Study> getStudyMembers(Long studyId) {
        Optional<Study> byId = studyRepository.findById(studyId);
    }
    */


}
