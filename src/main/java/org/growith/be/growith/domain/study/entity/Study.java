package org.growith.be.growith.domain.study.entity;


import jakarta.persistence.*;
import lombok.*;
import org.growith.be.growith.domain.study.entity.enums.ContactType;
import org.growith.be.growith.domain.study.entity.enums.StudyStatus;
import org.growith.be.growith.global.common.BaseEntity;
import org.growith.be.growith.domain.user.entity.User;
import org.growith.be.growith.domain.study.entity.enums.Format;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "study")
public class Study extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name  = "study_id")
    private Long id;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private StudyStatus studyStatus;

    @Enumerated(EnumType.STRING)
    private ContactType contactType;

    private String url;

    private Boolean isRecruiting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_field_id")
    private StudyField studyField;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Format format;

    @OneToMany(mappedBy = "study", fetch = FetchType.LAZY)
    private List<StudyStyle> studyStyles;

    @Builder.Default
    @Column(name = "scrap_count", nullable = false)
    private Long scrapCount = 0L;

    public void increaseScrapCount() {
        this.scrapCount++;
    }

    public void decreaseScrapCount() {
        if (this.scrapCount > 0) {
            this.scrapCount--;
        }
    }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setContactType(ContactType contactType) { this.contactType = contactType; }
    public void setStudyField(StudyField studyField) { this.studyField = studyField; }
    public void setFormat(Format format) { this.format = format; }
    public void setStudyStatus(StudyStatus status) { this.studyStatus = status; }
    public java.time.LocalDateTime getCreatedAt() { return super.getCreatedAt(); }
}