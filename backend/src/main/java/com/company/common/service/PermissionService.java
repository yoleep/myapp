package com.company.common.service;

import com.company.common.dto.PermissionDto;
import com.company.common.entity.Permission;
import com.company.common.entity.Role;
import com.company.common.entity.User;
import com.company.common.repository.PermissionRepository;
import com.company.common.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PermissionService {
    
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    
    private static final String PERMISSION_CACHE = "permissions";
    private static final String USER_PERMISSION_CACHE = "userPermissions";
    
    @Cacheable(value = PERMISSION_CACHE)
    public List<PermissionDto> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    @Cacheable(value = PERMISSION_CACHE, key = "#permissionId")
    public PermissionDto getPermissionById(Long permissionId) {
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found: " + permissionId));
        return toDto(permission);
    }
    
    public List<PermissionDto> getPermissionsByResource(String resource) {
        return permissionRepository.findByResource(resource).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    @Transactional
    @CacheEvict(value = {PERMISSION_CACHE, USER_PERMISSION_CACHE}, allEntries = true)
    public PermissionDto createPermission(PermissionDto permissionDto) {
        // Check for duplicate
        if (permissionRepository.existsByResourceAndAction(
                permissionDto.getResource(), permissionDto.getAction())) {
            throw new RuntimeException("Permission already exists for resource: " + 
                    permissionDto.getResource() + " and action: " + permissionDto.getAction());
        }
        
        Permission permission = Permission.builder()
                .name(permissionDto.getName())
                .description(permissionDto.getDescription())
                .resource(permissionDto.getResource())
                .action(permissionDto.getAction())
                .build();
        
        permission = permissionRepository.save(permission);
        log.info("Created permission: {}", permission.getName());
        return toDto(permission);
    }
    
    @Transactional
    @CacheEvict(value = {PERMISSION_CACHE, USER_PERMISSION_CACHE}, allEntries = true)
    public PermissionDto updatePermission(Long permissionId, PermissionDto permissionDto) {
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found: " + permissionId));
        
        permission.setName(permissionDto.getName());
        permission.setDescription(permissionDto.getDescription());
        permission.setResource(permissionDto.getResource());
        permission.setAction(permissionDto.getAction());
        
        permission = permissionRepository.save(permission);
        log.info("Updated permission: {}", permission.getName());
        return toDto(permission);
    }
    
    @Transactional
    @CacheEvict(value = {PERMISSION_CACHE, USER_PERMISSION_CACHE}, allEntries = true)
    public void deletePermission(Long permissionId) {
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found: " + permissionId));
        
        permissionRepository.delete(permission);
        log.info("Deleted permission: {}", permission.getName());
    }
    
    @Cacheable(value = USER_PERMISSION_CACHE, key = "#userId")
    public List<PermissionDto> getUserPermissions(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        
        Set<Permission> permissions = new HashSet<>();
        
        // Collect permissions from all user roles
        for (Role role : user.getRoles()) {
            permissions.addAll(role.getPermissions());
        }
        
        return permissions.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public List<PermissionDto> getEffectiveUserPermissions(Long userId) {
        List<PermissionDto> rolePermissions = getUserPermissions(userId);
        
        // In a real implementation, you might have user-specific permission overrides
        // For now, we just return role-based permissions
        return rolePermissions;
    }
    
    public boolean hasPermission(Long userId, String resource, String action) {
        List<PermissionDto> permissions = getUserPermissions(userId);
        
        return permissions.stream()
                .anyMatch(p -> p.getResource().equals(resource) && p.getAction().equals(action));
    }
    
    public Map<String, Boolean> batchCheckPermissions(Long userId, List<Map<String, String>> permissionChecks) {
        List<PermissionDto> userPermissions = getUserPermissions(userId);
        Map<String, Boolean> results = new HashMap<>();
        
        for (Map<String, String> check : permissionChecks) {
            String resource = check.get("resource");
            String action = check.get("action");
            String key = resource + ":" + action;
            
            boolean hasPermission = userPermissions.stream()
                    .anyMatch(p -> p.getResource().equals(resource) && p.getAction().equals(action));
            
            results.put(key, hasPermission);
        }
        
        return results;
    }
    
    public List<String> getAllResources() {
        return permissionRepository.findDistinctResources();
    }
    
    public List<String> getAllActions() {
        return List.of(
                "VIEW",
                "CREATE",
                "UPDATE",
                "DELETE",
                "EXECUTE",
                "APPROVE",
                "REJECT",
                "EXPORT",
                "IMPORT",
                "MANAGE"
        );
    }
    
    @Transactional
    @CacheEvict(value = PERMISSION_CACHE, allEntries = true)
    public List<PermissionDto> createFromTemplate(String templateName, String resource) {
        List<PermissionDto> created = new ArrayList<>();
        
        List<String> actions = switch (templateName.toUpperCase()) {
            case "CRUD" -> List.of("CREATE", "VIEW", "UPDATE", "DELETE");
            case "READ_ONLY" -> List.of("VIEW");
            case "ADMIN" -> List.of("CREATE", "VIEW", "UPDATE", "DELETE", "MANAGE", "EXPORT", "IMPORT");
            case "MODERATOR" -> List.of("VIEW", "UPDATE", "DELETE", "APPROVE", "REJECT");
            default -> throw new RuntimeException("Unknown template: " + templateName);
        };
        
        for (String action : actions) {
            PermissionDto dto = PermissionDto.builder()
                    .name(resource.toUpperCase() + "_" + action)
                    .description(action + " permission for " + resource)
                    .resource(resource)
                    .action(action)
                    .isActive(true)
                    .build();
            
            created.add(createPermission(dto));
        }
        
        log.info("Created {} permissions from template {} for resource {}", 
                created.size(), templateName, resource);
        
        return created;
    }
    
    @CacheEvict(value = {PERMISSION_CACHE, USER_PERMISSION_CACHE}, allEntries = true)
    public void refreshPermissionCache() {
        log.info("Refreshing permission cache");
    }
    
    private PermissionDto toDto(Permission permission) {
        return PermissionDto.builder()
                .id(permission.getId())
                .name(permission.getName())
                .description(permission.getDescription())
                .resource(permission.getResource())
                .action(permission.getAction())
                .isActive(true)
                .build();
    }
}