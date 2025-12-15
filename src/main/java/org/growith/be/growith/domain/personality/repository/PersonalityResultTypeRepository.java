package org.growith.be.growith.domain.personality.repository;

import org.growith.be.growith.domain.personality.entity.PersonalityResultType;
import org.growith.be.growith.domain.personality.entity.enums.ResultTypeCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonalityResultTypeRepository extends JpaRepository<PersonalityResultType, Long> {
    
    Optional<PersonalityResultType> findByTypeCode(ResultTypeCode typeCode);
}
