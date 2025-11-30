package org.growith.be.growith.domain.study.converter;

import org.growith.be.growith.domain.study.dto.request.StudyRequestDto;
import org.growith.be.growith.domain.study.dto.response.StudyResponseDto;
import org.growith.be.growith.domain.study.entity.Study;

import java.util.List;

public class StudyConverter {

    // Study -> StudyCardDto
    public static StudyResponseDto.StudyCardDto toStudyCardDto(Study study, Integer memberCount, Integer studyDays) {
        List<String> list = study.getStudyStyles().stream().map(ss -> ss.getStyle().getStyleName()).toList();

        return StudyResponseDto.StudyCardDto.builder()
                .studyId(study.getId())
                .studyStatus(study.getStudyStatus())
                .title(study.getTitle())
                .format(study.getFormat() != null ? study.getFormat().name() : null)
                .fieldName(study.getStudyField().getName())
                .styleNames(list)
                .memberCount(memberCount)
                .studyDays(studyDays)
                .build();
    }
}
