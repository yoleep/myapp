package com.company.common.dto.board;

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
public class CommentDto {
    
    private Long id;
    private Long postId;
    private Long authorId;
    private String authorName;
    private String content;
    private Long parentId;
    private Integer level;
    private Long likeCount;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private List<CommentDto> children;
}