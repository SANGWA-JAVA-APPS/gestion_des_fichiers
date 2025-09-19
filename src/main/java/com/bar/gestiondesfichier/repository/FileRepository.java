package com.bar.gestiondesfichier.repository;

import com.bar.gestiondesfichier.entity.FileEntity;
import com.bar.gestiondesfichier.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
    
    List<FileEntity> findByOwner(Account owner);
    
    List<FileEntity> findByOwnerAndActiveTrue(Account owner);
    
    Optional<FileEntity> findByFileNameAndActiveTrue(String fileName);
    
    @Query("SELECT f FROM FileEntity f WHERE f.owner.username = :username AND f.active = true")
    List<FileEntity> findActiveFilesByUsername(@Param("username") String username);
    
    @Query("SELECT f FROM FileEntity f WHERE f.contentType LIKE %:contentType% AND f.active = true")
    List<FileEntity> findByContentTypeContaining(@Param("contentType") String contentType);
    
    long countByOwnerAndActiveTrue(Account owner);
}