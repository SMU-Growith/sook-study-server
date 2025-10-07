package org.growith.be.growith.domain.study.repository;

import org.growith.be.growith.domain.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface StudyRepository extends JpaRepository<Study, Long>, JpaSpecificationExecutor<Study> {
    @Query("SELECT s FROM Study s WHERE s.createdAt >= :oneMonthAgo ORDER BY s.scrapCount DESC")
    List<Study> findPopularStudies(@Param("oneMonthAgo") LocalDateTime oneMonthAgo, Pageable pageable);

    @Query("SELECT s FROM Study s WHERE s.createdAt >= :oneMonthAgo ORDER BY s.createdAt DESC")
    List<Study> findNewStudies(@Param("oneMonthAgo") LocalDateTime oneMonthAgo, Pageable pageable);
}