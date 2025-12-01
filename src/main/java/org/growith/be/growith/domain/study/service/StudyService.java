package org.growith.be.growith.domain.study.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.growith.be.growith.domain.study.converter.StudyConverter;
import org.growith.be.growith.domain.study.dto.*;
import org.growith.be.growith.domain.study.dto.request.StudyRequestDto;
import org.growith.be.growith.domain.study.dto.response.StudyResponseDto;
import org.growith.be.growith.domain.study.entity.*;
import org.growith.be.growith.domain.user.entity.*;
import org.growith.be.growith.domain.user.repository.*;
import org.growith.be.growith.domain.study.repository.*;
import org.growith.be.growith.domain.application.repository.StudyApplicationRepository;
import org.growith.be.growith.global.error.code.status.StudyErrorCode;
import org.growith.be.growith.global.error.exception.handler.StudyException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Sort;
import org.growith.be.growith.domain.journal.repository.StudyJournalRepository;
import org.growith.be.growith.domain.journal.dto.StudyJournalDto;
import org.growith.be.growith.domain.journal.dto.StudyJournalListDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.growith.be.growith.domain.study.entity.enums.*;
import org.growith.be.growith.domain.application.dto.ApplicationDto;
import org.growith.be.growith.domain.application.entity.ApplicationStatus;
import org.growith.be.growith.domain.journal.entity.StudyJournal;
import org.growith.be.growith.domain.application.entity.StudyApplication;
import org.growith.be.growith.domain.journal.service.JournalEmojiService;
@Slf4j
@Service
@RequiredArgsConstructor
public class StudyService {
    private final StudyRepository studyRepository;
    private final UserRepository userRepository;
    private final StudyFieldRepository studyFieldRepository;
    private final StyleRepository styleRepository;
    private final RuleRepository ruleRepository;
    private final StudySessionRepository studySessionRepository;
    private final UserStudyRepository userStudyRepository;
    private final StudyJournalRepository studyJournalRepository;
    private final JournalEmojiService journalEmojiService;
    private final StudyStyleRepository studyStyleRepository;
    private final StudyApplicationRepository studyApplicationRepository;

    // 자신의 스터디 조회
    public List<StudyResponseDto.StudyCardDto> getMyStudies(String userId, int page, int size, String studyStatus) {
        StudyStatus status = StudyStatus.valueOf(studyStatus.toUpperCase());
        List<Study> studies = studyRepository.findMyStudies(Long.parseLong(userId), PageRequest.of(page, size), status);


        return studies.stream().map(study -> {
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
            return StudyConverter.toStudyCardDto(study, memberCount, studyDays);
        }).toList();
    }

    public List<StudyCardDto> getPopularStudies(int page, int size) {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        List<Study> studies = studyRepository.findPopularStudies(oneMonthAgo, PageRequest.of(page, size));
        return studies.stream().map(study -> StudyCardDto.builder()
                .studyId(study.getId())
                .studyStatus(study.getStudyStatus())
                .title(study.getTitle())
                .authorId(study.getUser().getUserId() != null ? study.getUser().getUserId().toString() : null)
                .scrapCount(study.getScrapCount())
                .format(study.getFormat() != null ? study.getFormat().name() : null)
                .fieldName(study.getStudyField().getName())
                .styleNames(study.getStudyStyles().stream().map(ss -> ss.getStyle().getStyleName()).toList())
                .build()).toList();
    }

    public List<StudyCardDto> getNewStudies(int page, int size) {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        List<Study> studies = studyRepository.findNewStudies(oneMonthAgo, PageRequest.of(page, size));
        return studies.stream().map(study -> StudyCardDto.builder()
                .studyId(study.getId())
                .studyStatus(study.getStudyStatus())
                .title(study.getTitle())
                .authorId(study.getUser().getUserId() != null ? study.getUser().getUserId().toString() : null)
                .scrapCount(study.getScrapCount())
                .format(study.getFormat() != null ? study.getFormat().name() : null)
                .fieldName(study.getStudyField().getName())
                .styleNames(study.getStudyStyles().stream().map(ss -> ss.getStyle().getStyleName()).toList())
                .build()).toList();
    }

    @Transactional
    public void createStudy(StudyDtlDto dto) {
        // 작성자 조회
        User user = userRepository.findById(Long.parseLong(dto.getAuthorId()))
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        // 분야 조회
        StudyField field = studyFieldRepository.findByName(dto.getFieldName())
                .orElseThrow(() -> new IllegalArgumentException("분야 없음"));

        // Study 생성
        Study study = Study.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .studyStatus(StudyStatus.ACTIVE) // 기본값 ACTIVE
                .contactType(ContactType.valueOf(dto.getContactType()))
                .user(user)
                .studyField(field)
                .format(Format.valueOf(dto.getFormat().toUpperCase()))
                .build();
        studyRepository.save(study);

        // 스타일 저장
        if (dto.getStyleNames() != null) {
            List<Style> styles = styleRepository.findByStyleNameIn(dto.getStyleNames());
            for (Style style : styles) {
                StudyStyle studyStyle = StudyStyle.builder()
                        .study(study)
                        .style(style)
                        .build();
                studyStyleRepository.save(studyStyle);
            }
        }

        // 규칙 저장
        if (dto.getRules() != null) {
            dto.getRules().forEach((category, desc) -> {
                Rule rule = Rule.builder()
                        .study(study)
                        .ruleCategory(RuleCategory.valueOf(category))
                        .description(desc)
                        .build();
                ruleRepository.save(rule);
            });
        }
    }

    public StudyDtlDto getStudyDetail(Long studyId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("스터디 없음"));

        // 스타일 이름 리스트
        List<String> styleNames = study.getStudyStyles().stream()
                .map(ss -> ss.getStyle().getStyleName())
                .toList();

        // 규칙 맵
        List<Rule> rules = ruleRepository.findByStudy(study);
        Map<String, String> ruleMap = rules.stream()
                .collect(java.util.stream.Collectors.toMap(r -> r.getRuleCategory().name(), Rule::getDescription));

        return StudyDtlDto.builder()
                .fieldName(study.getStudyField().getName())
                .styleNames(styleNames)
                .format(study.getFormat() != null ? study.getFormat().name() : null)
                .contactType(study.getContactType().name())
                .title(study.getTitle())
                .description(study.getDescription())
                .rules(ruleMap)
                .authorId(study.getUser().getUserId() != null ? study.getUser().getUserId().toString() : null)
                .createdAt(study.getCreatedAt())
                .build();
    }

    public StudyDtlDto updateStudy(Long studyId, StudyDtlDto dto) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("studyId에 해당하는 스터디 없음"));


        // 분야 조회
        //사용자가 보내준 수정값(분야)가 db에 있는지 확인
        StudyField field = studyFieldRepository.findByName(dto.getFieldName())
                .orElseThrow(() -> new IllegalArgumentException("분야 없음"));


        // 스터디 정보 업데이트
        study.setTitle(dto.getTitle());
        study.setDescription(dto.getDescription());
        study.setContactType(ContactType.valueOf(dto.getContactType()));
        study.setStudyField(field);
        study.setFormat(Format.valueOf(dto.getFormat().toUpperCase()));
        studyRepository.save(study);

        // 기존 스타일 삭제 후 새로 저장
        studyStyleRepository.deleteByStudy(study);
        if (dto.getStyleNames() != null) {
            List<Style> styles = styleRepository.findByStyleNameIn(dto.getStyleNames());
            for (Style style : styles) {
                StudyStyle studyStyle = StudyStyle.builder()
                        .study(study)
                        .style(style)
                        .build();
                studyStyleRepository.save(studyStyle);
            }
        }

        // 기존 규칙 삭제 후 새로 저장
        ruleRepository.deleteByStudy(study);
        if (dto.getRules() != null) {
            dto.getRules().forEach((category, desc) -> {
                Rule rule = Rule.builder()
                        .study(study)
                        .ruleCategory(RuleCategory.valueOf(category))
                        .description(desc)
                        .build();
                ruleRepository.save(rule);
            });
        }

        return getStudyDetail(studyId);
    }

    public StudyDtlDto closedStudy(Long studyId)
    {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("studyId에 해당하는 스터디 없음"));
        study.setStudyStatus(StudyStatus.CLOSED);
        studyRepository.save(study);
        return getStudyDetail(studyId);
    }

    public void deleteStudy(Long studyId) {
        studyRepository.deleteByStudyId(studyId);
    }

    public void withdrawStudy(Long studyId, String userId) {
        // 프론트에서 현재 로그인한 사용자 userId로 받아야함
        studyRepository.withdraw(studyId, Long.parseLong(userId));
    }

    public List<StudyCardDto> searchStudies(
            List<String> fields,
            List<String> formats,
            List<String> styles,
            String status,
            String keyword,
            String sort,
            int page,
            int size
    ) {
        Specification<Study> spec = StudySpecifications.searchSpec(fields, formats, styles, status, keyword);
        Sort sortObj = ("old".equalsIgnoreCase(sort)) ? Sort.by("createdAt").ascending() : Sort.by("createdAt").descending();
        PageRequest pageable = PageRequest.of(page, size, sortObj);
        return studyRepository.findAll(spec, pageable)
                .stream()
                .map(study -> StudyCardDto.builder()
                        .studyId(study.getId())
                        .studyStatus(study.getStudyStatus())
                        .title(study.getTitle())
                        .authorId(study.getUser().getUserId() != null ? study.getUser().getUserId().toString() : null)
                        .scrapCount(study.getScrapCount())
                        .format(study.getFormat() != null ? study.getFormat().name() : null)
                        .fieldName(study.getStudyField().getName())
                        .styleNames(study.getStudyStyles().stream().map(ss -> ss.getStyle().getStyleName()).toList())
                        .build())
                .toList();
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

    public StudySessionCardDto createStudySession(Long studyId, StudySessionCardDto dto) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyException(StudyErrorCode.STUDY_NOT_FOUND));

        // 최대 회차 조회
        Integer maxNumber = study.getStudySessions().stream()
                .mapToInt(StudySession::getNumber)
                .max()
                .orElse(0);

        StudySession newSession = StudySession.createSession(
                maxNumber + 1,
                dto.getTitle(),
                study
        );


        StudySession savedSession = studySessionRepository.save(newSession);

        return StudySessionCardDto.builder()
                .sessionId(savedSession.getId())
                .sessionNumber(savedSession.getNumber())
                .title(savedSession.getTitle())
                .build();
    }

    public Map<String, String> getStudyRules(Long studyId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyException(StudyErrorCode.STUDY_NOT_FOUND));

        List<Rule> rules = ruleRepository.findByStudy(study);
        // 이거 코드 뭔 뜻인지 찾아보기
        Map<String, String> ruleMap = rules.stream()
                .collect(java.util.stream.Collectors.toMap(r -> r.getRuleCategory().name(), Rule::getDescription));
        return ruleMap;
    }

    public void updateStudyRules(Long studyId, Map<String, String> rules) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("스터디 없음"));

        // 기존 규칙 삭제
        ruleRepository.deleteByStudy(study);

        // 새 규칙 저장
        if (rules != null) {
            rules.forEach((category, desc) -> {
                Rule rule = Rule.builder()
                        .study(study)
                        .ruleCategory(RuleCategory.valueOf(category))
                        .description(desc)
                        .build();
                ruleRepository.save(rule);
            });
        }
    }

    // 스터디 멤버 조회
    public List<StudyMemberDto> getStudyMembers(Long studyId) {
        return studyRepository.findStudyMembers(studyId);
    }

    // 리더->팀원으로 역할 변경
    @Transactional
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

    @Transactional
    public StudyJournalDto createStudyJournal(Long sessionId, Long userId, StudyJournalDto dto) {
        // 세션 존재 확인
        StudySession session = studySessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("세션을 찾을 수 없음"));

        // 사용자 존재 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없음"));

        StudyJournal journal = StudyJournal.createJournal(
                dto.getTitle(),
                dto.getContent(),
                dto.getUrl(),
                dto.getFileUrl(), 
                dto.getFileName(), 
sessionId,
userId
        );

        StudyJournal savedJournal = studyJournalRepository.save(journal);

        return StudyJournalDto.builder()
                .journalId(savedJournal.getId())
                .title(savedJournal.getTitle())
                .content(savedJournal.getContent())
                .url(savedJournal.getUrl())
                .sessionId(savedJournal.getSessionId())
                .userId(savedJournal.getUserId())
                .build();
    }


    public StudyJournalDto getStudyJournal(Long journalId) {
        StudyJournal journal = studyJournalRepository.findById(journalId)
                .orElseThrow(() -> new IllegalArgumentException("일지를 찾을 수 없음"));

        StudyJournalDto.EmojiCounts emojiCounts = journalEmojiService.getEmojiCounts(journalId);

        return StudyJournalDto.builder()
                .journalId(journal.getId())
                .title(journal.getTitle())
                .content(journal.getContent())
                .url(journal.getUrl())
                .fileUrl(journal.getFileUrl())
                .fileName(journal.getFileName())
                .sessionId(journal.getSessionId())
                .userId(journal.getUserId())
                .emojiCounts(emojiCounts)
                .build();
    }

    public void updateStudySession(Long sessionId, StudySessionCardDto dto) {
        StudySession session = studySessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("세션을 찾을 수 없음"));

        session.updateTitle(dto.getTitle());
        studySessionRepository.save(session);
    }

    public StudySessionCardDto getStudySession(Long sessionId) {
        StudySession session = studySessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("세션을 찾을 수 없음"));

        return StudySessionCardDto.builder()
                .sessionId(session.getId())
                .sessionNumber(session.getNumber())
                .title(session.getTitle())
                .build();
    }



    @Transactional
    public StudyJournalDto updateStudyJournal(Long journalId, StudyJournalDto dto) {
        StudyJournal journal = studyJournalRepository.findById(journalId)
                .orElseThrow(() -> new IllegalArgumentException("일지를 찾을 수 없음"));

        // 일지 수정 메서드
        journal.updateJournal(
                dto.getTitle(), 
                dto.getContent(), 
                dto.getUrl(),
                dto.getFileUrl(),  
                dto.getFileName()
        );

        StudyJournal updatedJournal = studyJournalRepository.save(journal);

        return StudyJournalDto.builder()
                .journalId(updatedJournal.getId())
                .title(updatedJournal.getTitle())
                .content(updatedJournal.getContent())
                .url(updatedJournal.getUrl())
                .fileUrl(updatedJournal.getFileUrl())
                .fileName(updatedJournal.getFileName())
                .sessionId(updatedJournal.getSessionId())
                .userId(updatedJournal.getUserId())
                .build();
    }

    @Transactional
    public void deleteStudyJournal(Long journalId) {
        StudyJournal journal = studyJournalRepository.findById(journalId)
                .orElseThrow(() -> new IllegalArgumentException("일지를 찾을 수 없음"));

        studyJournalRepository.delete(journal);
    }



    public List<StudyJournalListDto> getStudyJournalsBySession(Long sessionId, int page, int size) {
        List<StudyJournal> allJournals = studyJournalRepository.findBySessionId(sessionId);
        Integer totalCount = allJournals.size();

        // 페이징 처리
        List<StudyJournal> journals = allJournals.stream()
                .skip((long) page * size)
                .limit(size)
                .toList();

        return journals.stream().map(journal -> {
            // 사용자 정보 조회
            User user = userRepository.findById(journal.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없음"));

            // 사용자의 스터디 역할 조회
            StudySession session = studySessionRepository.findById(sessionId)
                    .orElseThrow(() -> new IllegalArgumentException("세션을 찾을 수 없음"));

            String userRole = studyRepository.findUserRoleInStudy(journal.getUserId(), session.getStudy().getId());
            StudyRole studyRole = StudyRole.valueOf(userRole);

            // 해당 일지의 제출자 수 조회
            Integer submittedCount = studyJournalRepository.countByJournalId(journal.getId());

            return StudyJournalListDto.builder()
                    .journalId(journal.getId())
                    .title(journal.getTitle())
                    .content(journal.getContent())
                    .url(journal.getUrl())
                    .userId(journal.getUserId())
                    .studyRole(studyRole)
                    .submittedCount(submittedCount)
                    .totalCount(totalCount)
                    .build();
        }).toList();
    }

    // 스터디 지원
    @Transactional
    public org.growith.be.growith.domain.application.dto.ApplicationDto createApplication(Long studyId, Long userId, org.growith.be.growith.domain.application.dto.ApplicationDto applicationDto) {
        // 스터디 존재 확인
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("스터디를 찾을 수 없음"));

        // 사용자 존재 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없음"));

        // 지원서 생성
        org.growith.be.growith.domain.application.entity.StudyApplication application = org.growith.be.growith.domain.application.entity.StudyApplication.builder()
                .study(study)
                .user(user)
                .motivation(applicationDto.getMotivation())
                .applicationStatus(org.growith.be.growith.domain.application.entity.ApplicationStatus.PENDING)
                .build();

        org.growith.be.growith.domain.application.entity.StudyApplication savedApplication = studyApplicationRepository.save(application);

        return org.growith.be.growith.domain.application.dto.ApplicationDto.builder()
                .applicationId(savedApplication.getId())
                .studyId(savedApplication.getStudy().getId())
                .userId(savedApplication.getUser().getUserId())
                .nickName(savedApplication.getUser().getNickName())
                .major(savedApplication.getUser().getMajor().name())
                .studentStatus(savedApplication.getUser().getStudentStatus().name())
                .applicationStatus(savedApplication.getApplicationStatus())
                .motivation(savedApplication.getMotivation())
                .build();
    }

    // 지원자 승인/거절
    @Transactional
    public ApplicationDto updateApplicationStatus(Long applicationId, ApplicationStatus status) {
        StudyApplication application = studyApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("지원서를 찾을 수 없음"));

        // 상태 업데이트
        application.updateStatus(status);
        StudyApplication updatedApplication = studyApplicationRepository.save(application);

        // 만약 승인된 경우에만 UserStudy 추가
        if (status == ApplicationStatus.ACCEPTED) {
            UserStudy userStudy = UserStudy.builder()
                    .user(application.getUser())
                    .study(application.getStudy())
                    .studyRole(StudyRole.MEMBER)
                    .build();
            userStudyRepository.save(userStudy);
        }

        return ApplicationDto.builder()
                .applicationId(updatedApplication.getId())
                .studyId(updatedApplication.getStudy().getId())
                .userId(updatedApplication.getUser().getUserId())
                .nickName(updatedApplication.getUser().getNickName())
                .major(updatedApplication.getUser().getMajor().name())
                .studentStatus(updatedApplication.getUser().getStudentStatus().name())
                .applicationStatus(updatedApplication.getApplicationStatus())
                .motivation(updatedApplication.getMotivation())
                .build();
    }


    // 지원자 조회
    public List<org.growith.be.growith.domain.application.dto.ApplicationDto> getApplications(Long studyId) {
        List<org.growith.be.growith.domain.application.entity.StudyApplication> applications = studyApplicationRepository.findByStudyId(studyId);

        return applications.stream()
                .map(application -> org.growith.be.growith.domain.application.dto.ApplicationDto.builder()
                        .applicationId(application.getId())
                        .studyId(application.getStudy().getId())
                        .userId(application.getUser().getUserId())
                        .nickName(application.getUser().getNickName())
                        .major(application.getUser().getMajor().name())
                        .studentStatus(application.getUser().getStudentStatus().name())
                        .applicationStatus(application.getApplicationStatus())
                        .motivation(application.getMotivation())
                        .build())
                .toList();
    }
}
