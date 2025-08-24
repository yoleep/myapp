package com.company.common.dto.board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class PostCreateRequest {
    
    @NotNull(message = "Board ID is required")
    private Long boardId;
    
    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 200, message = "Title must be between 1 and 200 characters")
    private String title;
    
    @NotBlank(message = "Content is required")
    private String content;
    
    private String postType; // NORMAL, NOTICE, IMPORTANT, etc.
    
    private Boolean isPinned = false;
    
    private Boolean isSecret = false;
    
    private String secretPassword; // For secret posts
    
    private Boolean allowComments = true;
    
    private List<Long> attachmentIds; // File IDs from file upload
    
    private List<String> tags;
    
    private String category;
    
    private Long parentPostId; // For reply posts
    
    private Boolean isAnonymous = false;
    
    private String metadata; // JSON string for additional data
}