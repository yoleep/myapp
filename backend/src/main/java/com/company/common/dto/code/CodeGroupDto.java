package com.company.common.dto.code;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeGroupDto {
    
    private Long id;
    private String groupCode;
    private String groupName;
    private String description;
    private Integer sortOrder;
    private Boolean isActive;
    private Boolean isSystem;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CodeItemDto> codeItems = new ArrayList<>();
}