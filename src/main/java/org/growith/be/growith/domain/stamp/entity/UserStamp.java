package org.growith.be.growith.domain.stamp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.growith.be.growith.domain.stamp.entity.enums.StampLevel;
import org.growith.be.growith.domain.stamp.entity.enums.StampType;
import org.growith.be.growith.domain.user.entity.User;
import org.growith.be.growith.global.common.BaseEntity;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_stamp", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "stamp_type"})
})
public class UserStamp extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name  = "user_stamp_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "stamp_type", nullable = false)
    private StampType stampType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StampLevel achievedLevel = StampLevel.NONE;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isAchieved = false;

    // 스탬프 레벨 업데이트
    public void updateLevel(StampLevel newLevel) {
        this.achievedLevel = newLevel;
        this.isAchieved = newLevel != StampLevel.NONE;
    }
}
