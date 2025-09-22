package com.igihecyubuntu.app.service;

import com.igihecyubuntu.app.entity.Comment;
import com.igihecyubuntu.app.dto.projection.CommentProjection;
import com.igihecyubuntu.app.repository.CommentRepository;
import com.igihecyubuntu.app.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;

    public List<CommentProjection> getAllComments() {
        return commentRepository.findAllProjectedBy();
    }

    public Optional<CommentProjection> getCommentById(Long id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Invalid comment ID");
        }
        return commentRepository.findProjectedById(id);
    }

    public List<CommentProjection> getCommentsByUser(Long doneBy) {
        if (doneBy == null || doneBy <= 0) {
            throw new BadRequestException("Invalid user ID");
        }
        return commentRepository.findByDoneByOrderByIdDesc(doneBy);
    }

    public Comment createComment(Comment comment) {
        validateComment(comment);
        return commentRepository.save(comment);
    }

    public Comment updateComment(Long id, Comment comment) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Invalid comment ID");
        }
        
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Comment not found"));
        
        validateComment(comment);
        
        existingComment.setTitle(comment.getTitle());
        existingComment.setContent(comment.getContent());
        existingComment.setDoneBy(comment.getDoneBy());
        
        return commentRepository.save(existingComment);
    }

    public void deleteComment(Long id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Invalid comment ID");
        }
        
        if (!commentRepository.existsById(id)) {
            throw new BadRequestException("Comment not found");
        }
        
        commentRepository.deleteById(id);
    }

    // Dashboard methods
    public List<Object[]> getCommentCountsByUser() {
        return commentRepository.findCommentCountsByUser();
    }

    public List<Object[]> getCommentCountsByDate() {
        return commentRepository.findCommentCountsByDate();
    }

    private void validateComment(Comment comment) {
        if (comment == null) {
            throw new BadRequestException("Comment data is required");
        }
        if (comment.getTitle() == null || comment.getTitle().trim().isEmpty()) {
            throw new BadRequestException("Comment title is required");
        }
        if (comment.getContent() == null || comment.getContent().trim().isEmpty()) {
            throw new BadRequestException("Comment content is required");
        }
        if (comment.getDoneBy() == null || comment.getDoneBy() <= 0) {
            throw new BadRequestException("Valid user ID is required");
        }
    }
}