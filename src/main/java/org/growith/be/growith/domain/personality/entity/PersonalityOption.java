package org.growith.be.growith.domain.personality.entity;

import jakarta.persistence.*;
import lombok.*;
import org.growith.be.growith.domain.personality.entity.enums.PersonalityType;
import org.growith.be.growith.global.common.BaseEntity;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "personality_option")
public class PersonalityOption extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private PersonalityQuestion question;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String optionText;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PersonalityType personalityType;
}
