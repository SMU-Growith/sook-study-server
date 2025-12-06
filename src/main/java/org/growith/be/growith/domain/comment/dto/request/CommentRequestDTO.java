package org.growith.be.growith.domain.comment.dto.request;

public record CommentRequestDTO() {

    public record CreateComment(
        String content
    ){}
}
