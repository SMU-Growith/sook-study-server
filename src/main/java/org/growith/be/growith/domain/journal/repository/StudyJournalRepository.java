package org.growith.be.growith.domain.journal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.growith.be.growith.domain.journal.entity.StudyJournal;
import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.Query;;
import org.springframework.data.repository.query.Param;

public interface StudyJournalRepository extends JpaRepository<StudyJournal, Long> {
    Optional<StudyJournal> findById(Long id);

    List<StudyJournal> findBySessionId(Long sessionId);

    @Query("SELECT COUNT(sj) FROM StudyJournal sj WHERE sj.id = :journalId")
    Integer countByJournalId(@Param("journalId") Long journalId);

    long countByUserId(Long userId);
}
