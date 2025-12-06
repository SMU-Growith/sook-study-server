package org.growith.be.growith.domain.application.repository;

import org.growith.be.growith.domain.application.entity.StudyApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyApplicationRepository extends JpaRepository<StudyApplication, Long> {
    List<StudyApplication> findByStudyId(Long studyId);
    StudyApplication findByStudyIdAndUserId(Long studyId, Long userId);
}
