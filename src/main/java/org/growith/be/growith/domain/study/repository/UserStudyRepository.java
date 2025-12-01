package org.growith.be.growith.domain.study.repository;

import org.growith.be.growith.domain.study.entity.UserStudy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserStudyRepository extends JpaRepository<UserStudy, Long> {
    // 스터디 id와 User Id로 UserStudy 반환
    Optional<UserStudy> findByStudyIdAndUserId(Long studyId, Long userId);

    // 스터디 id로 UserStudy 반환
    List<UserStudy> findByStudyId(Long studyId);
}
