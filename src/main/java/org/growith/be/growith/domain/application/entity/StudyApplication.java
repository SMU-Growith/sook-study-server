package org.growith.be.growith.domain.application.entity;

import jakarta.persistence.*;
import lombok.*;
import org.growith.be.growith.domain.study.entity.Study;
import org.growith.be.growith.domain.user.entity.User;
import org.growith.be.growith.domain.user.entity.enums.StudentStatus;
import org.growith.be.growith.global.common.BaseEntity;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "study_application")
public class StudyApplication extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_application_id")
    private Long id;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private StudentStatus studentStatus;

    private String major;

    private String phoneNumber;

    @Column(length = 150)
    private String motivation;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study;

    public void updateStatus(ApplicationStatus status) {
        this.applicationStatus = status;
    }
}
