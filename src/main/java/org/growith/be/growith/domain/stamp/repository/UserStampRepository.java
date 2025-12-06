package org.growith.be.growith.domain.stamp.repository;

import org.growith.be.growith.domain.stamp.entity.UserStamp;
import org.growith.be.growith.domain.stamp.entity.enums.StampType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserStampRepository extends JpaRepository<UserStamp, Long> {
    
    @Query("SELECT us FROM UserStamp us WHERE us.user.id = :userId")
    List<UserStamp> findAllByUserId(@Param("userId") Long userId);

    Optional<UserStamp> findByUserIdAndStampType(Long userId, StampType stampType);
}
