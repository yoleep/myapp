package com.company.common.service;

import com.company.common.entity.AuditLog;
import com.company.common.repository.AuditLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.Arrays;

@Service
@Aspect
@RequiredArgsConstructor
@Slf4j
public class AuditLogService {
    
    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;
    
    @Async
    public void logAction(String action, String entityType, String entityId, Object oldValue, Object newValue) {
        try {
            HttpServletRequest request = getCurrentRequest();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            AuditLog auditLog = AuditLog.builder()
                    .action(action)
                    .entityType(entityType)
                    .entityId(entityId)
                    .oldValue(oldValue != null ? objectMapper.writeValueAsString(oldValue) : null)
                    .newValue(newValue != null ? objectMapper.writeValueAsString(newValue) : null)
                    .build();
            
            if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                auditLog.setUserEmail(userDetails.getUsername());
                // Set userId if available
            }
            
            if (request != null) {
                auditLog.setIpAddress(getClientIpAddress(request));
                auditLog.setUserAgent(request.getHeader("User-Agent"));
                auditLog.setRequestMethod(request.getMethod());
                auditLog.setRequestUrl(request.getRequestURI());
            }
            
            auditLogRepository.save(auditLog);
            
        } catch (Exception e) {
            log.error("Failed to save audit log", e);
        }
    }
    
    @Around("@annotation(Auditable)")
    public Object auditMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();
        
        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;
            
            // Log successful execution
            logMethodExecution(className, methodName, args, result, executionTime, true);
            
            return result;
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            
            // Log failed execution
            logMethodExecution(className, methodName, args, e.getMessage(), executionTime, false);
            
            throw e;
        }
    }
    
    @Async
    private void logMethodExecution(String className, String methodName, Object[] args, Object result, 
                                    long executionTime, boolean success) {
        try {
            HttpServletRequest request = getCurrentRequest();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            AuditLog auditLog = AuditLog.builder()
                    .action(className + "." + methodName)
                    .entityType(className)
                    .newValue(success ? "SUCCESS" : "FAILURE")
                    .executionTime(executionTime)
                    .build();
            
            if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                auditLog.setUserEmail(userDetails.getUsername());
            }
            
            if (request != null) {
                auditLog.setIpAddress(getClientIpAddress(request));
                auditLog.setUserAgent(request.getHeader("User-Agent"));
                auditLog.setRequestMethod(request.getMethod());
                auditLog.setRequestUrl(request.getRequestURI());
                auditLog.setResponseStatus(success ? 200 : 500);
            }
            
            auditLogRepository.save(auditLog);
            
            if (log.isDebugEnabled()) {
                log.debug("Audit log saved for {}.{} - Execution time: {}ms", 
                         className, methodName, executionTime);
            }
            
        } catch (Exception e) {
            log.error("Failed to save audit log for method execution", e);
        }
    }
    
    private HttpServletRequest getCurrentRequest() {
        try {
            ServletRequestAttributes attributes = 
                (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            return attributes.getRequest();
        } catch (Exception e) {
            return null;
        }
    }
    
    private String getClientIpAddress(HttpServletRequest request) {
        String[] headers = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
        };
        
        for (String header : headers) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip.split(",")[0];
            }
        }
        
        return request.getRemoteAddr();
    }
    
    public void logLogin(String userEmail, String ipAddress, boolean success) {
        AuditLog auditLog = AuditLog.builder()
                .userEmail(userEmail)
                .action(success ? "LOGIN_SUCCESS" : "LOGIN_FAILURE")
                .entityType("Authentication")
                .ipAddress(ipAddress)
                .build();
        
        auditLogRepository.save(auditLog);
    }
    
    public void logLogout(String userEmail, String ipAddress) {
        AuditLog auditLog = AuditLog.builder()
                .userEmail(userEmail)
                .action("LOGOUT")
                .entityType("Authentication")
                .ipAddress(ipAddress)
                .build();
        
        auditLogRepository.save(auditLog);
    }
}