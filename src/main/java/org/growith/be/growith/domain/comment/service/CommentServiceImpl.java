package org.growith.be.growith.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.comment.converter.CommentConverter;
import org.growith.be.growith.domain.comment.dto.request.CommentRequestDTO;
import org.growith.be.growith.domain.comment.entity.Comment;
import org.growith.be.growith.domain.comment.repository.CommentRepository;
import org.growith.be.growith.domain.study.entity.Study;
import org.growith.be.growith.domain.study.repository.StudyRepository;
import org.growith.be.growith.domain.user.entity.User;
import org.growith.be.growith.domain.user.repository.UserRepository;
import org.growith.be.growith.global.error.code.status.CommentErrorCode;
import org.growith.be.growith.global.error.code.status.StudyErrorCode;
import org.growith.be.growith.global.error.code.status.UserErrorCode;
import org.growith.be.growith.global.error.exception.handler.CommentException;
import org.growith.be.growith.global.error.exception.handler.StudyException;
import org.growith.be.growith.global.error.exception.handler.UserException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final StudyRepository studyRepository;
    private final UserRepository userRepository;

    // 댓글 작성
    public Comment createComment(CommentRequestDTO.CreateComment request, Long studyId, Long userId){
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyException(StudyErrorCode.STUDY_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND));

        Comment commentEntity = CommentConverter.toCommentEntity(user, study, request.content());
        return commentRepository.save(commentEntity);
    }

    // 댓글 삭제
    public void deleteComment(Long commentId){
        commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));
    }


}
