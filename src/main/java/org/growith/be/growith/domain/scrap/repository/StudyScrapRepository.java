package org.growith.be.growith.domain.scrap.repository;

import org.growith.be.growith.domain.scrap.entity.StudyScrap;
import org.growith.be.growith.domain.study.entity.Study;
import org.growith.be.growith.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudyScrapRepository extends JpaRepository<StudyScrap, Long> {
    Optional<StudyScrap> findByUserAndStudy(User user, Study study);
    Page<StudyScrap> findByUser(User user, Pageable pageable);
    long countByUser(User user);
}
