package org.growith.be.growith.domain.study.dto;

import lombok.*;
import org.growith.be.growith.domain.study.entity.enums.StudyStatus;
import org.growith.be.growith.domain.study.entity.enums.StudyStyleCategory;

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
    private StudyStyleCategory styleNames;

    // 내 스터디 조회에서 사용
    private Integer memberCount;    // 스터디원 수
    private Integer studyDays;  // 스터디 진행 일수
    private String userRole; // 사용자의 스터디 내 역할
}
