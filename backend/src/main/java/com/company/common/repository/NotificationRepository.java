package com.company.common.repository;

import com.company.common.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    Page<Notification> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    List<Notification> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(Long userId);
    
    Long countByUserIdAndIsReadFalse(Long userId);
    
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true, n.readAt = :readAt WHERE n.user.id = :userId AND n.isRead = false")
    void markAllAsRead(Long userId, LocalDateTime readAt);
    
    @Query("SELECT n FROM Notification n WHERE n.expiresAt < :now AND n.expiresAt IS NOT NULL")
    List<Notification> findExpiredNotifications(LocalDateTime now);
    
    void deleteByUserIdAndCreatedAtBefore(Long userId, LocalDateTime date);
    
    Page<Notification> findByUserId(Long userId, Pageable pageable);
    
    Page<Notification> findByUserIdAndIsRead(Long userId, Boolean isRead, Pageable pageable);
    
    Page<Notification> findByUserIdAndType(Long userId, String type, Pageable pageable);
    
    Page<Notification> findByUserIdAndIsReadAndType(Long userId, Boolean isRead, String type, Pageable pageable);
    
    List<Notification> findByUserIdAndIsReadTrue(Long userId);
}