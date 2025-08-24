package com.company.common.controller;

import com.company.common.dto.notification.NotificationDto;
import com.company.common.dto.notification.NotificationRequest;
import com.company.common.service.NotificationService;
import com.company.common.util.Auditable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification Management", description = "APIs for managing notifications")
public class NotificationController {
    
    private final NotificationService notificationService;
    
    @PostMapping("/send")
    @PreAuthorize("isAuthenticated()")
    @Auditable(action = "SEND_NOTIFICATION")
    @Operation(summary = "Send notification", description = "Send a notification to users")
    public ResponseEntity<NotificationDto> sendNotification(
            @Valid @RequestBody NotificationRequest request,
            @Parameter(hidden = true) @RequestAttribute("userId") Long senderId) {
        NotificationDto notification = notificationService.sendNotification(
                request.getUserId(),
                request
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(notification);
    }
    
    @PostMapping("/send/bulk")
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(action = "SEND_BULK_NOTIFICATION")
    @Operation(summary = "Send bulk notifications", description = "Send notifications to multiple users")
    public ResponseEntity<Map<String, Object>> sendBulkNotifications(
            @RequestBody Map<String, Object> request) {
        List<Long> userIds = (List<Long>) request.get("userIds");
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setType((String) request.get("type"));
        notificationRequest.setTitle((String) request.get("title"));
        notificationRequest.setMessage((String) request.get("message"));
        notificationRequest.setPriority((String) request.get("priority"));
        
        notificationService.sendBulkNotifications(userIds, notificationRequest);
        
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Bulk notifications sent",
                "count", userIds.size()
        ));
    }
    
    @GetMapping("/users/{userId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get user notifications", description = "Get all notifications for a user")
    public ResponseEntity<Page<NotificationDto>> getUserNotifications(
            @PathVariable Long userId,
            @RequestParam(required = false) Boolean isRead,
            @RequestParam(required = false) String type,
            @PageableDefault(size = 20) Pageable pageable,
            @Parameter(hidden = true) @RequestAttribute("userId") Long requesterId) {
        
        // Users can only view their own notifications unless admin
        if (!userId.equals(requesterId) && !isAdmin(requesterId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        Page<NotificationDto> notifications = notificationService.getUserNotifications(
                userId, isRead, type, pageable);
        return ResponseEntity.ok(notifications);
    }
    
    @GetMapping("/{notificationId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get notification", description = "Get a specific notification by ID")
    public ResponseEntity<NotificationDto> getNotification(
            @PathVariable Long notificationId,
            @Parameter(hidden = true) @RequestAttribute("userId") Long userId) {
        NotificationDto notification = notificationService.getNotificationById(notificationId);
        
        // Check if user has access to this notification
        if (!notification.getUserId().equals(userId) && !isAdmin(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        return ResponseEntity.ok(notification);
    }
    
    @PutMapping("/{notificationId}/read")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Mark as read", description = "Mark a notification as read")
    public ResponseEntity<NotificationDto> markAsRead(
            @PathVariable Long notificationId,
            @Parameter(hidden = true) @RequestAttribute("userId") Long userId) {
        NotificationDto notification = notificationService.markAsRead(notificationId, userId);
        return ResponseEntity.ok(notification);
    }
    
    @PutMapping("/read/bulk")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Mark multiple as read", description = "Mark multiple notifications as read")
    public ResponseEntity<Map<String, Object>> markMultipleAsRead(
            @RequestBody List<Long> notificationIds,
            @Parameter(hidden = true) @RequestAttribute("userId") Long userId) {
        int count = notificationService.markMultipleAsRead(notificationIds, userId);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "count", count
        ));
    }
    
    @PutMapping("/users/{userId}/read-all")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Mark all as read", description = "Mark all user notifications as read")
    public ResponseEntity<Map<String, Object>> markAllAsRead(
            @PathVariable Long userId,
            @Parameter(hidden = true) @RequestAttribute("userId") Long requesterId) {
        
        // Users can only mark their own notifications as read
        if (!userId.equals(requesterId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "All notifications marked as read"
        ));
    }
    
    @DeleteMapping("/{notificationId}")
    @PreAuthorize("isAuthenticated()")
    @Auditable(action = "DELETE_NOTIFICATION")
    @Operation(summary = "Delete notification", description = "Delete a notification")
    public ResponseEntity<Void> deleteNotification(
            @PathVariable Long notificationId,
            @Parameter(hidden = true) @RequestAttribute("userId") Long userId) {
        notificationService.deleteNotification(notificationId, userId);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/users/{userId}/clear")
    @PreAuthorize("isAuthenticated()")
    @Auditable(action = "CLEAR_NOTIFICATIONS")
    @Operation(summary = "Clear notifications", description = "Clear all read notifications for a user")
    public ResponseEntity<Map<String, Object>> clearReadNotifications(
            @PathVariable Long userId,
            @Parameter(hidden = true) @RequestAttribute("userId") Long requesterId) {
        
        // Users can only clear their own notifications
        if (!userId.equals(requesterId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        int count = notificationService.clearReadNotifications(userId);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "count", count
        ));
    }
    
    @GetMapping("/users/{userId}/unread-count")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get unread count", description = "Get count of unread notifications")
    public ResponseEntity<Map<String, Long>> getUnreadCount(
            @PathVariable Long userId,
            @Parameter(hidden = true) @RequestAttribute("userId") Long requesterId) {
        
        // Users can only view their own count
        if (!userId.equals(requesterId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        Long count = notificationService.getUnreadCount(userId);
        return ResponseEntity.ok(Map.of("count", count));
    }
    
    @GetMapping("/users/{userId}/preferences")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get notification preferences", description = "Get user notification preferences")
    public ResponseEntity<Map<String, Object>> getNotificationPreferences(
            @PathVariable Long userId,
            @Parameter(hidden = true) @RequestAttribute("userId") Long requesterId) {
        
        // Users can only view their own preferences
        if (!userId.equals(requesterId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        Map<String, Object> preferences = notificationService.getUserPreferences(userId);
        return ResponseEntity.ok(preferences);
    }
    
    @PutMapping("/users/{userId}/preferences")
    @PreAuthorize("isAuthenticated()")
    @Auditable(action = "UPDATE_NOTIFICATION_PREFERENCES")
    @Operation(summary = "Update preferences", description = "Update user notification preferences")
    public ResponseEntity<Map<String, Object>> updateNotificationPreferences(
            @PathVariable Long userId,
            @RequestBody Map<String, Object> preferences,
            @Parameter(hidden = true) @RequestAttribute("userId") Long requesterId) {
        
        // Users can only update their own preferences
        if (!userId.equals(requesterId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        Map<String, Object> updated = notificationService.updateUserPreferences(userId, preferences);
        return ResponseEntity.ok(updated);
    }
    
    // Helper method to check if user is admin (would typically be in a service)
    private boolean isAdmin(Long userId) {
        // This would typically check the user's roles
        // Placeholder implementation
        return false;
    }
}