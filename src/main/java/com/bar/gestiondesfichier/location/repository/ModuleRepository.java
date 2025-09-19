package com.bar.gestiondesfichier.location.repository;

import com.bar.gestiondesfichier.common.repository.BaseRepository;
import com.bar.gestiondesfichier.location.model.Module;
import com.bar.gestiondesfichier.location.model.Module.ModuleType;
import com.bar.gestiondesfichier.location.projection.ModuleProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModuleRepository extends BaseRepository<Module> {
    
    // Paginated methods
    Page<Module> findByActiveTrue(Pageable pageable);
    
    Page<Module> findByLocationEntityIdAndActiveTrue(Long locationEntityId, Pageable pageable);
    
    Page<Module> findByModuleTypeAndActiveTrue(ModuleType moduleType, Pageable pageable);
    
    Page<Module> findByLocationEntityIdAndModuleTypeAndActiveTrue(Long locationEntityId, ModuleType moduleType, Pageable pageable);
    
    // Non-paginated methods (legacy support)
    List<Module> findByLocationEntityIdAndActiveTrue(Long locationEntityId);
    
    List<Module> findByModuleTypeAndActiveTrue(ModuleType moduleType);
    
    List<Module> findByLocationEntityIdAndModuleTypeAndActiveTrue(Long locationEntityId, ModuleType moduleType);
    
    Optional<Module> findByModuleCodeAndActiveTrue(String moduleCode);
    
    List<Module> findByNameContainingIgnoreCaseAndActiveTrue(String name);
    
    @Query("SELECT m as module, " +
           "(SELECT COUNT(s) FROM Section s WHERE s.module = m AND s.active = true) as sectionsCount " +
           "FROM Module m WHERE m.locationEntity.id = :locationEntityId AND m.active = true ORDER BY m.name")
    List<ModuleProjection> findProjectionsByLocationEntityId(@Param("locationEntityId") Long locationEntityId);
    
    @Query("SELECT m as module, " +
           "(SELECT COUNT(s) FROM Section s WHERE s.module = m AND s.active = true) as sectionsCount " +
           "FROM Module m WHERE m.id = :id AND m.active = true")
    Optional<ModuleProjection> findProjectionById(@Param("id") Long id);
    
    @Query("SELECT m FROM Module m JOIN FETCH m.locationEntity le JOIN FETCH le.country " +
           "WHERE m.active = true ORDER BY le.country.name, le.name, m.name")
    List<Module> findAllWithLocationHierarchy();
    
    boolean existsByModuleCodeAndActiveTrue(String moduleCode);
    
    boolean existsByNameIgnoreCaseAndLocationEntityIdAndActiveTrue(String name, Long locationEntityId);
    
    long countByLocationEntityIdAndActiveTrue(Long locationEntityId);
    
    long countByModuleTypeAndActiveTrue(ModuleType moduleType);
}