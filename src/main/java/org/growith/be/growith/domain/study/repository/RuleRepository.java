package org.growith.be.growith.domain.study.repository;

import java.util.List;
import org.growith.be.growith.domain.study.entity.Rule;
import org.growith.be.growith.domain.study.entity.Study;
import org.growith.be.growith.domain.study.entity.enums.RuleCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RuleRepository extends JpaRepository<Rule, Long> {
    List<Rule> findByStudy(Study study);
    void deleteByStudy(Study study);

    List<Rule> findByStudy_Id(Long studyId);

    Optional<Rule> findByStudy_IdAndRuleCategory(Long studyId, RuleCategory ruleCategory);
}
