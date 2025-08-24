package com.company.common.dto.code;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeItemDto {
    
    private Long id;
    private Long groupId;
    private String code;
    private String codeName;
    private String codeValue;
    private String description;
    private Long parentId;
    private Integer sortOrder;
    private Boolean isActive;
    private Boolean isDefault;
    private String attribute1;
    private String attribute2;
    private String attribute3;
    private String attribute4;
    private String attribute5;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CodeItemDto> children;
}