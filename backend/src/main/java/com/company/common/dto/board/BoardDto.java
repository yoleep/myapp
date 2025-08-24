package com.company.common.dto.board;

import com.company.common.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {
    
    private Long id;
    private Board.BoardType boardType;
    private String boardName;
    private String description;
    private Boolean useComment;
    private Boolean useAttachment;
    private Boolean useSecret;
    private Boolean useReply;
    private Boolean useCategory;
    private Integer attachmentLimit;
    private Long attachmentSizeLimit;
    private String allowedExtensions;
    private Boolean isActive;
    private Boolean isPublic;
    private Integer sortOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}