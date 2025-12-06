package org.growith.be.growith.domain.comment.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

public record CommentResponseDTO() {

    @Builder
    public record CommentDetail(
            String nickname,
            String content,
            LocalDateTime createdAt
    ){ }
}
