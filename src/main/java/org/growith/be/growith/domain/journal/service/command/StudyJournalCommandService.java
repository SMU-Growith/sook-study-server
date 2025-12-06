package org.growith.be.growith.domain.journal.service.command;

public interface StudyJournalCommandService {
    // 스터디 세션 삭제 (팀장 권한 필요)
    void deleteStudySession(Long sessionId, Long userId);
}
