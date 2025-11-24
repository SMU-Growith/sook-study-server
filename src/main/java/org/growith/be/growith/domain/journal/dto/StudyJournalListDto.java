package org.growith.be.growith.domain.journal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.growith.be.growith.domain.study.entity.enums.StudyRole;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyJournalListDto {
    private Long journalId;
    private String title;
    private String content;
    private String url;  // ✅ 추가
    private Long userId;
    private StudyRole studyRole;
    private Integer submittedCount;  // ✅ 추가
    private Integer totalCount;
}