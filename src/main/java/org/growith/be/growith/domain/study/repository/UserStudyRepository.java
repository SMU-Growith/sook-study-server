package org.growith.be.growith.domain.study.repository;

import org.growith.be.growith.domain.study.entity.UserStudy;
import org.growith.be.growith.domain.study.entity.enums.StudyRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.growith.be.growith.domain.study.entity.enums.StudyStatus;

import java.util.List;
import java.util.Optional;

public interface UserStudyRepository extends JpaRepository<UserStudy, Long> {
    // 스터디 id와 User Id로 UserStudy 반환
    Optional<UserStudy> findByStudyIdAndUserId(Long studyId, Long userId);

    // 스터디 id로 UserStudy 반환
    List<UserStudy> findByStudyId(Long studyId);

    @Query("SELECT COUNT(us) FROM UserStudy us WHERE us.study.id = :studyId " +
           "AND us.studyRole != org.growith.be.growith.domain.study.entity.enums.StudyRole.WITHDRAWN")
    Long countByStudyId(Long studyId);

    @Query("SELECT us FROM UserStudy us WHERE us.user.id = :userId " +
           "AND ((:studyStatus = org.growith.be.growith.domain.study.entity.enums.StudyStatus.ACTIVE " +
           "      AND us.study.studyStatus = org.growith.be.growith.domain.study.entity.enums.StudyStatus.ACTIVE " +
           "      AND us.studyRole != org.growith.be.growith.domain.study.entity.enums.StudyRole.WITHDRAWN) " +
           "OR (:studyStatus = org.growith.be.growith.domain.study.entity.enums.StudyStatus.CLOSED " +
           "    AND (us.study.studyStatus = org.growith.be.growith.domain.study.entity.enums.StudyStatus.CLOSED " +
           "         OR us.studyRole = org.growith.be.growith.domain.study.entity.enums.StudyRole.WITHDRAWN)))")
    Page<UserStudy> findByUserIdAndStatus(Long userId, StudyStatus studyStatus, Pageable pageable);

    // 해당 사용자가 스터디의 팀장인지 확인
    boolean existsByStudyIdAndUserIdAndStudyRole(Long studyId, Long userId, StudyRole studyRole);

    void deleteByStudyIdAndUserId(Long studyId, Long userId);
    
    // 스터디 삭제 시 모든 user_study 삭제
    void deleteByStudyId(Long studyId);
}
