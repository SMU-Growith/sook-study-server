package org.growith.be.growith.domain.journal.service.query;

import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.journal.dto.StudyJournalDto;
import org.growith.be.growith.domain.journal.dto.StudyJournalListDto;
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

    public StudyJournalDto getStudyJournal(Long journalId) {
        StudyJournal journal = studyJournalRepository.findById(journalId)
                .orElseThrow(() -> new IllegalArgumentException("일지를 찾을 수 없음"));

        StudyJournalDto.EmojiCounts emojiCounts = journalEmojiService.getEmojiCounts(journalId);

        // 첨부파일 DTO 변환
        List<StudyJournalDto.AttachmentDto> attachmentDtos = journal.getAttachments().stream()
                .map(att -> StudyJournalDto.AttachmentDto.builder()
                        .attachmentId(att.getId())
                        .fileUrl(att.getFileUrl())
                        .fileName(att.getFileName())
                        .fileSize(att.getFileSize())
                        .build())
                .toList();

        return StudyJournalDto.builder()
                .journalId(journal.getId())
                .title(journal.getTitle())
                .content(journal.getContent())
                .url(journal.getUrl())
                .sessionId(journal.getSessionId())
                .userId(journal.getUserId())
                .attachments(attachmentDtos)
                .emojiCounts(emojiCounts)
                .build();
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
}
