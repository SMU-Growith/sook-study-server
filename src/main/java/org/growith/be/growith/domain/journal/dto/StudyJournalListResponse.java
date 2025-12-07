package org.growith.be.growith.domain.journal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyJournalListResponse {
    private Integer totalCount; // 전체 일지 수
    private Integer sessionNumber; // 세션 회차
    private String title; // 세션 제목
    private List<StudyJournalListDto> journals; // 일지 목록
}
