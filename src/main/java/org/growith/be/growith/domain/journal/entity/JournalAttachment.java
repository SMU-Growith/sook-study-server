package org.growith.be.growith.domain.journal.entity;

import jakarta.persistence.*;
import lombok.*;
import org.growith.be.growith.global.common.BaseEntity;

@Entity
@Table(name = "journal_attachment")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JournalAttachment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attachment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_journal_id", nullable = false)
    private StudyJournal studyJournal;

    @Column(nullable = false)
    private String fileUrl;

    @Column(nullable = false)
    private String fileName;

    private Long fileSize;

    public static JournalAttachment create(StudyJournal studyJournal, String fileUrl, String fileName, Long fileSize) {
        return JournalAttachment.builder()
                .studyJournal(studyJournal)
                .fileUrl(fileUrl)
                .fileName(fileName)
                .fileSize(fileSize)
                .build();
    }
}
