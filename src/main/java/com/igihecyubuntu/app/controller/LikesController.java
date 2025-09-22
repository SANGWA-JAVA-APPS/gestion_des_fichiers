package com.igihecyubuntu.app.controller;

import com.igihecyubuntu.app.entity.Likes;
import com.igihecyubuntu.app.dto.projection.LikesProjection;
import com.igihecyubuntu.app.service.LikesService;
import com.igihecyubuntu.app.exception.BadRequestException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
@Tag(name = "Likes Controller", description = "APIs for managing like operations")
public class LikesController {

    private final LikesService likesService;

    @GetMapping
    @Operation(summary = "Get all likes", description = "Retrieve a list of all likes in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved likes"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "400", description = "Invalid inputs")
    })
    public ResponseEntity<Map<String, Object>> getAllLikes() {
        try {
            List<LikesProjection> likes = likesService.getAllLikes();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Successfully retrieved likes");
            response.put("data", likes);
            response.put("status", 200);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new BadRequestException("Failed to retrieve likes");
        }
    }

    @GetMapping("/post/{postId}")
    @Operation(summary = "Get likes by post", description = "Retrieve likes for a specific post")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved likes"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "400", description = "Invalid inputs")
    })
    public ResponseEntity<Map<String, Object>> getLikesByPost(@PathVariable Long postId) {
        try {
            List<LikesProjection> likes = likesService.getLikesByPost(postId);
            long likeCount = likesService.getLikesCountByPost(postId);
            
            Map<String, Object> data = new HashMap<>();
            data.put("likes", likes);
            data.put("totalCount", likeCount);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Successfully retrieved likes");
            response.put("data", data);
            response.put("status", 200);
            
            return ResponseEntity.ok(response);
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("Failed to retrieve likes");
        }
    }

    @PostMapping("/toggle")
    @Operation(summary = "Toggle like", description = "Toggle like status for a post by a user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved likes"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "400", description = "Invalid inputs")
    })
    public ResponseEntity<Map<String, Object>> toggleLike(@RequestParam Long postId, @RequestParam Long doneBy) {
        try {
            Likes result = likesService.toggleLike(postId, doneBy);
            long likeCount = likesService.getLikesCountByPost(postId);
            
            Map<String, Object> data = new HashMap<>();
            data.put("liked", result != null);
            data.put("totalCount", likeCount);
            if (result != null) {
                data.put("like", result);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Successfully retrieved likes");
            response.put("data", data);
            response.put("status", 200);
            
            return ResponseEntity.ok(response);
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("Failed to toggle like");
        }
    }

    @PostMapping
    @Operation(summary = "Create like", description = "Create a new like for a post")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved likes"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "400", description = "Invalid inputs")
    })
    public ResponseEntity<Map<String, Object>> createLike(@RequestBody Likes like) {
        try {
            Likes createdLike = likesService.createLike(like);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Successfully retrieved likes");
            response.put("data", createdLike);
            response.put("status", 200);
            
            return ResponseEntity.ok(response);
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("Failed to create like");
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete like", description = "Delete a like by its unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved likes"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "400", description = "Invalid inputs")
    })
    public ResponseEntity<Map<String, Object>> deleteLike(@PathVariable Long id) {
        try {
            likesService.deleteLike(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Successfully retrieved likes");
            response.put("status", 200);
            
            return ResponseEntity.ok(response);
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("Failed to delete like");
        }
    }

    @GetMapping("/dashboard/stats")
    @Operation(summary = "Get likes statistics", description = "Retrieve likes statistics for dashboard")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved likes"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "400", description = "Invalid inputs")
    })
    public ResponseEntity<Map<String, Object>> getLikesStatistics() {
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("countsByPost", likesService.getLikesCountsByPost());
            stats.put("countsByDate", likesService.getLikesCountsByDate());
            stats.put("countsByUser", likesService.getLikesCountsByUser());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Successfully retrieved likes");
            response.put("data", stats);
            response.put("status", 200);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new BadRequestException("Failed to retrieve likes statistics");
        }
    }
}