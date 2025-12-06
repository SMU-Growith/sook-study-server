package org.growith.be.growith.domain.journal.repository;

import org.growith.be.growith.domain.journal.entity.JournalAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JournalAttachmentRepository extends JpaRepository<JournalAttachment, Long> {
    List<JournalAttachment> findByStudyJournalId(Long studyJournalId);
    void deleteByStudyJournalId(Long studyJournalId);
}
