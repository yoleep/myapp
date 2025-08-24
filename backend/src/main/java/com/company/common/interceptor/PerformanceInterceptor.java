package com.company.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Component
public class PerformanceInterceptor implements HandlerInterceptor {
    
    private static final String THREAD_CPU_TIME = "threadCpuTime";
    private static final String THREAD_USER_TIME = "threadUserTime";
    private static final String START_MEMORY = "startMemory";
    
    private final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
    private final MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
    
    // Metrics storage
    private final ConcurrentHashMap<String, AtomicLong> requestCounts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicLong> totalResponseTimes = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicLong> maxResponseTimes = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicLong> minResponseTimes = new ConcurrentHashMap<>();
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod) {
            // Record CPU time
            long threadId = Thread.currentThread().getId();
            request.setAttribute(THREAD_CPU_TIME, threadMXBean.getThreadCpuTime(threadId));
            request.setAttribute(THREAD_USER_TIME, threadMXBean.getThreadUserTime(threadId));
            
            // Record memory usage
            long usedMemory = memoryMXBean.getHeapMemoryUsage().getUsed();
            request.setAttribute(START_MEMORY, usedMemory);
        }
        
        return true;
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
                               Object handler, Exception ex) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            String methodName = handlerMethod.getMethod().getName();
            String className = handlerMethod.getBeanType().getSimpleName();
            String endpoint = className + "." + methodName;
            
            // Calculate CPU time
            Long startCpuTime = (Long) request.getAttribute(THREAD_CPU_TIME);
            Long startUserTime = (Long) request.getAttribute(THREAD_USER_TIME);
            
            if (startCpuTime != null && startUserTime != null) {
                long threadId = Thread.currentThread().getId();
                long cpuTime = (threadMXBean.getThreadCpuTime(threadId) - startCpuTime) / 1_000_000; // Convert to ms
                long userTime = (threadMXBean.getThreadUserTime(threadId) - startUserTime) / 1_000_000;
                
                // Calculate memory usage
                Long startMemory = (Long) request.getAttribute(START_MEMORY);
                long memoryUsed = 0;
                if (startMemory != null) {
                    long currentMemory = memoryMXBean.getHeapMemoryUsage().getUsed();
                    memoryUsed = (currentMemory - startMemory) / 1_048_576; // Convert to MB
                }
                
                // Update metrics
                String metricsKey = request.getMethod() + ":" + request.getRequestURI();
                updateMetrics(metricsKey, cpuTime);
                
                // Log performance data
                if (cpuTime > 100) { // Log if CPU time > 100ms
                    log.warn("PERFORMANCE [{}] {} - CPU: {}ms, User: {}ms, Memory: {}MB",
                            endpoint,
                            metricsKey,
                            cpuTime,
                            userTime,
                            memoryUsed);
                } else {
                    log.debug("PERFORMANCE [{}] {} - CPU: {}ms, User: {}ms, Memory: {}MB",
                            endpoint,
                            metricsKey,
                            cpuTime,
                            userTime,
                            memoryUsed);
                }
                
                // Log metrics summary periodically
                logMetricsSummary(metricsKey);
            }
        }
    }
    
    private void updateMetrics(String key, long responseTime) {
        // Update request count
        requestCounts.computeIfAbsent(key, k -> new AtomicLong(0)).incrementAndGet();
        
        // Update total response time
        totalResponseTimes.computeIfAbsent(key, k -> new AtomicLong(0)).addAndGet(responseTime);
        
        // Update max response time
        maxResponseTimes.compute(key, (k, v) -> {
            if (v == null) {
                return new AtomicLong(responseTime);
            }
            long current = v.get();
            if (responseTime > current) {
                v.set(responseTime);
            }
            return v;
        });
        
        // Update min response time
        minResponseTimes.compute(key, (k, v) -> {
            if (v == null) {
                return new AtomicLong(responseTime);
            }
            long current = v.get();
            if (responseTime < current) {
                v.set(responseTime);
            }
            return v;
        });
    }
    
    private void logMetricsSummary(String key) {
        long count = requestCounts.get(key).get();
        
        // Log summary every 100 requests
        if (count % 100 == 0) {
            long total = totalResponseTimes.get(key).get();
            long avg = total / count;
            long max = maxResponseTimes.get(key).get();
            long min = minResponseTimes.get(key).get();
            
            log.info("METRICS SUMMARY [{}] - Requests: {}, Avg: {}ms, Min: {}ms, Max: {}ms",
                    key, count, avg, min, max);
        }
    }
    
    public void resetMetrics() {
        requestCounts.clear();
        totalResponseTimes.clear();
        maxResponseTimes.clear();
        minResponseTimes.clear();
        log.info("Performance metrics reset");
    }
    
    public ConcurrentHashMap<String, AtomicLong> getRequestCounts() {
        return new ConcurrentHashMap<>(requestCounts);
    }
    
    public ConcurrentHashMap<String, AtomicLong> getAverageResponseTimes() {
        ConcurrentHashMap<String, AtomicLong> averages = new ConcurrentHashMap<>();
        requestCounts.forEach((key, count) -> {
            long total = totalResponseTimes.get(key).get();
            averages.put(key, new AtomicLong(total / count.get()));
        });
        return averages;
    }
}