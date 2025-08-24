package com.company.common.dto.notification;

import com.company.common.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    
    private Long id;
    private Long userId;
    private String title;
    private String message;
    private Notification.NotificationType type;
    private Notification.NotificationPriority priority;
    private Boolean isRead;
    private LocalDateTime readAt;
    private String actionUrl;
    private String icon;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private String metadata;
}