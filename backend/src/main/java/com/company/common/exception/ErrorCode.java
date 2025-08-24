package com.company.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    
    // Authentication Errors (AUTH_)
    AUTH_INVALID_CREDENTIALS("AUTH_001", "Invalid email or password", 401),
    AUTH_TOKEN_EXPIRED("AUTH_002", "Authentication token has expired", 401),
    AUTH_TOKEN_INVALID("AUTH_003", "Invalid authentication token", 401),
    AUTH_ACCOUNT_LOCKED("AUTH_004", "Account is locked due to multiple failed attempts", 403),
    AUTH_ACCOUNT_DISABLED("AUTH_005", "Account has been disabled", 403),
    AUTH_EMAIL_NOT_VERIFIED("AUTH_006", "Email address not verified", 403),
    AUTH_MFA_REQUIRED("AUTH_007", "Multi-factor authentication required", 401),
    AUTH_MFA_INVALID("AUTH_008", "Invalid MFA code", 401),
    AUTH_SESSION_EXPIRED("AUTH_009", "Session has expired", 401),
    AUTH_INSUFFICIENT_PERMISSIONS("AUTH_010", "Insufficient permissions", 403),
    
    // Validation Errors (VALIDATION_)
    VALIDATION_FAILED("VALIDATION_001", "Validation failed", 400),
    VALIDATION_EMAIL_INVALID("VALIDATION_002", "Invalid email format", 400),
    VALIDATION_PASSWORD_WEAK("VALIDATION_003", "Password does not meet requirements", 400),
    VALIDATION_FIELD_REQUIRED("VALIDATION_004", "Required field is missing", 400),
    VALIDATION_FIELD_TOO_LONG("VALIDATION_005", "Field exceeds maximum length", 400),
    VALIDATION_FIELD_TOO_SHORT("VALIDATION_006", "Field is below minimum length", 400),
    VALIDATION_INVALID_FORMAT("VALIDATION_007", "Invalid data format", 400),
    VALIDATION_DUPLICATE_ENTRY("VALIDATION_008", "Duplicate entry exists", 409),
    VALIDATION_INVALID_DATE_RANGE("VALIDATION_009", "Invalid date range", 400),
    VALIDATION_FILE_TYPE_NOT_ALLOWED("VALIDATION_010", "File type not allowed", 400),
    
    // System Errors (SYSTEM_)
    SYSTEM_INTERNAL_ERROR("SYSTEM_001", "Internal server error", 500),
    SYSTEM_SERVICE_UNAVAILABLE("SYSTEM_002", "Service temporarily unavailable", 503),
    SYSTEM_DATABASE_ERROR("SYSTEM_003", "Database operation failed", 500),
    SYSTEM_EXTERNAL_SERVICE_ERROR("SYSTEM_004", "External service error", 502),
    SYSTEM_CONFIGURATION_ERROR("SYSTEM_005", "System configuration error", 500),
    SYSTEM_MAINTENANCE_MODE("SYSTEM_006", "System is under maintenance", 503),
    SYSTEM_RATE_LIMIT_EXCEEDED("SYSTEM_007", "Rate limit exceeded", 429),
    SYSTEM_TIMEOUT("SYSTEM_008", "Request timeout", 408),
    
    // Resource Errors (RESOURCE_)
    RESOURCE_NOT_FOUND("RESOURCE_001", "Resource not found", 404),
    RESOURCE_ALREADY_EXISTS("RESOURCE_002", "Resource already exists", 409),
    RESOURCE_LOCKED("RESOURCE_003", "Resource is locked", 423),
    RESOURCE_DELETED("RESOURCE_004", "Resource has been deleted", 410),
    RESOURCE_EXPIRED("RESOURCE_005", "Resource has expired", 410),
    RESOURCE_QUOTA_EXCEEDED("RESOURCE_006", "Resource quota exceeded", 413),
    RESOURCE_ACCESS_DENIED("RESOURCE_007", "Access to resource denied", 403),
    
    // User Errors (USER_)
    USER_NOT_FOUND("USER_001", "User not found", 404),
    USER_ALREADY_EXISTS("USER_002", "User already exists", 409),
    USER_INACTIVE("USER_003", "User account is inactive", 403),
    USER_SUSPENDED("USER_004", "User account is suspended", 403),
    USER_DELETED("USER_005", "User account has been deleted", 410),
    USER_EMAIL_ALREADY_EXISTS("USER_006", "Email address already registered", 409),
    USER_PROFILE_INCOMPLETE("USER_007", "User profile is incomplete", 400),
    
    // File Errors (FILE_)
    FILE_NOT_FOUND("FILE_001", "File not found", 404),
    FILE_UPLOAD_FAILED("FILE_002", "File upload failed", 500),
    FILE_TOO_LARGE("FILE_003", "File size exceeds limit", 413),
    FILE_TYPE_NOT_SUPPORTED("FILE_004", "File type not supported", 415),
    FILE_VIRUS_DETECTED("FILE_005", "Virus detected in file", 422),
    FILE_CORRUPTED("FILE_006", "File is corrupted", 422),
    FILE_STORAGE_FULL("FILE_007", "Storage quota exceeded", 507),
    
    // Notification Errors (NOTIFICATION_)
    NOTIFICATION_NOT_FOUND("NOTIFICATION_001", "Notification not found", 404),
    NOTIFICATION_SEND_FAILED("NOTIFICATION_002", "Failed to send notification", 500),
    NOTIFICATION_TEMPLATE_NOT_FOUND("NOTIFICATION_003", "Notification template not found", 404),
    NOTIFICATION_CHANNEL_UNAVAILABLE("NOTIFICATION_004", "Notification channel unavailable", 503),
    
    // Board Errors (BOARD_)
    BOARD_NOT_FOUND("BOARD_001", "Board not found", 404),
    BOARD_POST_NOT_FOUND("BOARD_002", "Post not found", 404),
    BOARD_COMMENT_NOT_FOUND("BOARD_003", "Comment not found", 404),
    BOARD_ACCESS_DENIED("BOARD_004", "Board access denied", 403),
    BOARD_POSTING_DISABLED("BOARD_005", "Posting is disabled", 403),
    BOARD_COMMENT_DISABLED("BOARD_006", "Comments are disabled", 403),
    
    // Menu Errors (MENU_)
    MENU_NOT_FOUND("MENU_001", "Menu not found", 404),
    MENU_ALREADY_EXISTS("MENU_002", "Menu already exists", 409),
    MENU_CIRCULAR_REFERENCE("MENU_003", "Circular menu reference detected", 400),
    MENU_MAX_DEPTH_EXCEEDED("MENU_004", "Maximum menu depth exceeded", 400),
    
    // Code Management Errors (CODE_)
    CODE_GROUP_NOT_FOUND("CODE_001", "Code group not found", 404),
    CODE_ITEM_NOT_FOUND("CODE_002", "Code item not found", 404),
    CODE_ALREADY_EXISTS("CODE_003", "Code already exists", 409),
    CODE_IN_USE("CODE_004", "Code is in use and cannot be deleted", 409),
    CODE_SYSTEM_CODE("CODE_005", "System code cannot be modified", 403),
    
    // Configuration Errors (CONFIG_)
    CONFIG_NOT_FOUND("CONFIG_001", "Configuration not found", 404),
    CONFIG_INVALID_VALUE("CONFIG_002", "Invalid configuration value", 400),
    CONFIG_LOCKED("CONFIG_003", "Configuration is locked", 423),
    CONFIG_ENCRYPTION_FAILED("CONFIG_004", "Configuration encryption failed", 500),
    
    // Business Logic Errors (BUSINESS_)
    BUSINESS_RULE_VIOLATION("BUSINESS_001", "Business rule violation", 422),
    BUSINESS_WORKFLOW_ERROR("BUSINESS_002", "Workflow error", 422),
    BUSINESS_INVALID_STATE("BUSINESS_003", "Invalid state transition", 422),
    BUSINESS_DEPENDENCY_ERROR("BUSINESS_004", "Dependency constraint violation", 422),
    BUSINESS_LIMIT_EXCEEDED("BUSINESS_005", "Business limit exceeded", 422);
    
    private final String code;
    private final String message;
    private final int httpStatus;
    
    ErrorCode(String code, String message, int httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
    
    public static ErrorCode fromCode(String code) {
        for (ErrorCode errorCode : ErrorCode.values()) {
            if (errorCode.getCode().equals(code)) {
                return errorCode;
            }
        }
        return SYSTEM_INTERNAL_ERROR;
    }
}