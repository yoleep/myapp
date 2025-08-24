package com.company.common.repository;

import com.company.common.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
    
    Optional<FileEntity> findByFileName(String fileName);
    
    List<FileEntity> findByUploadedById(Long userId);
    
    @Query("SELECT f FROM FileEntity f WHERE f.expiresAt < :now AND f.expiresAt IS NOT NULL")
    List<FileEntity> findExpiredFiles(LocalDateTime now);
    
    List<FileEntity> findByIsPublicTrue();
}