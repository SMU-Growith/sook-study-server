package org.growith.be.growith.domain.study.service.query;

import org.growith.be.growith.domain.study.dto.response.StudyResponseDto;

public interface StudyQueryService {
    StudyResponseDto.StudyDetail getStudyDetail(Long studyId);

}
