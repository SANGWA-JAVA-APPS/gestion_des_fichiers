package com.igihecyubuntu.app.controller;

import com.igihecyubuntu.app.service.*;
import com.igihecyubuntu.app.exception.BadRequestException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard Controller", description = "APIs for dashboard statistics and analytics")
public class DashboardController {

    private final BlogService blogService;
    private final PostService postService;
    private final LikesService likesService;
    private final CommentService commentService;
    private final CategoryService categoryService;
    private final PictureService pictureService;

    @GetMapping("/stats")
    @Operation(summary = "Get dashboard statistics", description = "Retrieve comprehensive dashboard statistics")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved dashboard statistics"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "400", description = "Invalid inputs")
    })
    public ResponseEntity<Map<String, Object>> getDashboardStatistics() {
        try {
            Map<String, Object> dashboardData = new HashMap<>();
            
            // Blog statistics
            Map<String, Object> blogStats = new HashMap<>();
            blogStats.put("countsByStatus", blogService.getBlogCountsByStatus());
            blogStats.put("statistics", blogService.getBlogStatistics());
            dashboardData.put("blogs", blogStats);
            
            // Post statistics
            Map<String, Object> postStats = new HashMap<>();
            postStats.put("countsByDate", postService.getPostCountsByDate());
            postStats.put("countsByBlog", postService.getPostCountsByBlog());
            postStats.put("countsByUser", postService.getPostCountsByUser());
            dashboardData.put("posts", postStats);
            
            // Likes statistics
            Map<String, Object> likesStats = new HashMap<>();
            likesStats.put("countsByPost", likesService.getLikesCountsByPost());
            likesStats.put("countsByDate", likesService.getLikesCountsByDate());
            likesStats.put("countsByUser", likesService.getLikesCountsByUser());
            dashboardData.put("likes", likesStats);
            
            // Comment statistics
            Map<String, Object> commentStats = new HashMap<>();
            commentStats.put("countsByUser", commentService.getCommentCountsByUser());
            commentStats.put("countsByDate", commentService.getCommentCountsByDate());
            dashboardData.put("comments", commentStats);
            
            // Category statistics
            Map<String, Object> categoryStats = new HashMap<>();
            categoryStats.put("usageStatistics", categoryService.getCategoryUsageStatistics());
            categoryStats.put("countsByUser", categoryService.getCategoryCountsByUser());
            dashboardData.put("categories", categoryStats);
            
            // Picture statistics
            Map<String, Object> pictureStats = new HashMap<>();
            pictureStats.put("countsByType", pictureService.getPictureCountsByType());
            pictureStats.put("uploadsByDate", pictureService.getPictureUploadsByDate());
            dashboardData.put("pictures", pictureStats);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Successfully retrieved dashboard statistics");
            response.put("data", dashboardData);
            response.put("status", 200);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new BadRequestException("Failed to retrieve dashboard statistics");
        }
    }

    @GetMapping("/summary")
    @Operation(summary = "Get dashboard summary", description = "Retrieve summarized dashboard metrics")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved dashboard summary"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "400", description = "Invalid inputs")
    })
    public ResponseEntity<Map<String, Object>> getDashboardSummary() {
        try {
            Map<String, Object> summary = new HashMap<>();
            
            // You can add summary calculations here based on the statistics
            summary.put("totalBlogs", blogService.getBlogCountsByStatus());
            summary.put("recentPosts", postService.getPostCountsByDate());
            summary.put("topLikedPosts", likesService.getLikesCountsByPost());
            summary.put("activeCategories", categoryService.getCategoryUsageStatistics());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Successfully retrieved dashboard summary");
            response.put("data", summary);
            response.put("status", 200);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new BadRequestException("Failed to retrieve dashboard summary");
        }
    }
}