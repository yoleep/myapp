package com.company.common.controller;

import com.company.common.dto.RoleDto;
import com.company.common.dto.PermissionDto;
import com.company.common.entity.Role;
import com.company.common.service.RoleService;
import com.company.common.util.Auditable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Tag(name = "Role Management", description = "APIs for managing roles and permissions")
public class RoleController {
    
    private final RoleService roleService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all roles", description = "Retrieve all roles in the system")
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        List<RoleDto> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }
    
    @GetMapping("/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get role by ID", description = "Retrieve a specific role by ID")
    public ResponseEntity<RoleDto> getRoleById(@PathVariable Long roleId) {
        RoleDto role = roleService.getRoleById(roleId);
        return ResponseEntity.ok(role);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(action = "CREATE_ROLE")
    @Operation(summary = "Create role", description = "Create a new role")
    public ResponseEntity<RoleDto> createRole(@Valid @RequestBody RoleDto roleDto) {
        RoleDto createdRole = roleService.createRole(roleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
    }
    
    @PutMapping("/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(action = "UPDATE_ROLE")
    @Operation(summary = "Update role", description = "Update an existing role")
    public ResponseEntity<RoleDto> updateRole(
            @PathVariable Long roleId,
            @Valid @RequestBody RoleDto roleDto) {
        RoleDto updatedRole = roleService.updateRole(roleId, roleDto);
        return ResponseEntity.ok(updatedRole);
    }
    
    @DeleteMapping("/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(action = "DELETE_ROLE")
    @Operation(summary = "Delete role", description = "Delete a role")
    public ResponseEntity<Void> deleteRole(@PathVariable Long roleId) {
        roleService.deleteRole(roleId);
        return ResponseEntity.noContent().build();
    }
    
    // Permission Management
    @GetMapping("/{roleId}/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get role permissions", description = "Get all permissions for a role")
    public ResponseEntity<Set<PermissionDto>> getRolePermissions(@PathVariable Long roleId) {
        Set<PermissionDto> permissions = roleService.getRolePermissions(roleId);
        return ResponseEntity.ok(permissions);
    }
    
    @PostMapping("/{roleId}/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(action = "ASSIGN_PERMISSIONS")
    @Operation(summary = "Assign permissions", description = "Assign permissions to a role")
    public ResponseEntity<RoleDto> assignPermissions(
            @PathVariable Long roleId,
            @RequestBody List<Long> permissionIds) {
        RoleDto role = roleService.assignPermissions(roleId, permissionIds);
        return ResponseEntity.ok(role);
    }
    
    @DeleteMapping("/{roleId}/permissions/{permissionId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(action = "REVOKE_PERMISSION")
    @Operation(summary = "Revoke permission", description = "Revoke a permission from a role")
    public ResponseEntity<Void> revokePermission(
            @PathVariable Long roleId,
            @PathVariable Long permissionId) {
        roleService.revokePermission(roleId, permissionId);
        return ResponseEntity.noContent().build();
    }
    
    // User-Role Management
    @PostMapping("/{roleId}/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(action = "ASSIGN_ROLE_TO_USER")
    @Operation(summary = "Assign role to user", description = "Assign a role to a user")
    public ResponseEntity<Map<String, Object>> assignRoleToUser(
            @PathVariable Long roleId,
            @PathVariable Long userId) {
        roleService.assignRoleToUser(userId, roleId);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Role assigned to user"
        ));
    }
    
    @DeleteMapping("/{roleId}/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(action = "REVOKE_ROLE_FROM_USER")
    @Operation(summary = "Revoke role from user", description = "Revoke a role from a user")
    public ResponseEntity<Map<String, Object>> revokeRoleFromUser(
            @PathVariable Long roleId,
            @PathVariable Long userId) {
        roleService.revokeRoleFromUser(userId, roleId);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Role revoked from user"
        ));
    }
    
    @GetMapping("/{roleId}/users")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get users by role", description = "Get all users with a specific role")
    public ResponseEntity<List<Map<String, Object>>> getUsersByRole(@PathVariable Long roleId) {
        List<Map<String, Object>> users = roleService.getUsersByRole(roleId);
        return ResponseEntity.ok(users);
    }
    
    // Role Hierarchy
    @GetMapping("/hierarchy")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get role hierarchy", description = "Get the role hierarchy tree")
    public ResponseEntity<Map<String, Object>> getRoleHierarchy() {
        Map<String, Object> hierarchy = roleService.getRoleHierarchy();
        return ResponseEntity.ok(hierarchy);
    }
    
    @PutMapping("/{roleId}/parent/{parentRoleId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(action = "SET_ROLE_PARENT")
    @Operation(summary = "Set role parent", description = "Set parent role for hierarchy")
    public ResponseEntity<RoleDto> setRoleParent(
            @PathVariable Long roleId,
            @PathVariable Long parentRoleId) {
        RoleDto role = roleService.setRoleParent(roleId, parentRoleId);
        return ResponseEntity.ok(role);
    }
    
    // Role Templates
    @GetMapping("/templates")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get role templates", description = "Get available role templates")
    public ResponseEntity<List<Map<String, Object>>> getRoleTemplates() {
        List<Map<String, Object>> templates = roleService.getRoleTemplates();
        return ResponseEntity.ok(templates);
    }
    
    @PostMapping("/from-template")
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(action = "CREATE_ROLE_FROM_TEMPLATE")
    @Operation(summary = "Create from template", description = "Create a role from a template")
    public ResponseEntity<RoleDto> createRoleFromTemplate(
            @RequestParam String templateName,
            @RequestBody Map<String, String> customizations) {
        RoleDto role = roleService.createRoleFromTemplate(templateName, customizations);
        return ResponseEntity.status(HttpStatus.CREATED).body(role);
    }
    
    // Role Duplication
    @PostMapping("/{roleId}/duplicate")
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(action = "DUPLICATE_ROLE")
    @Operation(summary = "Duplicate role", description = "Create a copy of an existing role")
    public ResponseEntity<RoleDto> duplicateRole(
            @PathVariable Long roleId,
            @RequestParam String newRoleName) {
        RoleDto duplicatedRole = roleService.duplicateRole(roleId, newRoleName);
        return ResponseEntity.status(HttpStatus.CREATED).body(duplicatedRole);
    }
}