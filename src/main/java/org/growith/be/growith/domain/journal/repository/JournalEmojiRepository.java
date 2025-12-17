package org.growith.be.growith.domain.journal.repository;

import org.growith.be.growith.domain.journal.entity.JournalEmoji;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JournalEmojiRepository extends JpaRepository<JournalEmoji, Long> {

    Optional<JournalEmoji> findByStudyJournalIdAndUserIdAndEmojiType(Long studyJournalId, Long userId, JournalEmoji.EmojiType emojiType);

    List<JournalEmoji> findAllByStudyJournalIdAndUserId(Long studyJournalId, Long userId);

    @Query("SELECT je.emojiType, COUNT(je) FROM JournalEmoji je WHERE je.studyJournal.id = :studyJournalId GROUP BY je.emojiType")
    List<Object[]> countEmojisByJournalId(@Param("studyJournalId") Long studyJournalId);

    void deleteByStudyJournalIdAndUserId(Long studyJournalId, Long userId);

    @Query("SELECT COUNT(je) FROM JournalEmoji je JOIN StudyJournal sj ON je.studyJournal.id = sj.id WHERE sj.user.id = :userId")
    Long countReceivedCheersByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(DISTINCT sj.user.id) FROM JournalEmoji je JOIN StudyJournal sj ON je.studyJournal.id = sj.id WHERE je.user.id = :userId")
    Long countDistinctCheeredUsers(@Param("userId") Long userId);
}
