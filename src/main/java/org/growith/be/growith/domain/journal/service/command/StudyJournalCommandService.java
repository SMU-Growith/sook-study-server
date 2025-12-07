package org.growith.be.growith.domain.journal.service.command;

import org.growith.be.growith.domain.journal.dto.CreateStudySessionRequest;
import org.growith.be.growith.domain.journal.dto.StudyJournalDto;
import org.growith.be.growith.domain.journal.dto.StudySessionCardDto;
import org.growith.be.growith.domain.journal.dto.UpdateStudySessionRequest;
import org.growith.be.growith.domain.journal.dto.UpdateStudyJournalRequest;
import org.growith.be.growith.domain.journal.dto.CreateStudyJournalRequest;

public interface StudyJournalCommandService {
    // 스터디 세션 삭제 (팀장 권한 필요)
    void deleteStudySession(Long sessionId, Long userId);
    StudyJournalDto createStudyJournal(Long sessionId, Long userId, CreateStudyJournalRequest request);
    void updateStudySession(Long sessionId, UpdateStudySessionRequest request);
    StudyJournalDto updateStudyJournal(Long journalId, UpdateStudyJournalRequest request);
    void deleteStudyJournal(Long journalId);
    StudySessionCardDto createStudySession(Long studyId, CreateStudySessionRequest request);
}
