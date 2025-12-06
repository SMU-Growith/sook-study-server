package org.growith.be.growith.domain.scrap.converter;

import org.growith.be.growith.domain.scrap.entity.StudyScrap;
import org.growith.be.growith.domain.study.converter.StudyConverter;
import org.growith.be.growith.domain.study.dto.response.StudyResponseDto;
import org.growith.be.growith.domain.study.entity.Study;
import org.springframework.data.domain.Page;

import java.util.List;

public class ScrapConverter {

    public static StudyResponseDto.StudyPreviewDTOList toStudyPreviewList(Page<StudyScrap> scrapPage){
        List<StudyScrap> content = scrapPage.getContent();
        List<Study> list= content.stream().map(StudyScrap::getStudy)
                .toList();
        return StudyConverter.toStudyPreviewDTOList(list);
    }
    public static StudyResponseDto.ToggleScrapResponseDto toToggleScrapResponseDto(Study study, Boolean isScraped){
        return StudyResponseDto.ToggleScrapResponseDto.builder()
                .studyId(study.getId())
                .isScraped(isScraped)
                .scrapCount(study.getScrapCount())
                .build();
    }
}
