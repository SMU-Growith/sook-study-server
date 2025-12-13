package org.growith.be.growith.domain.application.repository;

import org.growith.be.growith.domain.application.entity.StudyApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyApplicationRepository extends JpaRepository<StudyApplication, Long> {
    List<StudyApplication> findByStudyId(Long studyId);
    StudyApplication findByStudyIdAndUserId(Long studyId, Long userId);
    List<StudyApplication> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    @Query("SELECT CASE WHEN COUNT(sa) > 0 THEN true ELSE false END FROM StudyApplication sa WHERE sa.study.id = :studyId AND sa.user.id = :userId")
    boolean existsByStudyIdAndUserId(@Param("studyId") Long studyId, @Param("userId") Long userId);
}
