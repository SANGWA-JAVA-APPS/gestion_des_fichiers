package com.bar.gestiondesfichier.location.controller;

import com.bar.gestiondesfichier.location.model.LocationEntity;
import com.bar.gestiondesfichier.location.model.Country;
import com.bar.gestiondesfichier.location.repository.LocationEntityRepository;
import com.bar.gestiondesfichier.location.repository.CountryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/location/entities")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8081"})
@Tag(name = "Location Entity Management", description = "Location Entity CRUD operations")
public class LocationEntityController {

    private static final Logger log = LoggerFactory.getLogger(LocationEntityController.class);
    private final LocationEntityRepository locationEntityRepository;
    private final CountryRepository countryRepository;

    // Explicit constructor for dependency injection
    public LocationEntityController(LocationEntityRepository locationEntityRepository, CountryRepository countryRepository) {
        this.locationEntityRepository = locationEntityRepository;
        this.countryRepository = countryRepository;
    }

    @GetMapping
    @Operation(summary = "Get all location entities", description = "Retrieve all active location entities")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Location entities retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<LocationEntityDTO>> getAllLocationEntities() {
        try {
            List<LocationEntity> entities = locationEntityRepository.findByActiveTrue();
            List<LocationEntityDTO> entityDTOs = entities.stream()
                .map(this::convertToDTO)
                .toList();
            return ResponseEntity.ok(entityDTOs);
        } catch (Exception e) {
            log.error("Error retrieving location entities", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get location entity by ID", description = "Retrieve a specific location entity by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Location entity retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Location entity not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<LocationEntityDTO> getLocationEntityById(@PathVariable Long id) {
        try {
            Optional<LocationEntity> entity = locationEntityRepository.findByIdAndActiveTrue(id);
            return entity.map(e -> ResponseEntity.ok(convertToDTO(e)))
                        .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error retrieving location entity with id: " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/country/{countryId}")
    @Operation(summary = "Get location entities by country", description = "Retrieve location entities for a specific country")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Location entities retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<LocationEntityDTO>> getLocationEntitiesByCountry(@PathVariable Long countryId) {
        try {
            List<LocationEntity> entities = locationEntityRepository.findByCountryIdAndActiveTrue(countryId);
            List<LocationEntityDTO> entityDTOs = entities.stream()
                .map(this::convertToDTO)
                .toList();
            return ResponseEntity.ok(entityDTOs);
        } catch (Exception e) {
            log.error("Error retrieving location entities for country: " + countryId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    @Operation(summary = "Create location entity", description = "Create a new location entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Location entity created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<LocationEntityDTO> createLocationEntity(@RequestBody LocationEntityRequest entityRequest) {
        try {
            // Find the country
            Optional<Country> country = countryRepository.findByIdAndActiveTrue(entityRequest.getCountryId());
            if (!country.isPresent()) {
                return ResponseEntity.badRequest().build();
            }

            LocationEntity entity = new LocationEntity();
            entity.setName(entityRequest.getName());
            entity.setDescription(entityRequest.getDescription());
            entity.setCountry(country.get());
            entity.setActive(true);

            LocationEntity savedEntity = locationEntityRepository.save(entity);
            return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedEntity));
        } catch (Exception e) {
            log.error("Error creating location entity", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public Long getCountryId() { return countryId; }
        public void setCountryId(Long countryId) { this.countryId = countryId; }
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
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public Long getCountryId() { return countryId; }
        public void setCountryId(Long countryId) { this.countryId = countryId; }
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