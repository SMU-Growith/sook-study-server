package org.growith.be.growith.domain.journal.entity;

import jakarta.persistence.*;
import lombok.*;
import org.growith.be.growith.domain.user.entity.User;
import org.growith.be.growith.global.common.BaseEntity;

@Entity
@Table(name = "journal_emoji",
        uniqueConstraints = @UniqueConstraint(columnNames = {"study_journal_id", "user_id", "emoji_type"}))
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JournalEmoji extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "journal_emoji_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_journal_id")
    private StudyJournal studyJournal;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "emoji_type", nullable = false)
    private EmojiType emojiType;

    public enum EmojiType {
        HEART, LIKE, LAUGH, SURPRISE, CURIOSITY
    }

    public void updateEmojiType(EmojiType emojiType) {
        this.emojiType = emojiType;
    }
}
