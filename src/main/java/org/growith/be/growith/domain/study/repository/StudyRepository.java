package org.growith.be.growith.domain.study.repository;

import org.growith.be.growith.domain.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.growith.be.growith.domain.study.entity.enums.*;
import org.growith.be.growith.domain.user.entity.User;
import org.growith.be.growith.domain.application.entity.StudyApplication;
import java.time.LocalDateTime;
import java.util.List;

public interface StudyRepository extends JpaRepository<Study, Long>, JpaSpecificationExecutor<Study> {
    @Query("SELECT s FROM Study s WHERE s.createdAt >= :oneMonthAgo ORDER BY s.scrapCount DESC")
    List<Study> findPopularStudies(@Param("oneMonthAgo") LocalDateTime oneMonthAgo, Pageable pageable);

    @Query("SELECT s FROM Study s WHERE s.createdAt >= :oneMonthAgo ORDER BY s.createdAt DESC")
    List<Study> findNewStudies(@Param("oneMonthAgo") LocalDateTime oneMonthAgo, Pageable pageable);

    @Query("SELECT s FROM Study s WHERE s.user.id = :userId AND s.studyStatus = :studyStatus ORDER BY s.createdAt DESC")
    List<Study> findMyStudies(@Param("userId") Long userId, Pageable pageable, @Param("studyStatus") StudyStatus studyStatus);

    @Query("SELECT COUNT(us) FROM UserStudy us WHERE us.study.id = :studyId")
    Integer countMembersByStudyId(@Param("studyId") Long studyId);

    @Query("SELECT us.studyRole FROM UserStudy us WHERE us.study.id = :studyId AND us.user.id = :userId")
    String findUserRoleInStudy(@Param("studyId") Long studyId, @Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM UserStudy us WHERE us.study.id = :studyId AND us.user.id = :userId")
    void withdraw(@Param("studyId") Long studyId, @Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Study s WHERE s.id= :studyId")
    void deleteByStudyId(@Param("studyId") Long studyId);

    @Query("SELECT new org.growith.be.growith.domain.study.dto.StudyMemberDto(" +
            "u.id, u.nickName, u.studentStatus, u.major, u.phoneNumber, sa.motivation) " +
            "FROM UserStudy us " +
            "JOIN us.user u " +
            "LEFT JOIN StudyApplication sa ON sa.user = u AND sa.study = us.study " +
            "WHERE us.study.id = :studyId")
    List<org.growith.be.growith.domain.study.dto.StudyMemberDto> findStudyMembers(@Param("studyId") Long studyId);

}
