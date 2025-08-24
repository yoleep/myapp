package com.company.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {
    
    private static final String REQUEST_ID = "X-Request-ID";
    private static final String START_TIME = "startTime";
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Generate and set request ID
        String requestId = UUID.randomUUID().toString();
        request.setAttribute(REQUEST_ID, requestId);
        response.setHeader(REQUEST_ID, requestId);
        
        // Record start time
        long startTime = System.currentTimeMillis();
        request.setAttribute(START_TIME, startTime);
        
        // Log request details
        log.info("REQUEST [{}] {} {} - IP: {}, User-Agent: {}",
                requestId,
                request.getMethod(),
                request.getRequestURI(),
                getClientIP(request),
                request.getHeader("User-Agent"));
        
        // Log query parameters if present
        if (request.getQueryString() != null) {
            log.debug("REQUEST [{}] Query: {}", requestId, request.getQueryString());
        }
        
        return true;
    }
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, 
                          Object handler, ModelAndView modelAndView) {
        // Additional processing after handler execution
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
                               Object handler, Exception ex) {
        String requestId = (String) request.getAttribute(REQUEST_ID);
        Long startTime = (Long) request.getAttribute(START_TIME);
        
        if (startTime != null) {
            long duration = System.currentTimeMillis() - startTime;
            
            if (ex != null) {
                log.error("RESPONSE [{}] {} {} - Status: {}, Duration: {}ms, Error: {}",
                        requestId,
                        request.getMethod(),
                        request.getRequestURI(),
                        response.getStatus(),
                        duration,
                        ex.getMessage());
            } else {
                log.info("RESPONSE [{}] {} {} - Status: {}, Duration: {}ms",
                        requestId,
                        request.getMethod(),
                        request.getRequestURI(),
                        response.getStatus(),
                        duration);
                
                // Log slow requests
                if (duration > 1000) {
                    log.warn("SLOW REQUEST [{}] {} {} - Duration: {}ms",
                            requestId,
                            request.getMethod(),
                            request.getRequestURI(),
                            duration);
                }
            }
        }
    }
    
    private String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIP = request.getHeader("X-Real-IP");
        if (xRealIP != null && !xRealIP.isEmpty()) {
            return xRealIP;
        }
        
        return request.getRemoteAddr();
    }
}