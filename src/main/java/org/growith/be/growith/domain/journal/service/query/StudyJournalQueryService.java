package org.growith.be.growith.domain.journal.service.query;

import org.growith.be.growith.domain.journal.dto.StudyJournalDto;
import org.growith.be.growith.domain.journal.dto.StudyJournalListDto;
import org.growith.be.growith.domain.journal.dto.StudyJournalListResponse;
import org.growith.be.growith.domain.journal.dto.StudySessionCardDto;
import org.growith.be.growith.domain.journal.dto.StudySessionListDto;

import java.awt.print.Pageable;
import java.util.List;

public interface StudyJournalQueryService {
    StudyJournalDto getStudyJournal(Long journalId);
    StudyJournalListResponse getStudyJournalsBySession(Long sessionId, int page, int size);
    StudySessionListDto getStudySessions(Long studyId, int page, int size);
    StudySessionCardDto getStudySessionById(Long sessionId);

}
