package com.company.common.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionDto {
    
    private Long id;
    
    @NotBlank(message = "Permission name is required")
    private String name;
    
    private String description;
    
    private String resource;
    
    private String action;
    
    private Boolean isActive;
}