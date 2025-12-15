package org.growith.be.growith.domain.personality.entity;

import jakarta.persistence.*;
import lombok.*;
import org.growith.be.growith.domain.personality.entity.enums.ResultTypeCode;
import org.growith.be.growith.global.common.BaseEntity;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "personality_result_type")
public class PersonalityResultType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_type_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private ResultTypeCode typeCode;

    @Column(nullable = false, length = 50)
    private String typeName;

    @Column(nullable = false, length = 50)
    private String typeCategory;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String tagline;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String caution;
}
