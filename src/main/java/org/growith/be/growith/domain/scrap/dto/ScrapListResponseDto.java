package org.growith.be.growith.domain.scrap.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.growith.be.growith.domain.study.dto.StudyCardDto;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ScrapListResponseDto {
    private List<StudyCardDto> scraps;
    private long totalCount;
}
