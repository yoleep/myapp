package com.company.common.dto.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileDto {
    
    private Long id;
    private String fileName;
    private String originalName;
    private Long fileSize;
    private String fileType;
    private String contentType;
    private String uploadedBy;
    private LocalDateTime uploadedAt;
    private Long downloadCount;
    private Boolean isPublic;
    private LocalDateTime expiresAt;
    private String description;
}