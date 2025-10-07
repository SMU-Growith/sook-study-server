package org.growith.be.growith.domain.study.repository;

import java.util.List;
import org.growith.be.growith.domain.study.entity.Rule;
import org.growith.be.growith.domain.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuleRepository extends JpaRepository<Rule, Long> {
    List<Rule> findByStudy(Study study);
    void deleteByStudy(Study study);
}
