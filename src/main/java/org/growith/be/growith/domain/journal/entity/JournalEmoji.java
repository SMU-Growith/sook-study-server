package org.growith.be.growith.domain.journal.entity;

import jakarta.persistence.*;
import lombok.*;
import org.growith.be.growith.global.common.BaseEntity;

@Entity
@Table(name = "journal_emoji",
        uniqueConstraints = @UniqueConstraint(columnNames = {"study_journal_id", "user_id"}))
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JournalEmoji extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "study_journal_id", nullable = false)
    private Long studyJournalId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmojiType emojiType;

    public enum EmojiType {
        HEART, LIKE, LAUGH, SURPRISE, CURIOSITY
    }

    public void updateEmojiType(EmojiType emojiType) {
        this.emojiType = emojiType;
    }
}
