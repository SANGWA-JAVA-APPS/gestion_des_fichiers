package com.bar.gestiondesfichier.location.repository;

import com.bar.gestiondesfichier.common.repository.BaseRepository;
import com.bar.gestiondesfichier.location.model.Section;
import com.bar.gestiondesfichier.location.model.Section.SectionType;
import com.bar.gestiondesfichier.location.model.Section.AccessLevel;
import com.bar.gestiondesfichier.location.projection.SectionProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SectionRepository extends BaseRepository<Section> {
    
    // Paginated methods
    Page<Section> findByActiveTrue(Pageable pageable);
    
    Page<Section> findByModuleIdAndActiveTrue(Long moduleId, Pageable pageable);
    
    Page<Section> findBySectionTypeAndActiveTrue(SectionType sectionType, Pageable pageable);
    
    Page<Section> findByAccessLevelAndActiveTrue(AccessLevel accessLevel, Pageable pageable);
    
    Page<Section> findByModuleIdAndSectionTypeAndActiveTrue(Long moduleId, SectionType sectionType, Pageable pageable);
    
    // Non-paginated methods (legacy support)
    List<Section> findByModuleIdAndActiveTrue(Long moduleId);
    
    List<Section> findBySectionTypeAndActiveTrue(SectionType sectionType);
    
    List<Section> findByAccessLevelAndActiveTrue(AccessLevel accessLevel);
    
    List<Section> findByModuleIdAndSectionTypeAndActiveTrue(Long moduleId, SectionType sectionType);
    
    List<Section> findByModuleIdAndAccessLevelAndActiveTrue(Long moduleId, AccessLevel accessLevel);
    
    List<Section> findByFloorNumberAndActiveTrue(Integer floorNumber);
    
    List<Section> findByModuleIdAndFloorNumberAndActiveTrue(Long moduleId, Integer floorNumber);
    
    Optional<Section> findBySectionCodeAndActiveTrue(String sectionCode);
    
    List<Section> findByNameContainingIgnoreCaseAndActiveTrue(String name);
    
    List<Section> findByRoomNumberContainingIgnoreCaseAndActiveTrue(String roomNumber);
    
    @Query("SELECT s FROM Section s WHERE s.module.id = :moduleId AND s.active = true ORDER BY s.floorNumber, s.roomNumber, s.name")
    List<SectionProjection> findProjectionsByModuleId(@Param("moduleId") Long moduleId);
    
    @Query("SELECT s FROM Section s WHERE s.id = :id AND s.active = true")
    Optional<SectionProjection> findProjectionById(@Param("id") Long id);
    
    @Query("SELECT s FROM Section s JOIN FETCH s.module m JOIN FETCH m.locationEntity le JOIN FETCH le.country " +
           "WHERE s.active = true ORDER BY le.country.name, le.name, m.name, s.floorNumber, s.roomNumber")
    List<Section> findAllWithLocationHierarchy();
    
    @Query("SELECT s FROM Section s WHERE s.capacity >= :minCapacity AND s.active = true ORDER BY s.capacity DESC")
    List<Section> findByMinCapacity(@Param("minCapacity") Integer minCapacity);
    
    boolean existsBySectionCodeAndActiveTrue(String sectionCode);
    
    boolean existsByNameIgnoreCaseAndModuleIdAndActiveTrue(String name, Long moduleId);
    
    boolean existsByRoomNumberAndModuleIdAndActiveTrue(String roomNumber, Long moduleId);
    
    long countByModuleIdAndActiveTrue(Long moduleId);
    
    long countBySectionTypeAndActiveTrue(SectionType sectionType);
    
    long countByAccessLevelAndActiveTrue(AccessLevel accessLevel);
    
    long countByFloorNumberAndActiveTrue(Integer floorNumber);
}