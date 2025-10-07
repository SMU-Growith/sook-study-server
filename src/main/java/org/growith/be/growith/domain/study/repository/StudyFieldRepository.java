package org.growith.be.growith.domain.study.repository;

import org.growith.be.growith.domain.study.entity.StudyField;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudyFieldRepository extends JpaRepository<StudyField, Long> {
    Optional<StudyField> findByName(String name);
}
