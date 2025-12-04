package org.growith.be.growith.domain.comment.repository;

import org.growith.be.growith.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
