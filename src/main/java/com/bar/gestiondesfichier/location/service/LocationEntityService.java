package com.bar.gestiondesfichier.location.service;

import com.bar.gestiondesfichier.common.service.BaseService;
import com.bar.gestiondesfichier.location.model.LocationEntity;
import com.bar.gestiondesfichier.location.model.LocationEntity.EntityType;
import com.bar.gestiondesfichier.location.projection.LocationEntityProjection;

import java.util.List;
import java.util.Optional;

public interface LocationEntityService extends BaseService<LocationEntity> {
    
    List<LocationEntity> findByCountryId(Long countryId);
    
    List<LocationEntity> findByEntityType(EntityType entityType);
    
    List<LocationEntity> findByCountryIdAndEntityType(Long countryId, EntityType entityType);
    
    Optional<LocationEntity> findByCode(String code);
    
    List<LocationEntity> searchByName(String name);
    
    List<LocationEntityProjection> findProjectionsByCountryId(Long countryId);
    
    Optional<LocationEntityProjection> findProjectionById(Long id);
    
    boolean existsByCode(String code);
    
    boolean existsByNameAndCountry(String name, Long countryId);
    
    long countByCountryId(Long countryId);
    
    long countByEntityType(EntityType entityType);
    
    LocationEntity createLocationEntity(String name, String description, EntityType entityType, String code, 
                                       String postalCode, Long countryId);
    
    LocationEntity updateLocationEntity(Long id, String name, String description, EntityType entityType, 
                                       String code, String postalCode, Long countryId);
}