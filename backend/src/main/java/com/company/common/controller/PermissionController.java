package com.company.common.controller;

import com.company.common.dto.PermissionDto;
import com.company.common.service.PermissionService;
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

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
@Tag(name = "Permission Management", description = "APIs for managing permissions")
public class PermissionController {
    
    private final PermissionService permissionService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all permissions", description = "Retrieve all permissions in the system")
    public ResponseEntity<List<PermissionDto>> getAllPermissions() {
        List<PermissionDto> permissions = permissionService.getAllPermissions();
        return ResponseEntity.ok(permissions);
    }
    
    @GetMapping("/{permissionId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get permission by ID", description = "Retrieve a specific permission by ID")
    public ResponseEntity<PermissionDto> getPermissionById(@PathVariable Long permissionId) {
        PermissionDto permission = permissionService.getPermissionById(permissionId);
        return ResponseEntity.ok(permission);
    }
    
    @GetMapping("/resource/{resource}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get permissions by resource", description = "Retrieve all permissions for a specific resource")
    public ResponseEntity<List<PermissionDto>> getPermissionsByResource(@PathVariable String resource) {
        List<PermissionDto> permissions = permissionService.getPermissionsByResource(resource);
        return ResponseEntity.ok(permissions);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(action = "CREATE_PERMISSION")
    @Operation(summary = "Create permission", description = "Create a new permission")
    public ResponseEntity<PermissionDto> createPermission(@Valid @RequestBody PermissionDto permissionDto) {
        PermissionDto created = permissionService.createPermission(permissionDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{permissionId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(action = "UPDATE_PERMISSION")
    @Operation(summary = "Update permission", description = "Update an existing permission")
    public ResponseEntity<PermissionDto> updatePermission(
            @PathVariable Long permissionId,
            @Valid @RequestBody PermissionDto permissionDto) {
        PermissionDto updated = permissionService.updatePermission(permissionId, permissionDto);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{permissionId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(action = "DELETE_PERMISSION")
    @Operation(summary = "Delete permission", description = "Delete a permission")
    public ResponseEntity<Void> deletePermission(@PathVariable Long permissionId) {
        permissionService.deletePermission(permissionId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/users/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    @Operation(summary = "Get user permissions", description = "Get all permissions for a specific user")
    public ResponseEntity<List<PermissionDto>> getUserPermissions(@PathVariable Long userId) {
        List<PermissionDto> permissions = permissionService.getUserPermissions(userId);
        return ResponseEntity.ok(permissions);
    }
    
    @GetMapping("/users/{userId}/effective")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    @Operation(summary = "Get effective permissions", description = "Get effective permissions for a user (including role-based)")
    public ResponseEntity<List<PermissionDto>> getEffectiveUserPermissions(@PathVariable Long userId) {
        List<PermissionDto> permissions = permissionService.getEffectiveUserPermissions(userId);
        return ResponseEntity.ok(permissions);
    }
    
    @PostMapping("/users/{userId}/check")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    @Operation(summary = "Check permission", description = "Check if a user has a specific permission")
    public ResponseEntity<Map<String, Boolean>> checkUserPermission(
            @PathVariable Long userId,
            @RequestBody Map<String, String> request) {
        String resource = request.get("resource");
        String action = request.get("action");
        
        boolean hasPermission = permissionService.hasPermission(userId, resource, action);
        return ResponseEntity.ok(Map.of("hasPermission", hasPermission));
    }
    
    @PostMapping("/batch-check")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Batch check permissions", description = "Check multiple permissions at once")
    public ResponseEntity<Map<String, Boolean>> batchCheckPermissions(
            @RequestBody List<Map<String, String>> permissions) {
        Long userId = getCurrentUserId(); // This would get the current user's ID from security context
        Map<String, Boolean> results = permissionService.batchCheckPermissions(userId, permissions);
        return ResponseEntity.ok(results);
    }
    
    @GetMapping("/resources")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all resources", description = "Get list of all protected resources")
    public ResponseEntity<List<String>> getAllResources() {
        List<String> resources = permissionService.getAllResources();
        return ResponseEntity.ok(resources);
    }
    
    @GetMapping("/actions")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all actions", description = "Get list of all permission actions")
    public ResponseEntity<List<String>> getAllActions() {
        List<String> actions = permissionService.getAllActions();
        return ResponseEntity.ok(actions);
    }
    
    @PostMapping("/template")
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(action = "CREATE_PERMISSION_TEMPLATE")
    @Operation(summary = "Create from template", description = "Create permissions from a template")
    public ResponseEntity<List<PermissionDto>> createFromTemplate(
            @RequestParam String templateName,
            @RequestParam String resource) {
        List<PermissionDto> created = permissionService.createFromTemplate(templateName, resource);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    // Helper method to get current user ID (would typically be in a base controller or service)
    private Long getCurrentUserId() {
        // This would typically get the user ID from the security context
        // Placeholder implementation
        return 1L;
    }
}