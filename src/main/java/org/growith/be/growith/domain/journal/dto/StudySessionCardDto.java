package org.growith.be.growith.domain.journal.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudySessionCardDto {
    private Long sessionId;
    private Integer sessionNumber;
    private String title;
    private Integer submittedCount;
}
