package org.growith.be.growith.domain.personality.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "personality_test_stats")
public class PersonalityTestStats {

    @Id
    @Column(name = "stats_id")
    private Long id;

    @Column(nullable = false)
    @Builder.Default
    private Integer totalParticipants = 0;

    public void incrementParticipants() {
        this.totalParticipants++;
    }
}
