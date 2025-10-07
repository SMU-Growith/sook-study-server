package org.growith.be.growith.domain.study.repository;

import org.growith.be.growith.domain.study.entity.Study;
import org.growith.be.growith.domain.study.entity.StudyStyle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyStyleRepository extends JpaRepository<StudyStyle, Long> {
    void deleteByStudy(Study study);
}
