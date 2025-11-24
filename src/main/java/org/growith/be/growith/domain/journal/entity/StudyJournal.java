package org.growith.be.growith.domain.journal.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.growith.be.growith.global.common.BaseEntity;

@Setter
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "study_journal")
public class StudyJournal extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private String url;

    private Long sessionId;

    private Long userId;

    public static StudyJournal createJournal(String title, String content, String url, Long sessionId, Long userId) {
        return StudyJournal.builder()
                .title(title)
                .content(content)
                .url(url)
                .sessionId(sessionId)
                .userId(userId)
                .build();
    }

    public void updateJournal(String title, String content, String url) {
        this.title = title;
        this.content = content;
        this.url = url;
    }

}
