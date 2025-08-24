package com.company.common.controller;

import com.company.common.dto.ApiResponse;
import com.company.common.dto.user.UserDto;
import com.company.common.dto.user.UserUpdateRequest;
import com.company.common.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "User management APIs")
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    
    private final UserService userService;
    
    @GetMapping
    @Operation(summary = "Get all users", description = "Get paginated list of users")
    @PreAuthorize("hasAuthority('VIEW')")
    public ResponseEntity<ApiResponse<Page<UserDto>>> getAllUsers(Pageable pageable) {
        Page<UserDto> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(ApiResponse.success(users));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Get user details by ID")
    @PreAuthorize("hasAuthority('VIEW') or #id == authentication.principal.id")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable Long id) {
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(user));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Update user information")
    @PreAuthorize("hasAuthority('UPDATE') or #id == authentication.principal.id")
    public ResponseEntity<ApiResponse<UserDto>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request) {
        UserDto user = userService.updateUser(id, request);
        return ResponseEntity.ok(ApiResponse.success(user, "User updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Delete user (soft delete)")
    @PreAuthorize("hasAuthority('DELETE')")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
    }
    
    @PostMapping("/{userId}/roles/{roleId}")
    @Operation(summary = "Assign role to user", description = "Assign a role to user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<UserDto>> assignRole(
            @PathVariable Long userId,
            @PathVariable Long roleId) {
        UserDto user = userService.assignRole(userId, roleId);
        return ResponseEntity.ok(ApiResponse.success(user, "Role assigned successfully"));
    }
    
    @DeleteMapping("/{userId}/roles/{roleId}")
    @Operation(summary = "Remove role from user", description = "Remove a role from user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<UserDto>> removeRole(
            @PathVariable Long userId,
            @PathVariable Long roleId) {
        UserDto user = userService.removeRole(userId, roleId);
        return ResponseEntity.ok(ApiResponse.success(user, "Role removed successfully"));
    }
    
    @GetMapping("/{userId}/permissions")
    @Operation(summary = "Get user permissions", description = "Get all permissions for a user")
    @PreAuthorize("hasAuthority('VIEW') or #userId == authentication.principal.id")
    public ResponseEntity<ApiResponse<UserDto>> getUserPermissions(@PathVariable Long userId) {
        UserDto user = userService.getUserWithPermissions(userId);
        return ResponseEntity.ok(ApiResponse.success(user));
    }
}