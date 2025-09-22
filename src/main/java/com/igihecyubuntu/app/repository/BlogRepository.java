package com.igihecyubuntu.app.repository;

import com.igihecyubuntu.app.entity.Blog;
import com.igihecyubuntu.app.dto.projection.BlogProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

    List<BlogProjection> findAllProjectedBy();

    Optional<BlogProjection> findProjectedById(Long id);

    List<BlogProjection> findByStatusOrderByIdDesc(String status);

    // Dashboard queries using List<Object[]>
    @Query("SELECT COUNT(b), b.status FROM Blog b GROUP BY b.status")
    List<Object[]> findBlogCountsByStatus();

    @Query("SELECT b.title, b.status, COUNT(p) as postCount " +
           "FROM Blog b LEFT JOIN Post p ON b.id = p.blogId " +
           "GROUP BY b.id, b.title, b.status " +
           "ORDER BY postCount DESC")
    List<Object[]> findBlogStatistics();
}