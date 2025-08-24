package com.company.common.controller;

import com.company.common.dto.SystemConfigDto;
import com.company.common.entity.SystemConfig;
import com.company.common.service.SystemConfigService;
import com.company.common.util.Auditable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/system-config")
@RequiredArgsConstructor
@Tag(name = "System Configuration", description = "APIs for managing system configuration")
public class SystemConfigController {
    
    private final SystemConfigService systemConfigService;
    
    @GetMapping
    @Operation(summary = "Get all configurations", description = "Retrieve all system configurations")
    public ResponseEntity<List<SystemConfigDto>> getAllConfigurations(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean isActive) {
        List<SystemConfigDto> configs;
        
        if (category != null) {
            configs = systemConfigService.getConfigsByCategory(category);
        } else {
            configs = systemConfigService.getAllConfigs();
        }
        
        if (isActive != null) {
            configs = configs.stream()
                    .filter(config -> config.getIsActive().equals(isActive))
                    .toList();
        }
        
        return ResponseEntity.ok(configs);
    }
    
    @GetMapping("/categories")
    @Operation(summary = "Get configuration categories", description = "Retrieve all available configuration categories")
    public ResponseEntity<List<String>> getCategories() {
        List<String> categories = systemConfigService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
    
    @GetMapping("/{key}")
    @Operation(summary = "Get configuration by key", description = "Retrieve a specific configuration by its key")
    public ResponseEntity<SystemConfigDto> getConfigByKey(@PathVariable String key) {
        SystemConfigDto config = systemConfigService.getConfigByKey(key);
        return ResponseEntity.ok(config);
    }
    
    @GetMapping("/value/{key}")
    @Operation(summary = "Get configuration value", description = "Retrieve only the value of a configuration")
    public ResponseEntity<String> getConfigValue(@PathVariable String key) {
        String value = systemConfigService.getString(key, null);
        if (value == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(value);
    }
    
    @GetMapping("/category/{category}")
    @Operation(summary = "Get configurations by category", description = "Retrieve all configurations in a specific category")
    public ResponseEntity<Map<String, String>> getConfigMapByCategory(@PathVariable String category) {
        Map<String, String> configMap = systemConfigService.getConfigMapByCategory(category);
        return ResponseEntity.ok(configMap);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(action = "CREATE_CONFIG")
    @Operation(summary = "Create configuration", description = "Create a new system configuration (Admin only)")
    public ResponseEntity<SystemConfigDto> createConfig(@Valid @RequestBody SystemConfigDto configDto) {
        SystemConfigDto created = systemConfigService.createConfig(configDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{key}")
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(action = "UPDATE_CONFIG")
    @Operation(summary = "Update configuration", description = "Update an existing configuration (Admin only)")
    public ResponseEntity<SystemConfigDto> updateConfig(
            @PathVariable String key,
            @Valid @RequestBody SystemConfigDto configDto) {
        SystemConfigDto updated = systemConfigService.updateConfig(key, configDto);
        return ResponseEntity.ok(updated);
    }
    
    @PatchMapping("/{key}/value")
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(action = "UPDATE_CONFIG_VALUE")
    @Operation(summary = "Update configuration value", description = "Update only the value of a configuration (Admin only)")
    public ResponseEntity<SystemConfigDto> updateConfigValue(
            @PathVariable String key,
            @RequestBody Map<String, String> payload) {
        String value = payload.get("value");
        SystemConfigDto updated = systemConfigService.updateConfigValue(key, value);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{key}")
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(action = "DELETE_CONFIG")
    @Operation(summary = "Delete configuration", description = "Delete a configuration (Admin only)")
    public ResponseEntity<Void> deleteConfig(@PathVariable String key) {
        systemConfigService.deleteConfig(key);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/batch")
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(action = "BATCH_UPDATE_CONFIG")
    @Operation(summary = "Batch update configurations", description = "Update multiple configurations at once (Admin only)")
    public ResponseEntity<List<SystemConfigDto>> batchUpdateConfigs(
            @RequestBody List<SystemConfigDto> configs) {
        List<SystemConfigDto> updated = systemConfigService.batchUpdateConfigs(configs);
        return ResponseEntity.ok(updated);
    }
    
    @PostMapping("/refresh")
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(action = "REFRESH_CONFIG_CACHE")
    @Operation(summary = "Refresh configuration cache", description = "Clear and reload configuration cache (Admin only)")
    public ResponseEntity<Map<String, String>> refreshCache() {
        systemConfigService.refreshCache();
        return ResponseEntity.ok(Map.of("status", "success", "message", "Configuration cache refreshed"));
    }
    
    @GetMapping("/export")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Export configurations", description = "Export all configurations in JSON format (Admin only)")
    public ResponseEntity<Map<String, Object>> exportConfigurations(
            @RequestParam(required = false) String category) {
        Map<String, Object> export = systemConfigService.exportConfigurations(category);
        return ResponseEntity.ok(export);
    }
    
    @PostMapping("/import")
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(action = "IMPORT_CONFIGS")
    @Operation(summary = "Import configurations", description = "Import configurations from JSON (Admin only)")
    public ResponseEntity<Map<String, Object>> importConfigurations(
            @RequestBody Map<String, Object> configurations,
            @RequestParam(defaultValue = "false") Boolean overwrite) {
        Map<String, Object> result = systemConfigService.importConfigurations(configurations, overwrite);
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/validate/{key}")
    @Operation(summary = "Validate configuration", description = "Validate a configuration value against its constraints")
    public ResponseEntity<Map<String, Object>> validateConfig(
            @PathVariable String key,
            @RequestParam String value) {
        boolean isValid = systemConfigService.validateConfigValue(key, value);
        Map<String, Object> result = Map.of(
                "key", key,
                "value", value,
                "isValid", isValid
        );
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/defaults")
    @Operation(summary = "Get default configurations", description = "Retrieve default configuration values")
    public ResponseEntity<Map<String, String>> getDefaultConfigurations() {
        Map<String, String> defaults = systemConfigService.getDefaultConfigurations();
        return ResponseEntity.ok(defaults);
    }
    
    @PostMapping("/reset/{key}")
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(action = "RESET_CONFIG")
    @Operation(summary = "Reset to default", description = "Reset a configuration to its default value (Admin only)")
    public ResponseEntity<SystemConfigDto> resetToDefault(@PathVariable String key) {
        SystemConfigDto reset = systemConfigService.resetToDefault(key);
        return ResponseEntity.ok(reset);
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search configurations", description = "Search configurations by key or value")
    public ResponseEntity<List<SystemConfigDto>> searchConfigurations(
            @RequestParam String query,
            @RequestParam(defaultValue = "false") Boolean searchInValues) {
        List<SystemConfigDto> results = systemConfigService.searchConfigurations(query, searchInValues);
        return ResponseEntity.ok(results);
    }
    
    @GetMapping("/types")
    @Operation(summary = "Get configuration types", description = "Retrieve all available configuration value types")
    public ResponseEntity<List<String>> getConfigTypes() {
        List<String> types = List.of(
                SystemConfig.ValueType.STRING.name(),
                SystemConfig.ValueType.INTEGER.name(),
                SystemConfig.ValueType.LONG.name(),
                SystemConfig.ValueType.DOUBLE.name(),
                SystemConfig.ValueType.BOOLEAN.name(),
                SystemConfig.ValueType.JSON.name(),
                SystemConfig.ValueType.DATE.name(),
                SystemConfig.ValueType.TIME.name(),
                SystemConfig.ValueType.DATETIME.name()
        );
        return ResponseEntity.ok(types);
    }
}