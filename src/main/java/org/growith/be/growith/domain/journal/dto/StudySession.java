package org.growith.be.growith.domain.journal.dto;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.growith.be.growith.domain.journal.entity.StudyJournal;
import org.growith.be.growith.domain.study.entity.Study;
import org.growith.be.growith.global.common.BaseEntity;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "study_session")
public class StudySession extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_session_id")
    private Long id;

    private int number; //회차

    private Boolean isSubmitted; //제출여부

    private Boolean isActive; //세션진행여부

    private String title; //세션 제목 . . 추가했어영

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_journal_id")
    private StudyJournal studyJournal;

    //세션 생성
    public static StudySession createSession(int number, String title, Study study) {
        StudySession session = new StudySession();
        session.number = number;
        session.title = title;
        session.isSubmitted = false;
        session.isActive = false;
        session.study = study;
        return session;
    }
    //세션 수정
    public void updateTitle(String title) {
        this.title = title;
    }
}

