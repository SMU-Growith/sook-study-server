package org.growith.be.growith.domain.journal.repository;

import org.growith.be.growith.domain.journal.entity.JournalEmoji;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JournalEmojiRepository extends JpaRepository<JournalEmoji, Long> {

    Optional<JournalEmoji> findByStudyJournalIdAndUserId(Long studyJournalId, Long userId);

    @Query("SELECT je.emojiType, COUNT(je) FROM JournalEmoji je WHERE je.studyJournalId = :studyJournalId GROUP BY je.emojiType")
    List<Object[]> countEmojisByJournalId(@Param("studyJournalId") Long studyJournalId);

    void deleteByStudyJournalIdAndUserId(Long studyJournalId, Long userId);
}
