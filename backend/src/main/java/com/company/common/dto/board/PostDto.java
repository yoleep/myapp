package com.company.common.dto.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    
    private Long id;
    private Long boardId;
    private String boardName;
    private Long authorId;
    private String authorName;
    private String title;
    private String content;
    private String category;
    private Long viewCount;
    private Long likeCount;
    private Long commentCount;
    private List<String> tags;
    private Boolean isNotice;
    private Boolean isPinned;
    private Boolean isSecret;
    private String postType;
    private Boolean allowComments;
    private Boolean isAnonymous;
    private String metadata;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}