package org.growith.be.growith.domain.study.service.query;

import org.growith.be.growith.domain.study.dto.request.StudyRequestDto;
import org.growith.be.growith.domain.study.dto.response.StudyResponseDto;
import org.growith.be.growith.domain.study.entity.Study;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudyQueryService {
    StudyResponseDto.StudyDetail getStudyDetail(Long studyId);
    List<Study> searchStudies(StudyRequestDto.SearchStudyCondition request, Pageable pageable);
}
