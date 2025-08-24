package com.company.common.dto.board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardCreateRequest {
    
    @NotBlank(message = "Board name is required")
    @Size(min = 2, max = 100, message = "Board name must be between 2 and 100 characters")
    private String name;
    
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
    
    @NotNull(message = "Board type is required")
    private String boardType; // NOTICE, FAQ, QNA, FREE, etc.
    
    private Boolean isActive = true;
    
    private Boolean isPublic = true;
    
    private Boolean allowComments = true;
    
    private Boolean allowAttachments = true;
    
    private Long maxAttachmentSize = 10485760L; // 10MB default
    
    private Integer maxAttachmentCount = 5;
    
    private Boolean requireAuth = false;
    
    private Integer sortOrder = 0;
    
    private String allowedRoles; // Comma-separated role names
    
    private String moderatorIds; // Comma-separated user IDs
}