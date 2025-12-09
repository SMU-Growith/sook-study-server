package org.growith.be.growith.domain.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.comment.converter.CommentConverter;
import org.growith.be.growith.domain.comment.dto.request.CommentRequestDTO;
import org.growith.be.growith.domain.comment.dto.response.CommentResponseDTO;
import org.growith.be.growith.domain.comment.entity.Comment;
import org.growith.be.growith.domain.comment.service.CommentService;
import org.growith.be.growith.domain.user.entity.User;
import org.growith.be.growith.global.annotation.AuthenticatedUser;
import org.growith.be.growith.global.error.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Tag(name = "댓글 API")
@RequestMapping("/api/v1/studies")
public class CommentController {

    private final CommentService commentService;

    // @Operation(summary = "댓글 생성 API", description = "스터디에 댓글을 생성하는 API")
    // @PostMapping("/{studyId}/comments")
    // public ApiResponse<CommentResponseDTO.CommentDetail> createComment(
    //         @PathVariable("studyId") Long studyId,
    //         @AuthenticatedUser User user,
    //         @RequestBody CommentRequestDTO.CreateComment request
    // ){
    //     Comment comment = commentService.createComment(request, studyId, user.getId());
    //     return ApiResponse.onSuccess(CommentConverter.toCommentDetail(comment));
    // }

    // @Operation(summary = "댓글 삭제 API", description = "스터디에 댓글을 하는 삭제 API")
    // @DeleteMapping("/comments/{commentId}")
    // public ApiResponse<Void> deleteComment(
    //         @PathVariable("commentId") Long commentId
    // ){
    //     commentService.deleteComment(commentId);
    //     return ApiResponse.onSuccess(null);
    // }
}
