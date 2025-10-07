package org.growith.be.growith.domain.study.repository;

import org.growith.be.growith.domain.study.entity.Style;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StyleRepository extends JpaRepository<Style, Long> {
    List<Style> findByStyleNameIn(List<String> styleNames);
}
