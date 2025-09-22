package com.igihecyubuntu.app.repository;

import com.igihecyubuntu.app.entity.Likes;
import com.igihecyubuntu.app.dto.projection.LikesProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {

    List<LikesProjection> findAllProjectedBy();

    Optional<LikesProjection> findProjectedById(Long id);

    List<LikesProjection> findByPostIdOrderByDateTimeDesc(Long postId);

    List<LikesProjection> findByDoneByOrderByDateTimeDesc(Long doneBy);

    Optional<Likes> findByPostIdAndDoneBy(Long postId, Long doneBy);

    long countByPostId(Long postId);

    // Dashboard queries using List<Object[]>
    @Query("SELECT l.postId, COUNT(l) " +
           "FROM Likes l " +
           "GROUP BY l.postId " +
           "ORDER BY COUNT(l) DESC")
    List<Object[]> findLikesCountsByPost();

    @Query("SELECT DATE(l.dateTime), COUNT(l) " +
           "FROM Likes l " +
           "GROUP BY DATE(l.dateTime) " +
           "ORDER BY DATE(l.dateTime) DESC")
    List<Object[]> findLikesCountsByDate();

    @Query("SELECT l.doneBy, COUNT(l) " +
           "FROM Likes l " +
           "GROUP BY l.doneBy " +
           "ORDER BY COUNT(l) DESC")
    List<Object[]> findLikesCountsByUser();
}