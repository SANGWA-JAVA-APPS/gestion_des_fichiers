package com.igihecyubuntu.app.repository;

import com.igihecyubuntu.app.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {

    List<PostComment> findByPostId(Long postId);

    List<PostComment> findByCommentId(Long commentId);

    void deleteByPostIdAndCommentId(Long postId, Long commentId);

    // Dashboard queries using List<Object[]>
    @Query("SELECT pc.postId, COUNT(pc.commentId) " +
           "FROM PostComment pc " +
           "GROUP BY pc.postId " +
           "ORDER BY COUNT(pc.commentId) DESC")
    List<Object[]> findCommentCountsByPost();
}