package org.growith.be.growith.domain.journal.service.command;

import org.growith.be.growith.domain.journal.dto.CreateStudySessionRequest;
import org.growith.be.growith.domain.journal.dto.StudyJournalDto;
import org.growith.be.growith.domain.journal.dto.StudySessionCardDto;

public interface StudyJournalCommandService {
    // 스터디 세션 삭제 (팀장 권한 필요)
    void deleteStudySession(Long sessionId, Long userId);
    StudyJournalDto createStudyJournal(Long sessionId, Long userId, StudyJournalDto dto);
    void updateStudySession(Long sessionId, StudySessionCardDto dto);
    StudyJournalDto updateStudyJournal(Long journalId, StudyJournalDto dto);
    void deleteStudyJournal(Long journalId);
    StudySessionCardDto createStudySession(Long studyId, CreateStudySessionRequest request);
}
