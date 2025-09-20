package com.bar.gestiondesfichier.location.controller;

import com.bar.gestiondesfichier.common.util.ResponseUtil;
import com.bar.gestiondesfichier.location.model.LocationEntity;
import com.bar.gestiondesfichier.location.model.Country;
import com.bar.gestiondesfichier.location.repository.LocationEntityRepository;
import com.bar.gestiondesfichier.location.repository.CountryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/location/entities")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8081"})
@Tag(name = "Location Entity Management", description = "Location Entity CRUD operations with pagination")
public class LocationEntityController {

    private static final Logger log = LoggerFactory.getLogger(LocationEntityController.class);
    private final LocationEntityRepository locationEntityRepository;
    private final CountryRepository countryRepository;

    public LocationEntityController(LocationEntityRepository locationEntityRepository, CountryRepository countryRepository) {
        this.locationEntityRepository = locationEntityRepository;
        this.countryRepository = countryRepository;
    }

    @GetMapping
    @Operation(summary = "Get all location entities", description = "Retrieve paginated list of active location entities with default 20 records per page")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Location entities retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "403", description = "Session expired")
    })
    public ResponseEntity<Map<String, Object>> getAllLocationEntities(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "Page size (max 100)") @RequestParam(defaultValue = "20") Integer size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "name") String sort,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String direction) {
        try {
            log.info("Retrieving location entities - page: {}, size: {}, sort: {} {}", page, size, sort, direction);

            Pageable pageable = ResponseUtil.createPageable(page, size, sort, direction);
            Page<LocationEntity> entities = locationEntityRepository.findByActiveTrue(pageable);
            return ResponseUtil.successWithPagination(entities);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid parameters for location entity retrieval: {}", e.getMessage());
            return ResponseUtil.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("Error retrieving location entities", e);
            return ResponseUtil.badRequest("Failed to retrieve location entities: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get location entity by ID", description = "Retrieve a specific location entity by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Location entity retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Location entity not found or invalid ID")
    })
    public ResponseEntity<Map<String, Object>> getLocationEntityById(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseUtil.badRequest("Invalid location entity ID");
            }

            log.info("Retrieving location entity by ID: {}", id);
            Optional<LocationEntity> entity = locationEntityRepository.findByIdAndActiveTrue(id);

            if (entity.isPresent()) {
                return ResponseUtil.success(entity.get(), "Location entity retrieved successfully");
            } else {
                return ResponseUtil.badRequest("Location entity not found with ID: " + id);
            }
        } catch (Exception e) {
            log.error("Error retrieving location entity with ID: {}", id, e);
            return ResponseUtil.badRequest("Failed to retrieve location entity: " + e.getMessage());
        }
    }

    @GetMapping("/country/{countryId}")
    @Operation(summary = "Get location entities by country", description = "Retrieve location entities for a specific country")
    public ResponseEntity<Map<String, Object>> getLocationEntitiesByCountry(
            @PathVariable Long countryId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        try {
            if (countryId == null || countryId <= 0) {
                return ResponseUtil.badRequest("Invalid country ID");
            }

            log.info("Retrieving location entities by country: {}", countryId);
            Pageable pageable = ResponseUtil.createPageable(page, size, sort, direction);
            Page<LocationEntity> entities = locationEntityRepository.findByCountryIdAndActiveTrue(countryId, pageable);

            return ResponseUtil.successWithPagination(entities);
        } catch (Exception e) {
            log.error("Error retrieving location entities for country: {}", countryId, e);
            return ResponseUtil.badRequest("Failed to retrieve location entities: " + e.getMessage());
        }
    }

    @PostMapping
    @Operation(summary = "Create location entity", description = "Create a new location entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Location entity created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "403", description = "Session expired")
    })
    public ResponseEntity<Map<String, Object>> createLocationEntity(@RequestBody LocationEntityRequest entityRequest) {
        try {
            log.info("Creating location entity: {}", entityRequest.getName());

            // Validate input
            if (entityRequest.getName() == null || entityRequest.getName().trim().isEmpty()) {
                return ResponseUtil.badRequest("Location entity name is required");
            }

            if (entityRequest.getCountryId() == null || entityRequest.getCountryId() <= 0) {
                return ResponseUtil.badRequest("Valid country ID is required");
            }

            // Find the country
            Optional<Country> country = countryRepository.findByIdAndActiveTrue(entityRequest.getCountryId());
            if (!country.isPresent()) {
                return ResponseUtil.badRequest("Country not found with ID: " + entityRequest.getCountryId());
            }

            LocationEntity entity = new LocationEntity();
            entity.setName(entityRequest.getName().trim());
            entity.setDescription(entityRequest.getDescription() != null ? entityRequest.getDescription().trim() : null);
            entity.setCountry(country.get());
            entity.setActive(true);

            LocationEntity savedEntity = locationEntityRepository.save(entity);
            return ResponseUtil.success(savedEntity, "Location entity created successfully");
        } catch (Exception e) {
            log.error("Error creating location entity", e);
            return ResponseUtil.badRequest("Failed to create location entity: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update location entity", description = "Update an existing location entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Location entity updated successfully"),
        @ApiResponse(responseCode = "404", description = "Location entity not found"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<LocationEntityDTO> updateLocationEntity(@PathVariable Long id, @RequestBody LocationEntityRequest entityRequest) {
        try {
            Optional<LocationEntity> existingEntity = locationEntityRepository.findByIdAndActiveTrue(id);
            if (!existingEntity.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            LocationEntity entity = existingEntity.get();
            entity.setName(entityRequest.getName());
            entity.setDescription(entityRequest.getDescription());

            if (entityRequest.getCountryId() != null) {
                Optional<Country> country = countryRepository.findByIdAndActiveTrue(entityRequest.getCountryId());
                if (country.isPresent()) {
                    entity.setCountry(country.get());
                }
            }

            LocationEntity savedEntity = locationEntityRepository.save(entity);
            return ResponseEntity.ok(convertToDTO(savedEntity));
        } catch (Exception e) {
            log.error("Error updating location entity with id: " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete location entity", description = "Soft delete a location entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Location entity deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Location entity not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteLocationEntity(@PathVariable Long id) {
        try {
            Optional<LocationEntity> entity = locationEntityRepository.findByIdAndActiveTrue(id);
            if (!entity.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            LocationEntity locationEntity = entity.get();
            locationEntity.setActive(false);
            locationEntityRepository.save(locationEntity);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting location entity with id: " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Simple DTO for simplified frontend requirements
    public static class LocationEntityRequest {

        private String name;
        private String description;
        private Long countryId;

        // Getters and Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Long getCountryId() {
            return countryId;
        }

        public void setCountryId(Long countryId) {
            this.countryId = countryId;
        }
    }

    // Simple DTO for response
    public static class LocationEntityDTO {

        private Long id;
        private String name;
        private Long countryId;

        public LocationEntityDTO(Long id, String name, Long countryId) {
            this.id = id;
            this.name = name;
            this.countryId = countryId;
        }

        // Getters and Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getCountryId() {
            return countryId;
        }

        public void setCountryId(Long countryId) {
            this.countryId = countryId;
        }
    }

    // Conversion method
    private LocationEntityDTO convertToDTO(LocationEntity entity) {
        return new LocationEntityDTO(
                entity.getId(),
                entity.getName(),
                entity.getCountry() != null ? entity.getCountry().getId() : null
        );
    }
}
