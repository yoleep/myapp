package com.company.common.dto.board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostUpdateRequest {
    
    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 200, message = "Title must be between 1 and 200 characters")
    private String title;
    
    @NotBlank(message = "Content is required")
    private String content;
    
    private String postType;
    
    private Boolean isPinned;
    
    private Boolean isSecret;
    
    private String secretPassword;
    
    private Boolean allowComments;
    
    private List<Long> attachmentIds;
    
    private List<String> tags;
    
    private String category;
    
    private String metadata;
    
    private String updateReason; // For audit purposes
}