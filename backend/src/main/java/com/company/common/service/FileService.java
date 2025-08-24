package com.company.common.service;

import com.company.common.dto.file.FileDto;
import com.company.common.entity.FileEntity;
import com.company.common.entity.User;
import com.company.common.exception.BadRequestException;
import com.company.common.exception.ResourceNotFoundException;
import com.company.common.repository.FileRepository;
import com.company.common.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FileService {
    
    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    
    @Value("${file.upload-dir:./uploads}")
    private String uploadDir;
    
    @Value("${file.max-size:104857600}")
    private Long maxFileSize;
    
    public FileDto uploadFile(MultipartFile file, Long userId, String description, Integer expirationDays) {
        validateFile(file);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        String fileName = generateFileName(file);
        Path targetLocation = Paths.get(uploadDir).toAbsolutePath().normalize().resolve(fileName);
        
        try {
            Files.createDirectories(targetLocation.getParent());
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            FileEntity fileEntity = FileEntity.builder()
                    .fileName(fileName)
                    .originalName(file.getOriginalFilename())
                    .filePath(targetLocation.toString())
                    .fileSize(file.getSize())
                    .fileType(getFileExtension(file.getOriginalFilename()))
                    .contentType(file.getContentType())
                    .uploadedBy(user)
                    .description(description)
                    .fileHash(calculateFileHash(file))
                    .expiresAt(expirationDays != null ? LocalDateTime.now().plusDays(expirationDays) : null)
                    .build();
            
            fileEntity = fileRepository.save(fileEntity);
            log.info("File uploaded: {} by user: {}", fileName, user.getEmail());
            
            return convertToDto(fileEntity);
            
        } catch (IOException | NoSuchAlgorithmException ex) {
            throw new BadRequestException("Could not store file. Please try again!", ex);
        }
    }
    
    public Resource downloadFile(Long fileId, Long userId) {
        FileEntity fileEntity = fileRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("File not found"));
        
        // Check permissions
        if (!fileEntity.getIsPublic() && !fileEntity.getUploadedBy().getId().equals(userId)) {
            throw new BadRequestException("You don't have permission to download this file");
        }
        
        // Increment download count
        fileEntity.setDownloadCount(fileEntity.getDownloadCount() + 1);
        fileRepository.save(fileEntity);
        
        try {
            Path filePath = Paths.get(fileEntity.getFilePath()).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists()) {
                return resource;
            } else {
                throw new ResourceNotFoundException("File not found");
            }
        } catch (MalformedURLException ex) {
            throw new ResourceNotFoundException("File not found", ex);
        }
    }
    
    public void deleteFile(Long fileId, Long userId) {
        FileEntity fileEntity = fileRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("File not found"));
        
        // Check permissions
        if (!fileEntity.getUploadedBy().getId().equals(userId)) {
            throw new BadRequestException("You don't have permission to delete this file");
        }
        
        try {
            Path filePath = Paths.get(fileEntity.getFilePath()).normalize();
            Files.deleteIfExists(filePath);
            fileRepository.delete(fileEntity);
            log.info("File deleted: {} by user: {}", fileEntity.getFileName(), userId);
        } catch (IOException ex) {
            throw new BadRequestException("Could not delete file", ex);
        }
    }
    
    public List<FileDto> getUserFiles(Long userId) {
        return fileRepository.findByUploadedById(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<FileDto> getPublicFiles() {
        return fileRepository.findByIsPublicTrue().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public FileDto getFileInfo(Long fileId) {
        FileEntity fileEntity = fileRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("File not found"));
        return convertToDto(fileEntity);
    }
    
    @Transactional
    public void cleanupExpiredFiles() {
        List<FileEntity> expiredFiles = fileRepository.findExpiredFiles(LocalDateTime.now());
        for (FileEntity file : expiredFiles) {
            try {
                Path filePath = Paths.get(file.getFilePath()).normalize();
                Files.deleteIfExists(filePath);
                fileRepository.delete(file);
                log.info("Expired file deleted: {}", file.getFileName());
            } catch (IOException ex) {
                log.error("Failed to delete expired file: {}", file.getFileName(), ex);
            }
        }
    }
    
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("Failed to store empty file");
        }
        
        if (file.getSize() > maxFileSize) {
            throw new BadRequestException("File size exceeds maximum limit");
        }
        
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        if (fileName.contains("..")) {
            throw new BadRequestException("Filename contains invalid path sequence");
        }
    }
    
    private String generateFileName(MultipartFile file) {
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = getFileExtension(originalFileName);
        return UUID.randomUUID().toString() + "." + fileExtension;
    }
    
    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
    
    private String calculateFileHash(MultipartFile file) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(file.getBytes());
        byte[] digest = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    
    private FileDto convertToDto(FileEntity entity) {
        return FileDto.builder()
                .id(entity.getId())
                .fileName(entity.getFileName())
                .originalName(entity.getOriginalName())
                .fileSize(entity.getFileSize())
                .fileType(entity.getFileType())
                .contentType(entity.getContentType())
                .uploadedBy(entity.getUploadedBy().getEmail())
                .uploadedAt(entity.getCreatedAt())
                .downloadCount(entity.getDownloadCount())
                .isPublic(entity.getIsPublic())
                .expiresAt(entity.getExpiresAt())
                .description(entity.getDescription())
                .build();
    }
}