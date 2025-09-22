package com.bar.gestiondesfichier.location.service.impl;

import com.bar.gestiondesfichier.location.model.Country;
import com.bar.gestiondesfichier.location.projection.CountryProjection;
import com.bar.gestiondesfichier.location.repository.CountryRepository;
import com.bar.gestiondesfichier.location.service.CountryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CountryServiceImpl implements CountryService {

    private static final Logger log = LoggerFactory.getLogger(CountryServiceImpl.class);
    private final CountryRepository countryRepository;

    // Explicit constructor for dependency injection
    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public List<Country> findAll() {
        log.debug("Finding all countries");
        return countryRepository.findAll();
    }

    @Override
    public List<Country> findAllActive() {
        log.debug("Finding all active countries");
        return countryRepository.findByActiveTrue();
    }

    @Override
    public Optional<Country> findById(Long id) {
        log.debug("Finding country by id: {}", id);
        return countryRepository.findById(id);
    }

    @Override
    public Optional<Country> findByIdAndActive(Long id) {
        log.debug("Finding active country by id: {}", id);
        return countryRepository.findByIdAndActiveTrue(id);
    }

    @Override
    public Optional<Country> findByIsoCode(String isoCode) {
        log.debug("Finding country by ISO code: {}", isoCode);
        return countryRepository.findByIsoCodeAndActiveTrue(isoCode);
    }

    @Override
    public Optional<Country> findByName(String name) {
        log.debug("Finding country by name: {}", name);
        return countryRepository.findByNameIgnoreCaseAndActiveTrue(name);
    }

    @Override
    public List<Country> searchByName(String name) {
        log.debug("Searching countries by name containing: {}", name);
        return countryRepository.findByNameContainingIgnoreCaseAndActiveTrue(name);
    }

    @Override
    public List<CountryProjection> findAllProjections() {
        log.debug("Finding all active country projections");
        return countryRepository.findAllActiveProjections();
    }

    @Override
    public Optional<CountryProjection> findProjectionById(Long id) {
        log.debug("Finding country projection by id: {}", id);
        return countryRepository.findProjectionById(id);
    }

    @Override
    public boolean existsByIsoCode(String isoCode) {
        log.debug("Checking if country exists by ISO code: {}", isoCode);
        return countryRepository.existsByIsoCodeAndActiveTrue(isoCode);
    }

    @Override
    public boolean existsByName(String name) {
        log.debug("Checking if country exists by name: {}", name);
        return countryRepository.existsByNameIgnoreCaseAndActiveTrue(name);
    }

    @Override
    @Transactional
    public Country save(Country country) {
        log.debug("Saving country: {}", country.getName());
        return countryRepository.save(country);
    }

    @Override
    @Transactional
    public Country update(Country country) {
        log.debug("Updating country with id: {}", country.getId());
        return countryRepository.save(country);
    }

    @Override
    @Transactional
    public Country createCountry(String name, String description, String isoCode, String phoneCode, String flagUrl) {
        log.debug("Creating new country: {} with ISO code: {}", name, isoCode);
        
        try {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Country name is required");
            }
            
            if (isoCode != null && !isoCode.trim().isEmpty() && existsByIsoCode(isoCode)) {
                throw new IllegalArgumentException("Country with ISO code '" + isoCode + "' already exists");
            }
            
            if (existsByName(name)) {
                throw new IllegalArgumentException("Country with name '" + name + "' already exists");
            }
            
            // Create country using constructor instead of builder
            Country country = new Country(name.trim(), 
                                         description != null ? description.trim() : null,
                                         isoCode != null ? isoCode.trim().toUpperCase() : null,
                                         phoneCode != null ? phoneCode.trim() : null,
                                         flagUrl != null ? flagUrl.trim() : null);
            
            return countryRepository.save(country);
        } catch (Exception e) {
            log.error("Error creating country: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create country: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Country updateCountry(Long id, String name, String description, String isoCode, String phoneCode, String flagUrl) {
        log.debug("Updating country with id: {}", id);
        
        Country country = countryRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new IllegalArgumentException("Country not found with id: " + id));
        
        // Check for duplicate name (excluding current country)
        if (!country.getName().equalsIgnoreCase(name) && existsByName(name)) {
            throw new IllegalArgumentException("Country with name '" + name + "' already exists");
        }
        
        // Check for duplicate ISO code (excluding current country)
        if (!country.getIsoCode().equalsIgnoreCase(isoCode) && existsByIsoCode(isoCode)) {
            throw new IllegalArgumentException("Country with ISO code '" + isoCode + "' already exists");
        }
        
        country.setName(name);
        country.setDescription(description);
        country.setIsoCode(isoCode);
        country.setPhoneCode(phoneCode);
        country.setFlagUrl(flagUrl);
        
        return countryRepository.save(country);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.debug("Deleting country with id: {}", id);
        countryRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void softDeleteById(Long id) {
        log.debug("Soft deleting country with id: {}", id);
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Country not found with id: " + id));
        country.setActive(false);
        countryRepository.save(country);
    }

    @Override
    @Transactional
    public void restoreById(Long id) {
        log.debug("Restoring country with id: {}", id);
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Country not found with id: " + id));
        country.setActive(true);
        countryRepository.save(country);
    }

    @Override
    public long countAll() {
        return countryRepository.count();
    }

    @Override
    public long countActive() {
        return countryRepository.countActiveEntities();
    }

    @Override
    public long countInactive() {
        return countryRepository.countInactiveEntities();
    }
}