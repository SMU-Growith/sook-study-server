package org.growith.be.growith.domain.journal.service.command;

import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.journal.dto.StudyJournalDto;
import org.growith.be.growith.domain.journal.dto.StudySession;
import org.growith.be.growith.domain.journal.entity.StudyJournal;
import org.growith.be.growith.domain.journal.repository.StudyJournalRepository;
import org.growith.be.growith.domain.journal.dto.StudySessionCardDto;
import org.growith.be.growith.domain.study.entity.Study;
import org.growith.be.growith.domain.study.repository.StudyRepository;
import org.growith.be.growith.domain.study.repository.StudySessionRepository;
import org.growith.be.growith.domain.user.entity.User;
import org.growith.be.growith.domain.user.repository.UserRepository;
import org.growith.be.growith.global.error.code.status.StudyErrorCode;
import org.growith.be.growith.global.error.exception.handler.StudyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class StudyJournalCommandServiceImpl implements StudyJournalCommandService{
    private final StudySessionRepository studySessionRepository;
    private final StudyJournalRepository studyJournalRepository;
    private final UserRepository userRepository;
    private final StudyRepository studyRepository;

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

    public void deleteStudyJournal(Long journalId) {
        StudyJournal journal = studyJournalRepository.findById(journalId)
                .orElseThrow(() -> new IllegalArgumentException("일지를 찾을 수 없음"));

        studyJournalRepository.delete(journal);
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

}
