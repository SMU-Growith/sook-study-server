package org.growith.be.growith.domain.comment.service;

import org.growith.be.growith.domain.comment.dto.request.CommentRequestDTO;
import org.growith.be.growith.domain.comment.entity.Comment;

public interface CommentService {
    Comment createComment(CommentRequestDTO.CreateComment request, Long studyId, Long userId);

    void deleteComment(Long commentId);

}
