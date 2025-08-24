package com.company.common.service;

import com.company.common.dto.notification.NotificationDto;
import com.company.common.dto.notification.NotificationRequest;
import com.company.common.entity.Notification;
import com.company.common.entity.User;
import com.company.common.exception.ResourceNotFoundException;
import com.company.common.repository.NotificationRepository;
import com.company.common.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationService {
    
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final EmailService emailService;
    
    public NotificationDto createNotification(NotificationRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Notification notification = Notification.builder()
                .user(user)
                .title(request.getTitle())
                .message(request.getMessage())
                .type(request.getType())
                .priority(request.getPriority())
                .actionUrl(request.getActionUrl())
                .icon(request.getIcon())
                .expiresAt(request.getExpiresAt())
                .metadata(request.getMetadata())
                .build();
        
        notification = notificationRepository.save(notification);
        
        // Send real-time notification via WebSocket
        sendRealtimeNotification(user.getId(), convertToDto(notification));
        
        // Send email notification if type is EMAIL
        if (request.getType() == Notification.NotificationType.EMAIL) {
            sendEmailNotification(user, notification);
        }
        
        log.info("Notification created for user: {}", user.getEmail());
        
        return convertToDto(notification);
    }
    
    public Page<NotificationDto> getUserNotifications(Long userId, Pageable pageable) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
                .map(this::convertToDto);
    }
    
    public List<NotificationDto> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public Long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }
    
    public NotificationDto markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        
        if (!notification.getIsRead()) {
            notification.setIsRead(true);
            notification.setReadAt(LocalDateTime.now());
            notification = notificationRepository.save(notification);
        }
        
        return convertToDto(notification);
    }
    
    public void markAllAsRead(Long userId) {
        notificationRepository.markAllAsRead(userId, LocalDateTime.now());
        log.info("All notifications marked as read for user: {}", userId);
    }
    
    public void deleteNotification(Long notificationId, Long userId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        
        if (!notification.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Notification not found");
        }
        
        notificationRepository.delete(notification);
    }
    
    @Async
    public void sendBulkNotifications(List<Long> userIds, NotificationRequest request) {
        for (Long userId : userIds) {
            try {
                request.setUserId(userId);
                createNotification(request);
            } catch (Exception e) {
                log.error("Failed to send notification to user: {}", userId, e);
            }
        }
    }
    
    @Transactional
    public void cleanupOldNotifications(int daysToKeep) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysToKeep);
        List<User> users = userRepository.findAll();
        
        for (User user : users) {
            notificationRepository.deleteByUserIdAndCreatedAtBefore(user.getId(), cutoffDate);
        }
        
        log.info("Old notifications cleaned up (older than {} days)", daysToKeep);
    }
    
    @Transactional
    public void cleanupExpiredNotifications() {
        List<Notification> expiredNotifications = notificationRepository.findExpiredNotifications(LocalDateTime.now());
        notificationRepository.deleteAll(expiredNotifications);
        log.info("Expired notifications cleaned up: {}", expiredNotifications.size());
    }
    
    public NotificationDto sendNotification(Long userId, NotificationRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Notification notification = Notification.builder()
                .user(user)
                .type(request.getType())
                .title(request.getTitle())
                .message(request.getMessage())
                .priority(request.getPriority())
                .actionUrl(request.getActionUrl())
                .icon(request.getIcon())
                .metadata(request.getMetadata())
                .isRead(false)
                .expiresAt(request.getExpiresAt())
                .build();
        
        notification = notificationRepository.save(notification);
        NotificationDto dto = convertToDto(notification);
        
        // Send realtime notification
        sendRealtimeNotification(userId, dto);
        
        return dto;
    }
    
    public Page<NotificationDto> getUserNotifications(Long userId, Boolean isRead, String type, Pageable pageable) {
        Page<Notification> notifications;
        
        if (isRead != null && type != null) {
            notifications = notificationRepository.findByUserIdAndIsReadAndType(userId, isRead, type, pageable);
        } else if (isRead != null) {
            notifications = notificationRepository.findByUserIdAndIsRead(userId, isRead, pageable);
        } else if (type != null) {
            notifications = notificationRepository.findByUserIdAndType(userId, type, pageable);
        } else {
            notifications = notificationRepository.findByUserId(userId, pageable);
        }
        
        return notifications.map(this::convertToDto);
    }
    
    public NotificationDto getNotificationById(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        return convertToDto(notification);
    }
    
    public NotificationDto markAsRead(Long notificationId, Long userId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        
        if (!notification.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized to mark this notification as read");
        }
        
        notification.setIsRead(true);
        notification.setReadAt(LocalDateTime.now());
        notification = notificationRepository.save(notification);
        
        return convertToDto(notification);
    }
    
    public int markMultipleAsRead(List<Long> notificationIds, Long userId) {
        int count = 0;
        for (Long id : notificationIds) {
            try {
                markAsRead(id, userId);
                count++;
            } catch (Exception e) {
                log.error("Failed to mark notification {} as read", id, e);
            }
        }
        return count;
    }
    
    public int clearReadNotifications(Long userId) {
        List<Notification> readNotifications = notificationRepository.findByUserIdAndIsReadTrue(userId);
        notificationRepository.deleteAll(readNotifications);
        return readNotifications.size();
    }
    
    public Map<String, Object> getUserPreferences(Long userId) {
        // This would typically be stored in a separate preferences table
        Map<String, Object> preferences = new HashMap<>();
        preferences.put("emailEnabled", true);
        preferences.put("pushEnabled", true);
        preferences.put("smsEnabled", false);
        preferences.put("notificationTypes", List.of("INFO", "WARNING", "ERROR"));
        return preferences;
    }
    
    public Map<String, Object> updateUserPreferences(Long userId, Map<String, Object> preferences) {
        // This would typically update a preferences table
        log.info("Updated notification preferences for user: {}", userId);
        return preferences;
    }
    
    private void sendRealtimeNotification(Long userId, NotificationDto notification) {
        try {
            messagingTemplate.convertAndSendToUser(
                    userId.toString(),
                    "/queue/notifications",
                    notification
            );
        } catch (Exception e) {
            log.error("Failed to send realtime notification", e);
        }
    }
    
    @Async
    private void sendEmailNotification(User user, Notification notification) {
        try {
            emailService.sendNotificationEmail(
                    user.getEmail(),
                    notification.getTitle(),
                    notification.getMessage()
            );
        } catch (Exception e) {
            log.error("Failed to send email notification to: {}", user.getEmail(), e);
        }
    }
    
    private NotificationDto convertToDto(Notification notification) {
        return NotificationDto.builder()
                .id(notification.getId())
                .userId(notification.getUser().getId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .type(notification.getType())
                .priority(notification.getPriority())
                .isRead(notification.getIsRead())
                .readAt(notification.getReadAt())
                .actionUrl(notification.getActionUrl())
                .icon(notification.getIcon())
                .createdAt(notification.getCreatedAt())
                .expiresAt(notification.getExpiresAt())
                .metadata(notification.getMetadata())
                .build();
    }
}