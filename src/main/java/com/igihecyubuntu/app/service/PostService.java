package com.igihecyubuntu.app.service;

import com.igihecyubuntu.app.entity.Post;
import com.igihecyubuntu.app.dto.projection.PostProjection;
import com.igihecyubuntu.app.repository.PostRepository;
import com.igihecyubuntu.app.repository.BlogRepository;
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
public class PostService {

    private final PostRepository postRepository;
    private final BlogRepository blogRepository;

    public List<PostProjection> getAllPosts() {
        return postRepository.findAllProjectedBy();
    }

    public Optional<PostProjection> getPostById(Long id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Invalid post ID");
        }
        return postRepository.findProjectedById(id);
    }

    public List<PostProjection> getPostsByBlog(Long blogId) {
        if (blogId == null || blogId <= 0) {
            throw new BadRequestException("Invalid blog ID");
        }
        return postRepository.findByBlogIdOrderByDateTimeDesc(blogId);
    }

    public List<PostProjection> getPostsByUser(Long doneBy) {
        if (doneBy == null || doneBy <= 0) {
            throw new BadRequestException("Invalid user ID");
        }
        return postRepository.findByDoneByOrderByDateTimeDesc(doneBy);
    }

    public Post createPost(Post post) {
        validatePost(post);
        post.setDateTime(LocalDateTime.now());
        return postRepository.save(post);
    }

    public Post updatePost(Long id, Post post) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Invalid post ID");
        }
        
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Post not found"));
        
        validatePost(post);
        
        existingPost.setBlogId(post.getBlogId());
        existingPost.setDoneBy(post.getDoneBy());
        
        return postRepository.save(existingPost);
    }

    public void deletePost(Long id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Invalid post ID");
        }
        
        if (!postRepository.existsById(id)) {
            throw new BadRequestException("Post not found");
        }
        
        postRepository.deleteById(id);
    }

    // Dashboard methods
    public List<Object[]> getPostCountsByDate() {
        return postRepository.findPostCountsByDate();
    }

    public List<Object[]> getPostCountsByBlog() {
        return postRepository.findPostCountsByBlog();
    }

    public List<Object[]> getPostCountsByUser() {
        return postRepository.findPostCountsByUser();
    }

    private void validatePost(Post post) {
        if (post == null) {
            throw new BadRequestException("Post data is required");
        }
        if (post.getBlogId() == null || post.getBlogId() <= 0) {
            throw new BadRequestException("Valid blog ID is required");
        }
        if (post.getDoneBy() == null || post.getDoneBy() <= 0) {
            throw new BadRequestException("Valid user ID is required");
        }
        
        // Check if blog exists
        if (!blogRepository.existsById(post.getBlogId())) {
            throw new BadRequestException("Blog not found");
        }
    }
}