package com.igihecyubuntu.app.controller;

import com.igihecyubuntu.app.entity.Blog;
import com.igihecyubuntu.app.dto.projection.BlogProjection;
import com.igihecyubuntu.app.service.BlogService;
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
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
@Tag(name = "Blog Controller", description = "APIs for managing blog operations")
public class BlogController {

    private final BlogService blogService;

    @GetMapping
    @Operation(summary = "Get all blogs", description = "Retrieve a list of all blogs in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved blogs"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "400", description = "Invalid inputs")
    })
    public ResponseEntity<Map<String, Object>> getAllBlogs() {
        try {
            List<BlogProjection> blogs = blogService.getAllBlogs();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Successfully retrieved blogs");
            response.put("data", blogs);
            response.put("status", 200);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new BadRequestException("Failed to retrieve blogs");
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get blog by ID", description = "Retrieve a specific blog by its unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved blog"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "400", description = "Invalid inputs")
    })
    public ResponseEntity<Map<String, Object>> getBlogById(@PathVariable Long id) {
        try {
            Optional<BlogProjection> blog = blogService.getBlogById(id);
            Map<String, Object> response = new HashMap<>();
            
            if (blog.isPresent()) {
                response.put("success", true);
                response.put("message", "Successfully retrieved blog");
                response.put("data", blog.get());
                response.put("status", 200);
                
                return ResponseEntity.ok(response);
            } else {
                throw new BadRequestException("Blog not found");
            }
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("Failed to retrieve blog");
        }
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get blogs by status", description = "Retrieve blogs filtered by their status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved blogs"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "400", description = "Invalid inputs")
    })
    public ResponseEntity<Map<String, Object>> getBlogsByStatus(@PathVariable String status) {
        try {
            List<BlogProjection> blogs = blogService.getBlogsByStatus(status);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Successfully retrieved blogs");
            response.put("data", blogs);
            response.put("status", 200);
            
            return ResponseEntity.ok(response);
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("Failed to retrieve blogs");
        }
    }

    @PostMapping
    @Operation(summary = "Create new blog", description = "Create a new blog entry in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved blog"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "400", description = "Invalid inputs")
    })
    public ResponseEntity<Map<String, Object>> createBlog(@RequestBody Blog blog) {
        try {
            Blog createdBlog = blogService.createBlog(blog);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Successfully retrieved blog");
            response.put("data", createdBlog);
            response.put("status", 200);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("Failed to create blog");
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update blog", description = "Update an existing blog by its unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved blog"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "400", description = "Invalid inputs")
    })
    public ResponseEntity<Map<String, Object>> updateBlog(@PathVariable Long id, @RequestBody Blog blog) {
        try {
            Blog updatedBlog = blogService.updateBlog(id, blog);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Successfully retrieved blog");
            response.put("data", updatedBlog);
            response.put("status", 200);
            
            return ResponseEntity.ok(response);
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("Failed to update blog");
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete blog", description = "Delete a blog by its unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved blog"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "400", description = "Invalid inputs")
    })
    public ResponseEntity<Map<String, Object>> deleteBlog(@PathVariable Long id) {
        try {
            blogService.deleteBlog(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Successfully retrieved blog");
            response.put("status", 200);
            
            return ResponseEntity.ok(response);
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("Failed to delete blog");
        }
    }

    @GetMapping("/dashboard/stats")
    @Operation(summary = "Get blog statistics", description = "Retrieve blog statistics for dashboard")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved blog"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "400", description = "Invalid inputs")
    })
    public ResponseEntity<Map<String, Object>> getBlogStatistics() {
        try {
            List<Object[]> stats = blogService.getBlogStatistics();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Successfully retrieved blog");
            response.put("data", stats);
            response.put("status", 200);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new BadRequestException("Failed to retrieve blog statistics");
        }
    }
}