package com.igihecyubuntu.app.repository;

import com.igihecyubuntu.app.entity.Comment;
import com.igihecyubuntu.app.dto.projection.CommentProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<CommentProjection> findAllProjectedBy();

    Optional<CommentProjection> findProjectedById(Long id);

    List<CommentProjection> findByDoneByOrderByIdDesc(Long doneBy);

    // Dashboard queries using List<Object[]>
    @Query("SELECT COUNT(c), c.doneBy FROM Comment c GROUP BY c.doneBy")
    List<Object[]> findCommentCountsByUser();

    @Query("SELECT DATE(pc.post.dateTime), COUNT(c) " +
           "FROM Comment c JOIN PostComment pc ON c.id = pc.commentId " +
           "GROUP BY DATE(pc.post.dateTime) " +
           "ORDER BY DATE(pc.post.dateTime) DESC")
    List<Object[]> findCommentCountsByDate();
}