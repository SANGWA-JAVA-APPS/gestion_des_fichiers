package com.igihecyubuntu.app.repository;

import com.igihecyubuntu.app.entity.Post;
import com.igihecyubuntu.app.dto.projection.PostProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p.id as id, p.dateTime as dateTime, p.blogId as blogId, " +
           "p.doneBy as doneBy, b.title as blogTitle " +
           "FROM Post p JOIN Blog b ON p.blogId = b.id")
    List<PostProjection> findAllProjectedBy();

    @Query("SELECT p.id as id, p.dateTime as dateTime, p.blogId as blogId, " +
           "p.doneBy as doneBy, b.title as blogTitle " +
           "FROM Post p JOIN Blog b ON p.blogId = b.id WHERE p.id = :id")
    Optional<PostProjection> findProjectedById(Long id);

    @Query("SELECT p.id as id, p.dateTime as dateTime, p.blogId as blogId, " +
           "p.doneBy as doneBy, b.title as blogTitle " +
           "FROM Post p JOIN Blog b ON p.blogId = b.id WHERE p.blogId = :blogId " +
           "ORDER BY p.dateTime DESC")
    List<PostProjection> findByBlogIdOrderByDateTimeDesc(Long blogId);

    @Query("SELECT p.id as id, p.dateTime as dateTime, p.blogId as blogId, " +
           "p.doneBy as doneBy, b.title as blogTitle " +
           "FROM Post p JOIN Blog b ON p.blogId = b.id WHERE p.doneBy = :doneBy " +
           "ORDER BY p.dateTime DESC")
    List<PostProjection> findByDoneByOrderByDateTimeDesc(Long doneBy);

    // Dashboard queries using List<Object[]>
    @Query("SELECT DATE(p.dateTime), COUNT(p) " +
           "FROM Post p " +
           "GROUP BY DATE(p.dateTime) " +
           "ORDER BY DATE(p.dateTime) DESC")
    List<Object[]> findPostCountsByDate();

    @Query("SELECT b.title, COUNT(p) " +
           "FROM Post p JOIN Blog b ON p.blogId = b.id " +
           "GROUP BY b.id, b.title " +
           "ORDER BY COUNT(p) DESC")
    List<Object[]> findPostCountsByBlog();

    @Query("SELECT p.doneBy, COUNT(p) " +
           "FROM Post p " +
           "GROUP BY p.doneBy " +
           "ORDER BY COUNT(p) DESC")
    List<Object[]> findPostCountsByUser();
}