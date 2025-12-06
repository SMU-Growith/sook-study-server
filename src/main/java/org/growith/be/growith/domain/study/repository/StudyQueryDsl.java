package org.growith.be.growith.domain.study.repository;

import org.growith.be.growith.domain.study.dto.request.StudyRequestDto;
import org.growith.be.growith.domain.study.entity.Study;
import org.springframework.data.domain.PageRequest;

import java.awt.print.Pageable;
import java.util.List;

public interface StudyQueryDsl {
    public List<Study> searchStudy(StudyRequestDto.SearchStudyCondition request, PageRequest pageRequest);
}
