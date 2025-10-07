package org.growith.be.growith.domain.study.dto;

import lombok.*;
import org.growith.be.growith.domain.study.entity.enums.StudyStatus;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class StudyCardDto {
    private Long studyId;
    private StudyStatus studyStatus;
    private String title;
    private String authorId;
    private Long scrapCount;
    private String format; // 진행방식
    private String fieldName; // 분야
    private List<String> styleNames;
}