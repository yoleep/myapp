package com.company.common.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {
    
    private Long id;
    
    @NotBlank(message = "Role name is required")
    private String name;
    
    private String description;
    
    private Boolean isActive;
    
    private Set<PermissionDto> permissions;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}