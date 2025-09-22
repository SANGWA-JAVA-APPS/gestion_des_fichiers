package com.igihecyubuntu.app.repository;

import com.igihecyubuntu.app.entity.Picture;
import com.igihecyubuntu.app.dto.projection.PictureProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Long> {

    List<PictureProjection> findAllProjectedBy();

    Optional<PictureProjection> findProjectedById(Long id);

    List<PictureProjection> findByTypeOrderByDateTimeDesc(String type);

    List<PictureProjection> findByDoneByOrderByDateTimeDesc(Long doneBy);

    // Dashboard queries using List<Object[]>
    @Query("SELECT p.type, COUNT(p) FROM Picture p GROUP BY p.type")
    List<Object[]> findPictureCountsByType();

    @Query("SELECT DATE(p.dateTime), COUNT(p) " +
           "FROM Picture p " +
           "GROUP BY DATE(p.dateTime) " +
           "ORDER BY DATE(p.dateTime) DESC")
    List<Object[]> findPictureUploadsByDate();
}