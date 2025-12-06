package org.growith.be.growith.domain.journal.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class StudySessionListDto {
    private List<StudySessionCardDto> studySessions;
    private Integer totalCount;
}
