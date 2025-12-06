package org.growith.be.growith.domain.study.repository;

import org.growith.be.growith.domain.study.dto.request.StudyRequestDto;
import org.growith.be.growith.domain.study.entity.Study;
import org.growith.be.growith.domain.study.entity.StudyField;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudyQueryDsl {
    List<Study> searchStudy(StudyRequestDto.SearchStudyCondition request, List<StudyField> studyFields, PageRequest pageRequest);
    List<Study> getStudySortByPopularOrNew(Pageable pageable);

}
