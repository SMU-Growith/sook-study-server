package org.growith.be.growith.domain.study.repository;

import org.growith.be.growith.domain.study.entity.StudyField;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyFieldRepository extends JpaRepository<StudyField, Long> {
    Optional<StudyField> findByName(String name);
    
    // 1레벨(부모) 필드 조회
    List<StudyField> findByParentIsNullOrderBySortOrderAsc();
    
    // 특정 부모의 자식 필드 조회
    List<StudyField> findByParentOrderBySortOrderAsc(StudyField parent);
    
    // 2레벨(자식) 필드만 조회
    List<StudyField> findByParentIsNotNullOrderBySortOrderAsc();
}
