package com.company.common.service;

import com.company.common.dto.RoleDto;
import com.company.common.dto.PermissionDto;
import com.company.common.entity.Role;
import com.company.common.entity.Permission;
import com.company.common.entity.User;
import com.company.common.repository.RoleRepository;
import com.company.common.repository.PermissionRepository;
import com.company.common.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleService {
    
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    
    private static final String ROLE_CACHE = "roles";
    private static final String PERMISSION_CACHE = "permissions";
    
    @Cacheable(value = ROLE_CACHE)
    public List<RoleDto> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    @Cacheable(value = ROLE_CACHE, key = "#roleId")
    public RoleDto getRoleById(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleId));
        return toDto(role);
    }
    
    @Transactional
    @CacheEvict(value = ROLE_CACHE, allEntries = true)
    public RoleDto createRole(RoleDto roleDto) {
        if (roleRepository.findByName(roleDto.getName()).isPresent()) {
            throw new RuntimeException("Role already exists: " + roleDto.getName());
        }
        
        Role role = Role.builder()
                .name(roleDto.getName())
                .description(roleDto.getDescription())
                .isActive(true)
                .build();
        
        role = roleRepository.save(role);
        log.info("Created role: {}", role.getName());
        return toDto(role);
    }
    
    @Transactional
    @CacheEvict(value = ROLE_CACHE, allEntries = true)
    public RoleDto updateRole(Long roleId, RoleDto roleDto) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleId));
        
        role.setDescription(roleDto.getDescription());
        role.setIsActive(roleDto.getIsActive());
        
        role = roleRepository.save(role);
        log.info("Updated role: {}", role.getName());
        return toDto(role);
    }
    
    @Transactional
    @CacheEvict(value = {ROLE_CACHE, PERMISSION_CACHE}, allEntries = true)
    public void deleteRole(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleId));
        
        // Check if role is system role
        if (role.getName().startsWith("ROLE_ADMIN") || role.getName().startsWith("ROLE_USER")) {
            throw new RuntimeException("Cannot delete system role: " + role.getName());
        }
        
        // Remove role from all users
        List<User> users = userRepository.findByRolesContaining(role);
        for (User user : users) {
            user.getRoles().remove(role);
            userRepository.save(user);
        }
        
        roleRepository.delete(role);
        log.info("Deleted role: {}", role.getName());
    }
    
    @Cacheable(value = PERMISSION_CACHE, key = "#roleId")
    public Set<PermissionDto> getRolePermissions(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleId));
        
        return role.getPermissions().stream()
                .map(this::toPermissionDto)
                .collect(Collectors.toSet());
    }
    
    @Transactional
    @CacheEvict(value = {ROLE_CACHE, PERMISSION_CACHE}, allEntries = true)
    public RoleDto assignPermissions(Long roleId, List<Long> permissionIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleId));
        
        Set<Permission> permissions = new HashSet<>();
        for (Long permissionId : permissionIds) {
            Permission permission = permissionRepository.findById(permissionId)
                    .orElseThrow(() -> new RuntimeException("Permission not found: " + permissionId));
            permissions.add(permission);
        }
        
        role.setPermissions(permissions);
        role = roleRepository.save(role);
        
        log.info("Assigned {} permissions to role: {}", permissions.size(), role.getName());
        return toDto(role);
    }
    
    @Transactional
    @CacheEvict(value = {ROLE_CACHE, PERMISSION_CACHE}, allEntries = true)
    public void revokePermission(Long roleId, Long permissionId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleId));
        
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found: " + permissionId));
        
        role.getPermissions().remove(permission);
        roleRepository.save(role);
        
        log.info("Revoked permission {} from role: {}", permission.getName(), role.getName());
    }
    
    @Transactional
    @CacheEvict(value = ROLE_CACHE, allEntries = true)
    public void assignRoleToUser(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleId));
        
        user.getRoles().add(role);
        userRepository.save(user);
        
        log.info("Assigned role {} to user: {}", role.getName(), user.getEmail());
    }
    
    @Transactional
    @CacheEvict(value = ROLE_CACHE, allEntries = true)
    public void revokeRoleFromUser(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleId));
        
        user.getRoles().remove(role);
        userRepository.save(user);
        
        log.info("Revoked role {} from user: {}", role.getName(), user.getEmail());
    }
    
    public List<Map<String, Object>> getUsersByRole(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleId));
        
        List<User> users = userRepository.findByRolesContaining(role);
        
        return users.stream()
                .map(user -> {
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("id", user.getId());
                    userMap.put("email", user.getEmail());
                    userMap.put("firstName", user.getFirstName());
                    userMap.put("lastName", user.getLastName());
                    userMap.put("isActive", user.getIsActive());
                    return userMap;
                })
                .collect(Collectors.toList());
    }
    
    public Map<String, Object> getRoleHierarchy() {
        List<Role> allRoles = roleRepository.findAll();
        Map<String, Object> hierarchy = new HashMap<>();
        
        // Build hierarchy based on role names (simplified)
        hierarchy.put("ROLE_ADMIN", Map.of(
                "level", 1,
                "children", List.of("ROLE_MANAGER")
        ));
        hierarchy.put("ROLE_MANAGER", Map.of(
                "level", 2,
                "children", List.of("ROLE_USER")
        ));
        hierarchy.put("ROLE_USER", Map.of(
                "level", 3,
                "children", List.of()
        ));
        
        return hierarchy;
    }
    
    @Transactional
    @CacheEvict(value = ROLE_CACHE, allEntries = true)
    public RoleDto setRoleParent(Long roleId, Long parentRoleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleId));
        
        Role parentRole = roleRepository.findById(parentRoleId)
                .orElseThrow(() -> new RuntimeException("Parent role not found: " + parentRoleId));
        
        // Simplified parent setting - in a real system, you'd have a parent_id field
        log.info("Set parent of role {} to {}", role.getName(), parentRole.getName());
        
        return toDto(role);
    }
    
    public List<Map<String, Object>> getRoleTemplates() {
        List<Map<String, Object>> templates = new ArrayList<>();
        
        templates.add(Map.of(
                "name", "VIEWER",
                "description", "Read-only access template",
                "permissions", List.of("VIEW", "READ")
        ));
        
        templates.add(Map.of(
                "name", "EDITOR",
                "description", "Edit access template",
                "permissions", List.of("VIEW", "READ", "CREATE", "UPDATE")
        ));
        
        templates.add(Map.of(
                "name", "MODERATOR",
                "description", "Moderation access template",
                "permissions", List.of("VIEW", "READ", "CREATE", "UPDATE", "DELETE", "MODERATE")
        ));
        
        return templates;
    }
    
    @Transactional
    @CacheEvict(value = ROLE_CACHE, allEntries = true)
    public RoleDto createRoleFromTemplate(String templateName, Map<String, String> customizations) {
        String roleName = customizations.getOrDefault("name", "ROLE_" + templateName);
        String description = customizations.getOrDefault("description", templateName + " role");
        
        RoleDto roleDto = RoleDto.builder()
                .name(roleName)
                .description(description)
                .isActive(true)
                .build();
        
        return createRole(roleDto);
    }
    
    @Transactional
    @CacheEvict(value = ROLE_CACHE, allEntries = true)
    public RoleDto duplicateRole(Long roleId, String newRoleName) {
        Role sourceRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleId));
        
        Role newRole = Role.builder()
                .name(newRoleName)
                .description(sourceRole.getDescription() + " (Copy)")
                .permissions(new HashSet<>(sourceRole.getPermissions()))
                .isActive(true)
                .build();
        
        newRole = roleRepository.save(newRole);
        log.info("Duplicated role {} as {}", sourceRole.getName(), newRole.getName());
        
        return toDto(newRole);
    }
    
    private RoleDto toDto(Role role) {
        return RoleDto.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .isActive(role.getIsActive())
                .createdAt(role.getCreatedAt())
                .updatedAt(role.getUpdatedAt())
                .permissions(role.getPermissions().stream()
                        .map(this::toPermissionDto)
                        .collect(Collectors.toSet()))
                .build();
    }
    
    private PermissionDto toPermissionDto(Permission permission) {
        return PermissionDto.builder()
                .id(permission.getId())
                .name(permission.getName())
                .description(permission.getDescription())
                .resource(permission.getResource())
                .action(permission.getAction())
                .build();
    }
}