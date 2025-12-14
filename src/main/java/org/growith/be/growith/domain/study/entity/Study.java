package org.growith.be.growith.domain.study.entity;


import jakarta.persistence.*;
import lombok.*;
import org.growith.be.growith.domain.journal.dto.StudySession;
import org.growith.be.growith.domain.study.dto.request.StudyRequestDto;
import org.growith.be.growith.domain.study.entity.enums.ContactType;
import org.growith.be.growith.domain.study.entity.enums.StudyStatus;
import org.growith.be.growith.domain.study.entity.enums.StudyStyleCategory;
import org.growith.be.growith.global.common.BaseEntity;
import org.growith.be.growith.domain.user.entity.User;
import org.growith.be.growith.domain.study.entity.enums.StudyFormat;

import java.time.LocalDateTime;
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

    // 연락 방식에 따른 url
    private String url;

    private Boolean isRecruiting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_field_id")
    private StudyField studyField;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 스터디 진행 방식
    @Enumerated(EnumType.STRING)
    private StudyFormat studyFormat;

    @Enumerated(EnumType.STRING)
    private StudyStyleCategory studyStyleCategory;

        
    @OneToMany(mappedBy = "study")
    private List<StudySession> studySessions;

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

    public void updateStudy(StudyRequestDto.UpdateStudyDTO request, StudyField studyField) {
        this.title = request.title();
        this.description = request.description();
        this.contactType = request.contactType();
        this.url = request.url();
        this.studyStatus  = request.studyStatus();
        this.studyFormat = request.studyFormat();
        this.studyField = studyField;
    }

    public void changeStudyStatus (StudyStatus studyStatus) {
        this.studyStatus = studyStatus;
    }

    public void toggleIsRecruiting() {
        this.isRecruiting = !this.isRecruiting;
    }
}
