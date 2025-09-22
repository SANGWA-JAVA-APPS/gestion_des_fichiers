package com.igihecyubuntu.app.repository;

import com.igihecyubuntu.app.entity.PostCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostCategoryRepository extends JpaRepository<PostCategory, Long> {

    List<PostCategory> findByPostId(Long postId);

    List<PostCategory> findByCategoryId(Long categoryId);

    void deleteByPostIdAndCategoryId(Long postId, Long categoryId);

    // Dashboard queries using List<Object[]>
    @Query("SELECT pc.postId, COUNT(pc.categoryId) " +
           "FROM PostCategory pc " +
           "GROUP BY pc.postId " +
           "ORDER BY COUNT(pc.categoryId) DESC")
    List<Object[]> findCategoryCountsByPost();
}