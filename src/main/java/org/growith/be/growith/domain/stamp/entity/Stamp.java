package org.growith.be.growith.domain.stamp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.growith.be.growith.domain.stamp.entity.enums.StampLevel;
import org.growith.be.growith.domain.stamp.entity.enums.StampType;
import org.growith.be.growith.global.common.BaseEntity;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "stamp")
public class Stamp extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name  = "stamp_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StampType stampType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StampLevel stampLevel;

    private String name;

    private String description;
}
