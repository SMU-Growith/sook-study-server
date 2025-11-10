package org.growith.be.growith.domain.study.repository;

import org.growith.be.growith.domain.study.entity.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
public interface StudySessionRepository extends JpaRepository<StudySession, Long> {
    @Query("SELECT COUNT(us) FROM UserStudy us " +
            "JOIN StudySession ss ON ss.study.id = us.study.id " +
            "WHERE ss.id = :sessionId AND us.isSubmitted = true")
    Integer countSubmittedBySessionId(@Param("sessionId") Long sessionId);
}