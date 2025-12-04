package org.growith.be.growith.domain.comment.converter;

import org.growith.be.growith.domain.comment.dto.response.CommentResponseDTO;
import org.growith.be.growith.domain.comment.entity.Comment;
import org.growith.be.growith.domain.study.entity.Study;
import org.growith.be.growith.domain.user.entity.User;

public class CommentConverter {

    // Comment 엔티티 생성
    public static Comment toCommentEntity(User user, Study study, String content){
        return Comment.builder()
                .user(user)
                .study(study)
                .content(content)
                .build();
    }

    // CommentResponseDTO.CommentDetail
    public static CommentResponseDTO.CommentDetail toCommentDetail(Comment comment){
        return CommentResponseDTO.CommentDetail.builder()
                .nickname(comment.getUser().getNickName())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
