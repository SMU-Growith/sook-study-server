package org.growith.be.growith.domain.study.service.query;

import org.growith.be.growith.domain.study.dto.request.StudyRequestDto;
import org.growith.be.growith.domain.study.dto.response.StudyResponseDto;
import org.growith.be.growith.domain.study.entity.Study;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudyQueryService {
    // 리팩토링 완료
    StudyResponseDto.StudyDetail getStudyDetail(Long studyId);
    List<Study> searchStudies(StudyRequestDto.SearchStudyCondition request, Pageable pageable);
    List<Study> getStudiesByPopularOrNew(Pageable pageable);


    // 리팩토링 전
    StudyResponseDto.StudyPreviewDTOList getMyStudies(String userId, int page, int size, String studyStatus);
}
