package org.growith.be.growith.domain.study.entity;

import jakarta.persistence.*;
import lombok.*;
import org.growith.be.growith.domain.study.entity.enums.RuleCategory;
import org.growith.be.growith.global.common.BaseEntity;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "rule")
public class Rule extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name  = "rule_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private RuleCategory ruleCategory;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="study_id")
    private Study study;

    public void update(RuleCategory ruleCategory, String description) {
        this.ruleCategory = ruleCategory;
        this.description = description;
    }
}
