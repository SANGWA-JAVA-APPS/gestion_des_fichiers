package com.bar.gestiondesfichier.location.repository;

import com.bar.gestiondesfichier.common.repository.BaseRepository;
import com.bar.gestiondesfichier.location.model.LocationEntity;
import com.bar.gestiondesfichier.location.model.LocationEntity.EntityType;
import com.bar.gestiondesfichier.location.projection.LocationEntityProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationEntityRepository extends BaseRepository<LocationEntity> {
    
    List<LocationEntity> findByCountryIdAndActiveTrue(Long countryId);
    
    List<LocationEntity> findByEntityTypeAndActiveTrue(EntityType entityType);
    
    List<LocationEntity> findByCountryIdAndEntityTypeAndActiveTrue(Long countryId, EntityType entityType);
    
    Optional<LocationEntity> findByCodeAndActiveTrue(String code);
    
    List<LocationEntity> findByNameContainingIgnoreCaseAndActiveTrue(String name);
    
    @Query("SELECT le as locationEntity, " +
           "(SELECT COUNT(m) FROM Module m WHERE m.locationEntity = le AND m.active = true) as modulesCount " +
           "FROM LocationEntity le WHERE le.country.id = :countryId AND le.active = true ORDER BY le.name")
    List<LocationEntityProjection> findProjectionsByCountryId(@Param("countryId") Long countryId);
    
    @Query("SELECT le as locationEntity, " +
           "(SELECT COUNT(m) FROM Module m WHERE m.locationEntity = le AND m.active = true) as modulesCount " +
           "FROM LocationEntity le WHERE le.id = :id AND le.active = true")
    Optional<LocationEntityProjection> findProjectionById(@Param("id") Long id);
    
    boolean existsByCodeAndActiveTrue(String code);
    
    boolean existsByNameIgnoreCaseAndCountryIdAndActiveTrue(String name, Long countryId);
    
    long countByCountryIdAndActiveTrue(Long countryId);
    
    long countByEntityTypeAndActiveTrue(EntityType entityType);
}