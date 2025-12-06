package org.growith.be.growith.domain.study.repository;

import org.growith.be.growith.domain.study.entity.UserStudy;
import org.growith.be.growith.domain.study.entity.enums.StudyRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserStudyRepository extends JpaRepository<UserStudy, Long> {
    // 스터디 id와 User Id로 UserStudy 반환
    Optional<UserStudy> findByStudyIdAndUserId(Long studyId, Long userId);

    // 스터디 id로 UserStudy 반환
    List<UserStudy> findByStudyId(Long studyId);

    @Query("SELECT COUNT(us) FROM UserStudy us WHERE us.study.id = :studyId")
    Long countByStudyId(Long studyId);

    Page<UserStudy> findByUserId(Long userId, Pageable pageable);

    // 해당 사용자가 스터디의 팀장인지 확인
    boolean existsByStudyIdAndUserIdAndStudyRole(Long studyId, Long userId, StudyRole studyRole);

    void deleteByStudyIdAndUserId(Long studyId, Long userId);
}
