package org.growith.be.growith.domain.study.repository;

import org.growith.be.growith.domain.journal.dto.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudySessionRepository extends JpaRepository<StudySession, Long> {
    @Query("SELECT COUNT(sj) FROM StudyJournal sj WHERE sj.studySession.id = :sessionId")
    Integer countSubmittedBySessionId(@Param("sessionId") Long sessionId);


    @Query("SELECT COUNT(ss) FROM StudySession ss WHERE ss.study.id = :studyId")
    Long countByStudyId(Long studyId);

    // 스터디 ID로 세션 목록 조회 (회차 순으로 정렬)
    List<StudySession> findByStudyIdOrderByNumberAsc(Long studyId);
}
