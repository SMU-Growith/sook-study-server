package org.growith.be.growith.domain.study.entity;

import jakarta.persistence.*;
import lombok.*;
import org.growith.be.growith.global.common.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "study_field")
public class StudyField extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name  = "study_field_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private StudyField parent;

    @Builder.Default
    @OneToMany(mappedBy = "parent")
    private List<StudyField> studyField = new ArrayList<>();

    private String name;

    private int level;

    private int sort_order;

}
