package org.growith.be.growith.domain.journal.service.query;

import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.journal.dto.StudyJournalDto;
import org.growith.be.growith.domain.journal.dto.StudyJournalListDto;
import org.growith.be.growith.domain.journal.dto.StudyJournalListResponse;
import org.growith.be.growith.domain.journal.dto.StudySessionListDto;
import org.growith.be.growith.domain.journal.dto.StudySession;
import org.growith.be.growith.domain.journal.dto.StudySessionCardDto;
import org.growith.be.growith.domain.journal.entity.StudyJournal;
import org.growith.be.growith.domain.journal.repository.StudyJournalRepository;
import org.growith.be.growith.domain.journal.service.JournalEmojiService;
import org.growith.be.growith.domain.study.entity.Study;
import org.growith.be.growith.domain.study.entity.enums.StudyRole;
import org.growith.be.growith.domain.study.repository.StudyRepository;
import org.growith.be.growith.domain.study.repository.StudySessionRepository;
import org.growith.be.growith.domain.user.entity.User;
import org.growith.be.growith.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StudyJournalQueryServiceImpl implements StudyJournalQueryService {

    private final StudySessionRepository studySessionRepository;
    private final StudyJournalRepository studyJournalRepository;
    private final UserRepository userRepository;
    private final StudyRepository studyRepository;
    private final JournalEmojiService journalEmojiService;

    public StudyJournalDto getStudyJournal(Long journalId, Long userId) {
        StudyJournal journal = studyJournalRepository.findById(journalId)
                .orElseThrow(() -> new IllegalArgumentException("일지를 찾을 수 없음"));

        // 조회수 증가
        journal.increaseViewCount();

        StudyJournalDto.EmojiCounts emojiCounts = journalEmojiService.getEmojiCounts(journalId);
        StudyJournalDto.EmojiStatus emojiStatus = journalEmojiService.getEmojiStatus(journalId, userId);

        // 첨부파일 DTO 변환
        List<StudyJournalDto.AttachmentDto> attachmentDtos = journal.getAttachments().stream()
                .map(att -> StudyJournalDto.AttachmentDto.builder()
                        .attachmentId(att.getId())
                        .fileUrl(att.getFileUrl())
                        .fileName(att.getFileName())
                        .fileSize(att.getFileSize())
                        .build())
                .toList();

        // 사용자 조회
        User user = userRepository.findById(journal.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없음"));

        // 세션 조회
        StudySession session = studySessionRepository.findById(journal.getSessionId())
                .orElseThrow(() -> new IllegalArgumentException("세션을 찾을 수 없음"));

        // 역할 조회
        String userRoleStr = studyRepository.findUserRoleInStudy(session.getStudy().getId(), user.getId());
        StudyRole studyRole = (userRoleStr != null) ? StudyRole.valueOf(userRoleStr) : StudyRole.MEMBER;

        return StudyJournalDto.builder()
                .journalId(journal.getId())
                .title(session.getTitle())
                .content(journal.getContent())
                .url(journal.getUrl())
                .nickName(user.getNickName())
                .studyRole(studyRole)
                .viewCount(journal.getViewCount())
                .attachments(attachmentDtos)
                .emojiCounts(emojiCounts)
                .emojiStatus(emojiStatus)
                .build();
    }

    public StudyJournalListResponse getStudyJournalsBySession(Long sessionId, int page, int size) {
        StudySession session = studySessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("세션을 찾을 수 없음"));

        List<StudyJournal> allJournals = studyJournalRepository.findBySessionId(sessionId);
        Integer totalCount = allJournals.size();

        // 페이징 처리
        List<StudyJournal> journals = allJournals.stream()
                .skip((long) page * size)
                .limit(size)
                .toList();

        List<StudyJournalListDto> journalDtos = journals.stream().map(journal -> {
            // 사용자 정보 조회
            User user = userRepository.findById(journal.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없음"));

            String userRoleStr = studyRepository.findUserRoleInStudy(session.getStudy().getId(), journal.getUserId());
            StudyRole studyRole = (userRoleStr != null) ? StudyRole.valueOf(userRoleStr) : StudyRole.MEMBER;

            return StudyJournalListDto.builder()
                    .journalId(journal.getId())
                    .title(session.getTitle())
                    .nickName(user.getNickName())
                    .studyRole(studyRole)
                    .viewCount(journal.getViewCount())
                    .build();
        }).toList();

        return StudyJournalListResponse.builder()
                .totalCount(totalCount)
                .sessionNumber(session.getNumber())
                .title(session.getTitle())
                .journals(journalDtos)
                .build();
    }

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

    public StudySessionCardDto getStudySessionById(Long sessionId) {
        StudySession session = studySessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("sessionId에 해당하는 세션 없음"));

        Integer submittedCount = studySessionRepository.countSubmittedBySessionId(session.getId());

        return StudySessionCardDto.builder()
                .sessionId(session.getId())
                .sessionNumber(session.getNumber())
                .title(session.getTitle())
                .submittedCount(submittedCount)
                .build();
    }

}
