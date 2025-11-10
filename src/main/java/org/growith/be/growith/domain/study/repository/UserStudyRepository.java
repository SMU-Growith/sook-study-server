package org.growith.be.growith.domain.study.repository;

import org.growith.be.growith.domain.study.entity.UserStudy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserStudyRepository extends JpaRepository<UserStudy, Long> {
    Optional<UserStudy> findByStudyIdIdAndUserIdId(Long studyId, Long userId);
}
