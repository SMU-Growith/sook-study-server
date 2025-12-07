package org.growith.be.growith.domain.study.service.query;

import org.growith.be.growith.domain.study.dto.request.StudyRequestDto;
import org.growith.be.growith.domain.study.dto.response.StudyResponseDto;
import org.growith.be.growith.domain.study.entity.Study;
import org.growith.be.growith.domain.study.entity.enums.StudyStatus;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudyQueryService {
    // 리팩토링 완료
    StudyResponseDto.StudyDetail getStudyDetail(Long studyId, Long userId);
    List<Study> searchStudies(StudyRequestDto.SearchStudyCondition request, Pageable pageable);
    List<Study> getStudiesByPopularOrNew(Pageable pageable);
    List<StudyResponseDto.UserStudyPreviewDto> getMyStudies(Long userId, StudyStatus studyStatus, Pageable pageable);
    List<StudyResponseDto.StudyUsers> getStudyMembers(Long studyId);
    List<StudyResponseDto.StudyFieldDto> getStudyFields();
}
