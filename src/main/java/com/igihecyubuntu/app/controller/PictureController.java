package com.igihecyubuntu.app.controller;

import com.igihecyubuntu.app.entity.Picture;
import com.igihecyubuntu.app.dto.projection.PictureProjection;
import com.igihecyubuntu.app.service.PictureService;
import com.igihecyubuntu.app.exception.BadRequestException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/picture")
@Tag(name = "Picture Management", description = "Operations for managing pictures")
public class PictureController {

    @Autowired
    private PictureService pictureService;

    @GetMapping
    @Operation(summary = "Get all pictures", description = "Retrieve all pictures from the database")
    public ResponseEntity<List<PictureProjection>> getAllPictures() {
        try {
            List<PictureProjection> pictures = pictureService.getAllPictures();
            return ResponseEntity.ok(pictures);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get picture by ID", description = "Retrieve a specific picture by its ID")
    public ResponseEntity<PictureProjection> getPictureById(@PathVariable Long id) {
        try {
            Optional<PictureProjection> picture = pictureService.getPictureById(id);
            return picture.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    @Operation(summary = "Create picture", description = "Create a new picture record")
    public ResponseEntity<Picture> createPicture(@RequestBody Picture pictureData) {
        try {
            Picture newPicture = pictureService.createPicture(pictureData);
            return ResponseEntity.ok(newPicture);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update picture", description = "Update picture information")
    public ResponseEntity<Picture> updatePicture(
            @PathVariable Long id,
            @RequestBody Picture updateData) {
        try {
            Picture updatedPicture = pictureService.updatePicture(id, updateData);
            return ResponseEntity.ok(updatedPicture);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete picture", description = "Delete a picture by its ID")
    public ResponseEntity<Map<String, String>> deletePicture(@PathVariable Long id) {
        try {
            pictureService.deletePicture(id);
            return ResponseEntity.ok(Map.of("message", "Picture deleted successfully"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get pictures by type", description = "Retrieve pictures filtered by type")
    public ResponseEntity<List<PictureProjection>> getPicturesByType(@PathVariable String type) {
        try {
            List<PictureProjection> pictures = pictureService.getPicturesByType(type);
            return ResponseEntity.ok(pictures);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/user/{doneBy}")
    @Operation(summary = "Get pictures by user", description = "Retrieve pictures created by a specific user")
    public ResponseEntity<List<PictureProjection>> getPicturesByUser(@PathVariable Long doneBy) {
        try {
            List<PictureProjection> pictures = pictureService.getPicturesByUser(doneBy);
            return ResponseEntity.ok(pictures);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/stats/types")
    @Operation(summary = "Get picture counts by type", description = "Retrieve picture counts grouped by type")
    public ResponseEntity<List<Object[]>> getPictureCountsByType() {
        try {
            List<Object[]> counts = pictureService.getPictureCountsByType();
            return ResponseEntity.ok(counts);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/stats/uploads")
    @Operation(summary = "Get picture uploads by date", description = "Retrieve picture upload statistics by date")
    public ResponseEntity<List<Object[]>> getPictureUploadsByDate() {
        try {
            List<Object[]> uploads = pictureService.getPictureUploadsByDate();
            return ResponseEntity.ok(uploads);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}