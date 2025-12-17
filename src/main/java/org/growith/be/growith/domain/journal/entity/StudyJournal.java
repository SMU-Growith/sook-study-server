package org.growith.be.growith.domain.journal.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.growith.be.growith.domain.journal.dto.StudySession;
import org.growith.be.growith.domain.user.entity.User;
import org.growith.be.growith.global.common.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "study_journal")
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyJournal extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_journal_id")
    private Long id;

    private String content;

    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_session_id")
    private StudySession studySession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder.Default
    private Long viewCount = 0L;

    @OneToMany(mappedBy = "studyJournal", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<JournalAttachment> attachments = new ArrayList<>();

    public static StudyJournal createJournal(
            String content,
            String url,
            StudySession studySession,
            User user
    ) {
        return StudyJournal.builder()
                .content(content)
                .url(url)
                .studySession(studySession)
                .user(user)
                .viewCount(0L)
                .attachments(new ArrayList<>())
                .build();
    }

    public void updateJournal(String content, String url) {
        this.content = content;
        this.url = url;
    }

    public void increaseViewCount() {
        this.viewCount++;
    }


    // 첨부파일 추가
    public void addAttachment(JournalAttachment attachment) {
        this.attachments.add(attachment);
    }

    // 첨부파일 전체 교체
    public void replaceAttachments(List<JournalAttachment> newAttachments) {
        this.attachments.clear();
        this.attachments.addAll(newAttachments);
    }
}
