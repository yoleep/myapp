package com.company.common.controller;

import com.company.common.dto.ApiResponse;
import com.company.common.dto.file.FileDto;
import com.company.common.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
@Tag(name = "File Management", description = "File management APIs")
@SecurityRequirement(name = "bearerAuth")
public class FileController {
    
    private final FileService fileService;
    
    @PostMapping("/upload")
    @Operation(summary = "Upload file", description = "Upload a new file")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<FileDto>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "expirationDays", required = false) Integer expirationDays,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        // In production, get user ID from userDetails
        Long userId = 1L; // Placeholder
        
        FileDto fileDto = fileService.uploadFile(file, userId, description, expirationDays);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(fileDto, "File uploaded successfully"));
    }
    
    @GetMapping("/{fileId}/download")
    @Operation(summary = "Download file", description = "Download a file by ID")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable Long fileId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Long userId = 1L; // Placeholder
        Resource resource = fileService.downloadFile(fileId, userId);
        
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    
    @GetMapping("/{fileId}")
    @Operation(summary = "Get file info", description = "Get file information by ID")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<FileDto>> getFileInfo(@PathVariable Long fileId) {
        FileDto fileDto = fileService.getFileInfo(fileId);
        return ResponseEntity.ok(ApiResponse.success(fileDto));
    }
    
    @DeleteMapping("/{fileId}")
    @Operation(summary = "Delete file", description = "Delete a file by ID")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<String>> deleteFile(
            @PathVariable Long fileId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Long userId = 1L; // Placeholder
        fileService.deleteFile(fileId, userId);
        return ResponseEntity.ok(ApiResponse.success("File deleted successfully"));
    }
    
    @GetMapping("/my-files")
    @Operation(summary = "Get my files", description = "Get all files uploaded by current user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<FileDto>>> getMyFiles(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Long userId = 1L; // Placeholder
        List<FileDto> files = fileService.getUserFiles(userId);
        return ResponseEntity.ok(ApiResponse.success(files));
    }
    
    @GetMapping("/public")
    @Operation(summary = "Get public files", description = "Get all public files")
    public ResponseEntity<ApiResponse<List<FileDto>>> getPublicFiles() {
        List<FileDto> files = fileService.getPublicFiles();
        return ResponseEntity.ok(ApiResponse.success(files));
    }
}