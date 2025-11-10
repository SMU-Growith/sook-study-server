package org.growith.be.growith.domain.journal.repository;

import org.growith.be.growith.domain.journal.entity.StudyJournal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyJournalRepository extends JpaRepository<StudyJournal, Long> {
    Optional<StudyJournal> findById(Long id);

    List<StudyJournal> findBySessionId(Long sessionId);

    @Query("SELECT COUNT(sj) FROM StudyJournal sj WHERE sj.id = :journalId")
    Integer countByJournalId(@Param("journalId") Long journalId);

}
