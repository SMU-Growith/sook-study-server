package org.growith.be.growith.domain.journal.service.query;

import org.growith.be.growith.domain.journal.dto.StudyJournalDto;
import org.growith.be.growith.domain.journal.dto.StudyJournalListDto;
import org.growith.be.growith.domain.journal.dto.StudySessionCardDto;

import java.awt.print.Pageable;
import java.util.List;

public interface StudyJournalQueryService {
    StudyJournalDto getStudyJournal(Long journalId);
    List<StudyJournalListDto> getStudyJournalsBySession(Long sessionId, int page, int size);
    List<StudySessionCardDto> getStudySessions(Long studyId, int page, int size);

}
