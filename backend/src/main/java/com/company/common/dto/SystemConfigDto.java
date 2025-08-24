package com.company.common.dto;

import com.company.common.entity.SystemConfig;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemConfigDto {
    
    private Long id;
    
    @NotBlank(message = "Configuration key is required")
    private String configKey;
    
    @NotBlank(message = "Configuration value is required")
    private String configValue;
    
    private String description;
    
    @NotBlank(message = "Category is required")
    private String category;
    
    @NotNull(message = "Value type is required")
    private SystemConfig.ValueType valueType;
    
    private Boolean isActive = true;
    
    private Boolean isSystem = false;
    
    private Boolean isEncrypted = false;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}