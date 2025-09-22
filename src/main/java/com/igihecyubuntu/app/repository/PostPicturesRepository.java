package com.igihecyubuntu.app.repository;

import com.igihecyubuntu.app.entity.PostPictures;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostPicturesRepository extends JpaRepository<PostPictures, Long> {

    List<PostPictures> findByPostId(Long postId);

    List<PostPictures> findByPictureId(Long pictureId);

    void deleteByPostIdAndPictureId(Long postId, Long pictureId);

    // Dashboard queries using List<Object[]>
    @Query("SELECT pp.postId, COUNT(pp.pictureId) " +
           "FROM PostPictures pp " +
           "GROUP BY pp.postId " +
           "ORDER BY COUNT(pp.pictureId) DESC")
    List<Object[]> findPictureCountsByPost();
}