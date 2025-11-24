package org.growith.be.growith.domain.study.dto;

import lombok.*;

@Getter
@Builder
public class StudySessionCardDto {
    private Long sessionId;
    private Integer sessionNumber;
    private String title;
    private Integer submittedCount;
    private Integer totalCount;
}