package com.bar.gestiondesfichier.location.repository;

import com.bar.gestiondesfichier.common.repository.BaseRepository;
import com.bar.gestiondesfichier.location.model.Country;
import com.bar.gestiondesfichier.location.projection.CountryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository extends BaseRepository<Country> {
    
    // Pagination methods
    Page<Country> findByActiveTrue(Pageable pageable);
    
    @Query("SELECT c FROM Country c WHERE c.active = true AND LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Country> findByNameContainingIgnoreCaseAndActiveTrue(@Param("name") String name, Pageable pageable);
    
    // Existing methods
    Optional<Country> findByIsoCodeAndActiveTrue(String isoCode);
    
    Optional<Country> findByNameIgnoreCaseAndActiveTrue(String name);
    
    List<Country> findByNameContainingIgnoreCaseAndActiveTrue(String name);
    
    @Query("SELECT c as country, " +
           "(SELECT COUNT(e) FROM LocationEntity e WHERE e.country = c AND e.active = true) as entitiesCount " +
           "FROM Country c WHERE c.active = true ORDER BY c.name")
    List<CountryProjection> findAllActiveProjections();
    
    @Query("SELECT c as country, " +
           "(SELECT COUNT(e) FROM LocationEntity e WHERE e.country = c AND e.active = true) as entitiesCount " +
           "FROM Country c WHERE c.id = :id AND c.active = true")
    Optional<CountryProjection> findProjectionById(@Param("id") Long id);
    
    boolean existsByIsoCodeAndActiveTrue(String isoCode);
    
    boolean existsByNameIgnoreCaseAndActiveTrue(String name);
}