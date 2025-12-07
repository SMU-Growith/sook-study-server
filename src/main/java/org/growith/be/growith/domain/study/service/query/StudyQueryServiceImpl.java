package org.growith.be.growith.domain.study.service.query;

import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.application.entity.StudyApplication;
import org.growith.be.growith.domain.application.repository.StudyApplicationRepository;
import org.growith.be.growith.domain.journal.dto.StudySession;
import org.growith.be.growith.domain.journal.dto.StudySessionCardDto;
import org.growith.be.growith.domain.journal.dto.StudySessionListDto;
import org.growith.be.growith.domain.journal.repository.StudyJournalRepository;
import org.growith.be.growith.domain.journal.service.JournalEmojiService;
import org.growith.be.growith.domain.scrap.repository.StudyScrapRepository;
import org.growith.be.growith.domain.study.converter.StudyConverter;
import org.growith.be.growith.domain.study.dto.request.StudyRequestDto;
import org.growith.be.growith.domain.study.dto.response.StudyResponseDto;
import org.growith.be.growith.domain.study.entity.Rule;
import org.growith.be.growith.domain.study.entity.Study;
import org.growith.be.growith.domain.study.entity.StudyField;
import org.growith.be.growith.domain.study.entity.UserStudy;
import org.growith.be.growith.domain.study.entity.enums.StudyStatus;
import org.growith.be.growith.domain.study.repository.*;
import org.growith.be.growith.domain.user.entity.User;
import org.growith.be.growith.domain.user.repository.UserRepository;
import org.growith.be.growith.global.error.code.status.StudyErrorCode;
import org.growith.be.growith.global.error.code.status.UserErrorCode;
import org.growith.be.growith.global.error.exception.handler.StudyException;
import org.growith.be.growith.global.error.exception.handler.UserException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    private final StudyApplicationRepository studyApplicationRepository;
    private final StudyScrapRepository studyScrapRepository;


    // 자신의 스터디 조회
    public List<StudyResponseDto.UserStudyPreviewDto> getMyStudies(Long userId, String studyStatus, Pageable pageable) {
        StudyStatus status = StudyStatus.valueOf(studyStatus.toUpperCase());
        Page<UserStudy> userStudies = userStudyRepository.findByUserIdAndStatus(userId, status, pageable);
        return userStudies.stream()
                .map(userStudy -> {
                    Long memberCount = userStudyRepository.countByStudyId(userStudy.getStudy().getId());
                    // 스터디 시작일(생성일)로부터 며칠 지났는지 계산 (D-Day 개념, 시작일이 1일)
                    Long studyDays = java.time.temporal.ChronoUnit.DAYS.between(
                            userStudy.getStudy().getCreatedAt().toLocalDate(), 
                            java.time.LocalDate.now()
                    ) + 1;
                    return StudyConverter.toUserStudyPreviewDto(userStudy, memberCount, studyDays);
                })
                .toList();
    }

    // 인기/새로운 스터디 조회
    public List<Study> getStudiesByPopularOrNew(Pageable pageable) {
        return studyRepository.getStudySortByPopularOrNew(pageable);
    }

    // 스터디 상세 조회
    public StudyResponseDto.StudyDetail getStudyDetail(Long studyId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND));

        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyException(StudyErrorCode.STUDY_NOT_FOUND));
        List<Rule> rules = ruleRepository.findByStudy(study);

        Boolean isScraped = false;
        if (user != null) { // 로그인한 유저인 경우에만 스크랩 여부 확인
            isScraped = studyScrapRepository.findByUserAndStudy(user, study).isPresent();
        }

        return StudyConverter.toStudyDetail(study, rules, isScraped);
    }

    // 스터디 검색
    public List<Study> searchStudies(
            StudyRequestDto.SearchStudyCondition request,
            Pageable pageable
    ) {
        List<StudyField> studyFields = null;
        if (request.studyFieldIds() != null) {
            studyFields = request.studyFieldIds().stream().map(
                            studyFieldId -> studyFieldRepository.findById(studyFieldId)
                                    .orElseThrow(() ->new StudyException(StudyErrorCode.STUDY_FIELD_NOT_FOUND))
                    ).toList();
        }
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        return studyRepository.searchStudy(request, studyFields, pageRequest);
    }

    // 스터디 멤버 조회
    public List<StudyResponseDto.StudyUsers> getStudyMembers(Long studyId) {
        return userStudyRepository.findByStudyId(studyId).stream()
                .map(userStudy -> {
                    StudyApplication studyApplication = studyApplicationRepository.findByStudyIdAndUserId(userStudy.getStudy().getId(), userStudy.getUser().getId());
                    return StudyConverter.toStudyUsers(userStudy, studyApplication.getMotivation());
                }).toList();
    }


    // 스터디 세션 조회
    public StudySessionListDto getStudySessions(Long studyId, int page, int size) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("studyId에 해당하는 스터디 없음"));

        // 전체 세션 수 (페이징 전 전체 카운트)
        List<StudySession> allSessions = study.getStudySessions();
        Integer totalCount = allSessions.size();

        // 페이징 처리
        List<StudySessionCardDto> sessions = allSessions.stream()
                .skip((long) page * size)
                .limit(size)
                .map(session -> {
                    Integer submittedCount = studySessionRepository.countSubmittedBySessionId(session.getId());
                    return StudySessionCardDto.builder()
                            .sessionId(session.getId())
                            .sessionNumber(session.getNumber())
                            .title(session.getTitle())
                            .submittedCount(submittedCount)
                            .build();
                }).toList();

        return StudySessionListDto.builder()
                .studySessions(sessions)
                .totalCount(totalCount)
                .build();
    }


    // 스터디 분야 목록 조회
    @Override
    public List<StudyResponseDto.StudyFieldDto> getStudyFields() {
        return studyFieldRepository.findAll().stream()
                .filter(field -> field.getParent() == null)
                .map(this::toFieldDto)
                .toList();
    }

    private StudyResponseDto.StudyFieldDto toFieldDto(StudyField field) {
        return StudyResponseDto.StudyFieldDto.builder()
                .id(field.getId())
                .name(field.getName())
                .children(field.getStudyField().stream()
                        .map(this::toFieldDto)
                        .toList())
                .build();
    }

}
