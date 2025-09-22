package com.igihecyubuntu.app.service;

import com.igihecyubuntu.app.entity.Likes;
import com.igihecyubuntu.app.dto.projection.LikesProjection;
import com.igihecyubuntu.app.repository.LikesRepository;
import com.igihecyubuntu.app.repository.PostRepository;
import com.igihecyubuntu.app.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikesService {

    private final LikesRepository likesRepository;
    private final PostRepository postRepository;

    public List<LikesProjection> getAllLikes() {
        return likesRepository.findAllProjectedBy();
    }

    public List<LikesProjection> getLikesByPost(Long postId) {
        if (postId == null || postId <= 0) {
            throw new BadRequestException("Invalid post ID");
        }
        return likesRepository.findByPostIdOrderByDateTimeDesc(postId);
    }

    public List<LikesProjection> getLikesByUser(Long doneBy) {
        if (doneBy == null || doneBy <= 0) {
            throw new BadRequestException("Invalid user ID");
        }
        return likesRepository.findByDoneByOrderByDateTimeDesc(doneBy);
    }

    public long getLikesCountByPost(Long postId) {
        if (postId == null || postId <= 0) {
            throw new BadRequestException("Invalid post ID");
        }
        return likesRepository.countByPostId(postId);
    }

    public Likes toggleLike(Long postId, Long doneBy) {
        validateLikeData(postId, doneBy);
        
        Optional<Likes> existingLike = likesRepository.findByPostIdAndDoneBy(postId, doneBy);
        
        if (existingLike.isPresent()) {
            // Unlike - remove the like
            likesRepository.delete(existingLike.get());
            return null;
        } else {
            // Like - create new like
            Likes newLike = new Likes();
            newLike.setPostId(postId);
            newLike.setDoneBy(doneBy);
            newLike.setDateTime(LocalDateTime.now());
            return likesRepository.save(newLike);
        }
    }

    public Likes createLike(Likes like) {
        validateLike(like);
        like.setDateTime(LocalDateTime.now());
        
        // Check if user already liked this post
        Optional<Likes> existingLike = likesRepository.findByPostIdAndDoneBy(like.getPostId(), like.getDoneBy());
        if (existingLike.isPresent()) {
            throw new BadRequestException("User already liked this post");
        }
        
        return likesRepository.save(like);
    }

    public void deleteLike(Long id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Invalid like ID");
        }
        
        if (!likesRepository.existsById(id)) {
            throw new BadRequestException("Like not found");
        }
        
        likesRepository.deleteById(id);
    }

    // Dashboard methods
    public List<Object[]> getLikesCountsByPost() {
        return likesRepository.findLikesCountsByPost();
    }

    public List<Object[]> getLikesCountsByDate() {
        return likesRepository.findLikesCountsByDate();
    }

    public List<Object[]> getLikesCountsByUser() {
        return likesRepository.findLikesCountsByUser();
    }

    private void validateLike(Likes like) {
        if (like == null) {
            throw new BadRequestException("Like data is required");
        }
        validateLikeData(like.getPostId(), like.getDoneBy());
    }

    private void validateLikeData(Long postId, Long doneBy) {
        if (postId == null || postId <= 0) {
            throw new BadRequestException("Valid post ID is required");
        }
        if (doneBy == null || doneBy <= 0) {
            throw new BadRequestException("Valid user ID is required");
        }
        
        // Check if post exists
        if (!postRepository.existsById(postId)) {
            throw new BadRequestException("Post not found");
        }
    }
}