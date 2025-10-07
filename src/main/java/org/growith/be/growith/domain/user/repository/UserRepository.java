package org.growith.be.growith.domain.user.repository;

import org.growith.be.growith.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(Long id);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
