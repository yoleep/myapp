package com.company.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;
    
    @Column(name = "notification_type")
    @Enumerated(EnumType.STRING)
    private NotificationType type;
    
    @Column(name = "priority")
    @Enumerated(EnumType.STRING)
    private NotificationPriority priority = NotificationPriority.NORMAL;
    
    @Column(name = "is_read")
    private Boolean isRead = false;
    
    @Column(name = "read_at")
    private LocalDateTime readAt;
    
    @Column(name = "action_url")
    private String actionUrl;
    
    @Column(name = "icon")
    private String icon;
    
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
    
    @Column(name = "metadata", columnDefinition = "TEXT")
    private String metadata; // JSON string for additional data
    
    public enum NotificationType {
        INFO, SUCCESS, WARNING, ERROR, SYSTEM, USER, EMAIL, PUSH
    }
    
    public enum NotificationPriority {
        LOW, NORMAL, HIGH, URGENT
    }
}