package com.igihecyubuntu.app.service;

import com.igihecyubuntu.app.entity.Blog;
import com.igihecyubuntu.app.dto.projection.BlogProjection;
import com.igihecyubuntu.app.repository.BlogRepository;
import com.igihecyubuntu.app.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BlogService {

    private final BlogRepository blogRepository;

    public List<BlogProjection> getAllBlogs() {
        return blogRepository.findAllProjectedBy();
    }

    public Optional<BlogProjection> getBlogById(Long id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Invalid blog ID");
        }
        return blogRepository.findProjectedById(id);
    }

    public List<BlogProjection> getBlogsByStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new BadRequestException("Status cannot be empty");
        }
        return blogRepository.findByStatusOrderByIdDesc(status);
    }

    public Blog createBlog(Blog blog) {
        validateBlog(blog);
        return blogRepository.save(blog);
    }

    public Blog updateBlog(Long id, Blog blog) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Invalid blog ID");
        }
        
        Blog existingBlog = blogRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Blog not found"));
        
        validateBlog(blog);
        
        existingBlog.setTitle(blog.getTitle());
        existingBlog.setContent(blog.getContent());
        existingBlog.setStatus(blog.getStatus());
        
        return blogRepository.save(existingBlog);
    }

    public void deleteBlog(Long id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Invalid blog ID");
        }
        
        if (!blogRepository.existsById(id)) {
            throw new BadRequestException("Blog not found");
        }
        
        blogRepository.deleteById(id);
    }

    // Dashboard methods
    public List<Object[]> getBlogCountsByStatus() {
        return blogRepository.findBlogCountsByStatus();
    }

    public List<Object[]> getBlogStatistics() {
        return blogRepository.findBlogStatistics();
    }

    private void validateBlog(Blog blog) {
        if (blog == null) {
            throw new BadRequestException("Blog data is required");
        }
        if (blog.getTitle() == null || blog.getTitle().trim().isEmpty()) {
            throw new BadRequestException("Blog title is required");
        }
        if (blog.getContent() == null || blog.getContent().trim().isEmpty()) {
            throw new BadRequestException("Blog content is required");
        }
        if (blog.getStatus() == null || blog.getStatus().trim().isEmpty()) {
            throw new BadRequestException("Blog status is required");
        }
    }
}