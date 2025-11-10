package org.growith.be.growith.domain.journal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyJournalDto {
    private Long journalId;
    private String title;
    private String content;
    private String url;
    private Long sessionId;
    private Long userId;
    private Integer submittedCount;
}