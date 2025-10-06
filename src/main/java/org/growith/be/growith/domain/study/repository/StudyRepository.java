package org.growith.be.growith.domain.study.repository;

import org.growith.be.growith.domain.study.dto.StudyCardDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;
import org.growith.be.growith.domain.study.entity.Study;

public interface StudyRepository extends JpaRepository<Study, Long>, JpaSpecificationExecutor<Study> {
    @Query("""
    SELECT new org.growith.be.growith.domain.study.dto.StudyCardDto(
        s.id,
        s.studyStatus,
        s.title,
        u.userId,
        s.scrapCount,
        s.format,
        sf.name,
        COALESCE(CAST(COLLECT(DISTINCT st.styleName) AS list), NULL)
    )
    FROM Study s
    JOIN s.user u
    JOIN s.studyField sf
    LEFT JOIN s.studyStyles ss2
    LEFT JOIN ss2.style st
    GROUP BY s.id, s.studyStatus, s.title, u.userId, s.format, sf.name, s.scrapCount
    ORDER BY s.scrapCount DESC, s.title ASC
""")
    List<StudyCardDto> findPopularStudies(Pageable pageable);


    @Query("""
SELECT new org.growith.be.growith.domain.study.dto.StudyCardDto(
    s.id,
    s.studyStatus,
    s.title,
    u.userId,
    s.scrapCount,
    s.format,
    sf.name,
    COALESCE(CAST(COLLECT(DISTINCT st.styleName) AS list), NULL)
)
FROM Study s
JOIN s.user u
JOIN s.studyField sf
LEFT JOIN s.studyStyles ss2
LEFT JOIN ss2.style st
WHERE s.createdAt >= :oneMonthAgo
GROUP BY s.id, s.studyStatus, s.title, u.userId, s.format, sf.name, s.createdAt, s.scrapCount
ORDER BY s.createdAt DESC, s.title ASC
""")
    List<StudyCardDto> findNewStudies(@Param("oneMonthAgo") LocalDateTime oneMonthAgo, Pageable pageable);
}
