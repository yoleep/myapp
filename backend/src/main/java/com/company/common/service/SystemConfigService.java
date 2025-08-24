package com.company.common.service;

import com.company.common.dto.SystemConfigDto;
import com.company.common.entity.SystemConfig;
import com.company.common.repository.SystemConfigRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SystemConfigService {
    
    private final SystemConfigRepository systemConfigRepository;
    private final ObjectMapper objectMapper;
    
    private static final String CONFIG_CACHE = "systemConfigs";
    private final Map<String, String> defaultConfigs = new ConcurrentHashMap<>();
    
    @PostConstruct
    public void initializeDefaults() {
        // Initialize default configurations
        defaultConfigs.put("app.name", "MyApp");
        defaultConfigs.put("app.version", "1.0.0");
        defaultConfigs.put("app.timezone", "UTC");
        defaultConfigs.put("app.locale", "en_US");
        defaultConfigs.put("app.date_format", "yyyy-MM-dd");
        defaultConfigs.put("app.time_format", "HH:mm:ss");
        defaultConfigs.put("app.datetime_format", "yyyy-MM-dd HH:mm:ss");
        
        defaultConfigs.put("security.jwt_expiration", "86400000");
        defaultConfigs.put("security.password_min_length", "8");
        defaultConfigs.put("security.max_login_attempts", "5");
        defaultConfigs.put("security.account_lock_duration", "1800000");
        
        defaultConfigs.put("email.enabled", "true");
        defaultConfigs.put("email.from", "noreply@myapp.com");
        defaultConfigs.put("email.max_retry", "3");
        
        defaultConfigs.put("file.max_size", "10485760");
        defaultConfigs.put("file.allowed_extensions", "pdf,doc,docx,xls,xlsx,png,jpg,jpeg,gif");
        defaultConfigs.put("file.storage_path", "/uploads");
        
        defaultConfigs.put("notification.enabled", "true");
        defaultConfigs.put("notification.batch_size", "100");
        defaultConfigs.put("notification.retention_days", "30");
        
        // Load defaults into database if not exists
        loadDefaultsToDatabase();
    }
    
    private void loadDefaultsToDatabase() {
        defaultConfigs.forEach((key, value) -> {
            if (!systemConfigRepository.existsByConfigKey(key)) {
                String category = key.substring(0, key.indexOf('.'));
                SystemConfig config = SystemConfig.builder()
                        .configKey(key)
                        .configValue(value)
                        .category(category)
                        .description("Default configuration for " + key)
                        .valueType(determineValueType(value))
                        .isActive(true)
                        .isSystem(true)
                        .build();
                systemConfigRepository.save(config);
            }
        });
    }
    
    private SystemConfig.ValueType determineValueType(String value) {
        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
            return SystemConfig.ValueType.BOOLEAN;
        }
        try {
            Integer.parseInt(value);
            return SystemConfig.ValueType.INTEGER;
        } catch (NumberFormatException e1) {
            try {
                Long.parseLong(value);
                return SystemConfig.ValueType.LONG;
            } catch (NumberFormatException e2) {
                try {
                    Double.parseDouble(value);
                    return SystemConfig.ValueType.DOUBLE;
                } catch (NumberFormatException e3) {
                    return SystemConfig.ValueType.STRING;
                }
            }
        }
    }
    
    @Cacheable(value = CONFIG_CACHE, key = "#key")
    public SystemConfigDto getConfigByKey(String key) {
        SystemConfig config = systemConfigRepository.findByConfigKey(key)
                .orElseThrow(() -> new RuntimeException("Configuration not found: " + key));
        return toDto(config);
    }
    
    public String getString(String key, String defaultValue) {
        try {
            SystemConfigDto config = getConfigByKey(key);
            return config.getIsActive() ? config.getConfigValue() : defaultValue;
        } catch (RuntimeException e) {
            return defaultValue;
        }
    }
    
    public Integer getInteger(String key, Integer defaultValue) {
        String value = getString(key, null);
        if (value == null) return defaultValue;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.error("Invalid integer value for key: {}", key);
            return defaultValue;
        }
    }
    
    public Long getLong(String key, Long defaultValue) {
        String value = getString(key, null);
        if (value == null) return defaultValue;
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            log.error("Invalid long value for key: {}", key);
            return defaultValue;
        }
    }
    
    public Double getDouble(String key, Double defaultValue) {
        String value = getString(key, null);
        if (value == null) return defaultValue;
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            log.error("Invalid double value for key: {}", key);
            return defaultValue;
        }
    }
    
    public Boolean getBoolean(String key, Boolean defaultValue) {
        String value = getString(key, null);
        if (value == null) return defaultValue;
        return Boolean.parseBoolean(value);
    }
    
    public <T> T getJson(String key, Class<T> clazz, T defaultValue) {
        String value = getString(key, null);
        if (value == null) return defaultValue;
        try {
            return objectMapper.readValue(value, clazz);
        } catch (JsonProcessingException e) {
            log.error("Invalid JSON value for key: {}", key, e);
            return defaultValue;
        }
    }
    
    public List<SystemConfigDto> getAllConfigs() {
        return systemConfigRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public List<SystemConfigDto> getConfigsByCategory(String category) {
        return systemConfigRepository.findByCategory(category).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public Map<String, String> getConfigMapByCategory(String category) {
        return systemConfigRepository.findByCategory(category).stream()
                .filter(SystemConfig::getIsActive)
                .collect(Collectors.toMap(
                        SystemConfig::getConfigKey,
                        SystemConfig::getConfigValue
                ));
    }
    
    public List<String> getAllCategories() {
        return systemConfigRepository.findDistinctCategories();
    }
    
    @Transactional
    @CacheEvict(value = CONFIG_CACHE, key = "#configDto.configKey")
    public SystemConfigDto createConfig(SystemConfigDto configDto) {
        if (systemConfigRepository.existsByConfigKey(configDto.getConfigKey())) {
            throw new RuntimeException("Configuration already exists: " + configDto.getConfigKey());
        }
        
        SystemConfig config = toEntity(configDto);
        config = systemConfigRepository.save(config);
        log.info("Created configuration: {}", config.getConfigKey());
        return toDto(config);
    }
    
    @Transactional
    @CacheEvict(value = CONFIG_CACHE, key = "#key")
    public SystemConfigDto updateConfig(String key, SystemConfigDto configDto) {
        SystemConfig config = systemConfigRepository.findByConfigKey(key)
                .orElseThrow(() -> new RuntimeException("Configuration not found: " + key));
        
        config.setConfigValue(configDto.getConfigValue());
        config.setDescription(configDto.getDescription());
        config.setCategory(configDto.getCategory());
        config.setValueType(configDto.getValueType());
        config.setIsActive(configDto.getIsActive());
        config.setIsEncrypted(configDto.getIsEncrypted());
        
        config = systemConfigRepository.save(config);
        log.info("Updated configuration: {}", key);
        return toDto(config);
    }
    
    @Transactional
    @CacheEvict(value = CONFIG_CACHE, key = "#key")
    public SystemConfigDto updateConfigValue(String key, String value) {
        SystemConfig config = systemConfigRepository.findByConfigKey(key)
                .orElseThrow(() -> new RuntimeException("Configuration not found: " + key));
        
        config.setConfigValue(value);
        config = systemConfigRepository.save(config);
        log.info("Updated configuration value for: {}", key);
        return toDto(config);
    }
    
    @Transactional
    @CacheEvict(value = CONFIG_CACHE, key = "#key")
    public void deleteConfig(String key) {
        SystemConfig config = systemConfigRepository.findByConfigKey(key)
                .orElseThrow(() -> new RuntimeException("Configuration not found: " + key));
        
        if (config.getIsSystem()) {
            throw new RuntimeException("Cannot delete system configuration: " + key);
        }
        
        systemConfigRepository.delete(config);
        log.info("Deleted configuration: {}", key);
    }
    
    @Transactional
    @CacheEvict(value = CONFIG_CACHE, allEntries = true)
    public List<SystemConfigDto> batchUpdateConfigs(List<SystemConfigDto> configs) {
        List<SystemConfigDto> updated = new ArrayList<>();
        
        for (SystemConfigDto configDto : configs) {
            try {
                SystemConfig config = systemConfigRepository.findByConfigKey(configDto.getConfigKey())
                        .orElse(toEntity(configDto));
                
                config.setConfigValue(configDto.getConfigValue());
                config.setIsActive(configDto.getIsActive());
                
                config = systemConfigRepository.save(config);
                updated.add(toDto(config));
            } catch (Exception e) {
                log.error("Failed to update configuration: {}", configDto.getConfigKey(), e);
            }
        }
        
        log.info("Batch updated {} configurations", updated.size());
        return updated;
    }
    
    @CacheEvict(value = CONFIG_CACHE, allEntries = true)
    public void refreshCache() {
        log.info("Refreshing system configuration cache");
    }
    
    public Map<String, Object> exportConfigurations(String category) {
        List<SystemConfig> configs;
        if (category != null) {
            configs = systemConfigRepository.findByCategory(category);
        } else {
            configs = systemConfigRepository.findAll();
        }
        
        Map<String, Object> export = new HashMap<>();
        export.put("timestamp", LocalDateTime.now().toString());
        export.put("count", configs.size());
        export.put("configurations", configs.stream()
                .map(this::toDto)
                .collect(Collectors.toList()));
        
        return export;
    }
    
    @Transactional
    @CacheEvict(value = CONFIG_CACHE, allEntries = true)
    public Map<String, Object> importConfigurations(Map<String, Object> configurations, Boolean overwrite) {
        List<Map<String, Object>> configList = (List<Map<String, Object>>) configurations.get("configurations");
        
        int imported = 0;
        int skipped = 0;
        int failed = 0;
        
        for (Map<String, Object> configMap : configList) {
            try {
                String key = (String) configMap.get("configKey");
                
                if (!overwrite && systemConfigRepository.existsByConfigKey(key)) {
                    skipped++;
                    continue;
                }
                
                SystemConfigDto dto = objectMapper.convertValue(configMap, SystemConfigDto.class);
                if (overwrite && systemConfigRepository.existsByConfigKey(key)) {
                    updateConfig(key, dto);
                } else {
                    createConfig(dto);
                }
                imported++;
            } catch (Exception e) {
                log.error("Failed to import configuration", e);
                failed++;
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("imported", imported);
        result.put("skipped", skipped);
        result.put("failed", failed);
        
        log.info("Import completed - Imported: {}, Skipped: {}, Failed: {}", imported, skipped, failed);
        return result;
    }
    
    public boolean validateConfigValue(String key, String value) {
        try {
            SystemConfig config = systemConfigRepository.findByConfigKey(key)
                    .orElseThrow(() -> new RuntimeException("Configuration not found: " + key));
            
            return validateValueType(value, config.getValueType());
        } catch (Exception e) {
            return false;
        }
    }
    
    private boolean validateValueType(String value, SystemConfig.ValueType type) {
        if (value == null) return false;
        
        try {
            switch (type) {
                case INTEGER:
                    Integer.parseInt(value);
                    return true;
                case LONG:
                    Long.parseLong(value);
                    return true;
                case DOUBLE:
                    Double.parseDouble(value);
                    return true;
                case BOOLEAN:
                    return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false");
                case DATE:
                    LocalDate.parse(value, DateTimeFormatter.ISO_DATE);
                    return true;
                case TIME:
                    LocalTime.parse(value, DateTimeFormatter.ISO_TIME);
                    return true;
                case DATETIME:
                    LocalDateTime.parse(value, DateTimeFormatter.ISO_DATE_TIME);
                    return true;
                case JSON:
                    objectMapper.readTree(value);
                    return true;
                case STRING:
                default:
                    return true;
            }
        } catch (Exception e) {
            return false;
        }
    }
    
    public Map<String, String> getDefaultConfigurations() {
        return new HashMap<>(defaultConfigs);
    }
    
    @Transactional
    @CacheEvict(value = CONFIG_CACHE, key = "#key")
    public SystemConfigDto resetToDefault(String key) {
        String defaultValue = defaultConfigs.get(key);
        if (defaultValue == null) {
            throw new RuntimeException("No default value for configuration: " + key);
        }
        
        return updateConfigValue(key, defaultValue);
    }
    
    public List<SystemConfigDto> searchConfigurations(String query, Boolean searchInValues) {
        List<SystemConfig> configs;
        
        if (searchInValues) {
            configs = systemConfigRepository.findByConfigKeyContainingOrConfigValueContaining(query, query);
        } else {
            configs = systemConfigRepository.findByConfigKeyContaining(query);
        }
        
        return configs.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    private SystemConfigDto toDto(SystemConfig entity) {
        return SystemConfigDto.builder()
                .id(entity.getId())
                .configKey(entity.getConfigKey())
                .configValue(entity.getConfigValue())
                .description(entity.getDescription())
                .category(entity.getCategory())
                .valueType(entity.getValueType())
                .isActive(entity.getIsActive())
                .isSystem(entity.getIsSystem())
                .isEncrypted(entity.getIsEncrypted())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
    
    private SystemConfig toEntity(SystemConfigDto dto) {
        return SystemConfig.builder()
                .configKey(dto.getConfigKey())
                .configValue(dto.getConfigValue())
                .description(dto.getDescription())
                .category(dto.getCategory())
                .valueType(dto.getValueType())
                .isActive(dto.getIsActive())
                .isSystem(dto.getIsSystem() != null ? dto.getIsSystem() : false)
                .isEncrypted(dto.getIsEncrypted() != null ? dto.getIsEncrypted() : false)
                .build();
    }
}