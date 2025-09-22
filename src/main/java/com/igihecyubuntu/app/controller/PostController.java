package com.igihecyubuntu.app.controller;

import com.igihecyubuntu.app.entity.Post;
import com.igihecyubuntu.app.dto.projection.PostProjection;
import com.igihecyubuntu.app.service.PostService;
import com.igihecyubuntu.app.exception.BadRequestException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name = "Post Controller", description = "APIs for managing post operations")
public class PostController {

    private final PostService postService;

    @GetMapping
    @Operation(summary = "Get all posts", description = "Retrieve a list of all posts in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved posts"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "400", description = "Invalid inputs")
    })
    public ResponseEntity<Map<String, Object>> getAllPosts() {
        try {
            List<PostProjection> posts = postService.getAllPosts();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Successfully retrieved posts");
            response.put("data", posts);
            response.put("status", 200);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new BadRequestException("Failed to retrieve posts");
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get post by ID", description = "Retrieve a specific post by its unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved post"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "400", description = "Invalid inputs")
    })
    public ResponseEntity<Map<String, Object>> getPostById(@PathVariable Long id) {
        try {
            Optional<PostProjection> post = postService.getPostById(id);
            Map<String, Object> response = new HashMap<>();
            
            if (post.isPresent()) {
                response.put("success", true);
                response.put("message", "Successfully retrieved post");
                response.put("data", post.get());
                response.put("status", 200);
                
                return ResponseEntity.ok(response);
            } else {
                throw new BadRequestException("Post not found");
            }
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("Failed to retrieve post");
        }
    }

    @GetMapping("/blog/{blogId}")
    @Operation(summary = "Get posts by blog", description = "Retrieve posts filtered by blog ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved posts"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "400", description = "Invalid inputs")
    })
    public ResponseEntity<Map<String, Object>> getPostsByBlog(@PathVariable Long blogId) {
        try {
            List<PostProjection> posts = postService.getPostsByBlog(blogId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Successfully retrieved posts");
            response.put("data", posts);
            response.put("status", 200);
            
            return ResponseEntity.ok(response);
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("Failed to retrieve posts");
        }
    }

    @PostMapping
    @Operation(summary = "Create new post", description = "Create a new post entry in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved post"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "400", description = "Invalid inputs")
    })
    public ResponseEntity<Map<String, Object>> createPost(@RequestBody Post post) {
        try {
            Post createdPost = postService.createPost(post);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Successfully retrieved post");
            response.put("data", createdPost);
            response.put("status", 200);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("Failed to create post");
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update post", description = "Update an existing post by its unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved post"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "400", description = "Invalid inputs")
    })
    public ResponseEntity<Map<String, Object>> updatePost(@PathVariable Long id, @RequestBody Post post) {
        try {
            Post updatedPost = postService.updatePost(id, post);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Successfully retrieved post");
            response.put("data", updatedPost);
            response.put("status", 200);
            
            return ResponseEntity.ok(response);
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("Failed to update post");
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete post", description = "Delete a post by its unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved post"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "400", description = "Invalid inputs")
    })
    public ResponseEntity<Map<String, Object>> deletePost(@PathVariable Long id) {
        try {
            postService.deletePost(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Successfully retrieved post");
            response.put("status", 200);
            
            return ResponseEntity.ok(response);
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("Failed to delete post");
        }
    }

    @GetMapping("/dashboard/stats")
    @Operation(summary = "Get post statistics", description = "Retrieve post statistics for dashboard")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved post"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "400", description = "Invalid inputs")
    })
    public ResponseEntity<Map<String, Object>> getPostStatistics() {
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("countsByDate", postService.getPostCountsByDate());
            stats.put("countsByBlog", postService.getPostCountsByBlog());
            stats.put("countsByUser", postService.getPostCountsByUser());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Successfully retrieved post");
            response.put("data", stats);
            response.put("status", 200);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new BadRequestException("Failed to retrieve post statistics");
        }
    }
}