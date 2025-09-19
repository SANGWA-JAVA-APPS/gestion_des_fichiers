package com.bar.gestiondesfichier.location.controller;

import com.bar.gestiondesfichier.common.util.ResponseUtil;
import com.bar.gestiondesfichier.location.model.Country;
import com.bar.gestiondesfichier.location.service.CountryService;
import com.bar.gestiondesfichier.location.repository.CountryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/location/countries")
@Tag(name = "Country Management", description = "Country CRUD operations")
public class CountryController {

    private static final Logger log = LoggerFactory.getLogger(CountryController.class);
    private final CountryService countryService;
    private final CountryRepository countryRepository;

    public CountryController(CountryService countryService, CountryRepository countryRepository) {
        this.countryService = countryService;
        this.countryRepository = countryRepository;
    }

    @GetMapping
    @Operation(summary = "Get all countries", description = "Retrieve paginated list of active countries")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Countries retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Map<String, Object>> getAllCountries(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        try {
            Pageable pageable = ResponseUtil.createPageable(page, size, sort, direction);
            Page<Country> countries = countryRepository.findByActiveTrue(pageable);
            return ResponseUtil.successWithPagination(countries);
        } catch (Exception e) {
            log.error("Error retrieving countries", e);
            return ResponseUtil.badRequest("Failed to retrieve countries: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get country by ID", description = "Retrieve a specific country by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Country retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Country not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<Map<String, Object>> getCountryById(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseUtil.badRequest("Invalid country ID");
            }
            
            Optional<Country> country = countryService.findByIdAndActive(id);
            if (country.isPresent()) {
                return ResponseUtil.success(country.get());
            } else {
                return ResponseUtil.badRequest("Country not found with ID: " + id);
            }
        } catch (Exception e) {
            log.error("Error retrieving country with id: " + id, e);
            return ResponseUtil.badRequest("Failed to retrieve country: " + e.getMessage());
        }
    }

    @PostMapping
    @Operation(summary = "Create country", description = "Create a new country")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Country created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "403", description = "Session expired")
    })
    public ResponseEntity<Map<String, Object>> createCountry(@RequestBody Country countryRequest) {
        try {
            log.info("Creating country: {}", countryRequest.getName());
            
            Country country = countryService.createCountry(
                    countryRequest.getName(),
                    countryRequest.getDescription(),
                    countryRequest.getIsoCode(),
                    countryRequest.getPhoneCode(),
                    countryRequest.getFlagUrl()
            );
            
            return ResponseUtil.success(country, "Country created successfully");
        } catch (IllegalArgumentException e) {
            log.warn("Invalid country data: {}", e.getMessage());
            return ResponseUtil.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("Error creating country", e);
            return ResponseUtil.badRequest("Failed to save country: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update country", description = "Update an existing country")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Country updated successfully"),
        @ApiResponse(responseCode = "404", description = "Country not found"),
        @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<Map<String, Object>> updateCountry(@PathVariable Long id, @RequestBody Country countryRequest) {
        try {
            if (id == null || id <= 0) {
                return ResponseUtil.badRequest("Invalid country ID");
            }
            
            Country country = countryService.updateCountry(
                    id,
                    countryRequest.getName(),
                    countryRequest.getDescription(),
                    countryRequest.getIsoCode(),
                    countryRequest.getPhoneCode(),
                    countryRequest.getFlagUrl()
            );
            
            return ResponseUtil.success(country, "Country updated successfully");
        } catch (IllegalArgumentException e) {
            log.warn("Invalid country data for update: {}", e.getMessage());
            return ResponseUtil.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("Error updating country with id: " + id, e);
            return ResponseUtil.badRequest("Failed to update country: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete country", description = "Soft delete a country")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Country deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Country not found"),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<Map<String, Object>> deleteCountry(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseUtil.badRequest("Invalid country ID");
            }
            
            countryService.softDeleteById(id);
            return ResponseUtil.success(null, "Country deleted successfully");
        } catch (IllegalArgumentException e) {
            log.warn("Country not found for deletion: {}", e.getMessage());
            return ResponseUtil.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("Error deleting country with id: " + id, e);
            return ResponseUtil.badRequest("Failed to delete country: " + e.getMessage());
        }
    }
}
