package org.growith.be.growith.domain.personality.entity;

import jakarta.persistence.*;
import lombok.*;
import org.growith.be.growith.domain.user.entity.User;
import org.growith.be.growith.global.common.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_personality_test")
public class UserPersonalityTest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "result_type_id")
    private PersonalityResultType resultType;

    @Column(nullable = false)
    @Builder.Default
    private Integer plannedCount = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer freeCount = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer cooperativeCount = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer achievementCount = 0;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isSaved = false;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL)
    @Builder.Default
    private List<UserPersonalityAnswer> answers = new ArrayList<>();

    public void setResultType(PersonalityResultType resultType) {
        this.resultType = resultType;
    }

    public void markAsSaved() {
        this.isSaved = true;
    }
}
