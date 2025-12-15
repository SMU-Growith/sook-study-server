package org.growith.be.growith.domain.personality.repository;

import org.growith.be.growith.domain.personality.entity.PersonalityQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PersonalityQuestionRepository extends JpaRepository<PersonalityQuestion, Long> {
    
    @Query("SELECT DISTINCT q FROM PersonalityQuestion q LEFT JOIN FETCH q.options ORDER BY q.questionNumber")
    List<PersonalityQuestion> findAllWithOptions();
}
