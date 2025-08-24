package com.company.common.controller;

import com.company.common.dto.ApiResponse;
import com.company.common.dto.menu.MenuDto;
import com.company.common.dto.menu.MenuCreateRequest;
import com.company.common.dto.menu.MenuTreeDto;
import com.company.common.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/menus")
@RequiredArgsConstructor
@Tag(name = "Menu Management", description = "Menu management APIs")
@SecurityRequirement(name = "bearerAuth")
public class MenuController {
    
    private final MenuService menuService;
    
    @GetMapping("/tree")
    @Operation(summary = "Get menu tree", description = "Get hierarchical menu structure")
    public ResponseEntity<ApiResponse<List<MenuTreeDto>>> getMenuTree(
            @RequestParam(required = false) Long roleId,
            @RequestParam(required = false) Long userId) {
        List<MenuTreeDto> menuTree = menuService.getMenuTree(roleId, userId);
        return ResponseEntity.ok(ApiResponse.success(menuTree));
    }
    
    @GetMapping
    @Operation(summary = "Get all menus", description = "Get all menus list")
    @PreAuthorize("hasAuthority('VIEW')")
    public ResponseEntity<ApiResponse<List<MenuDto>>> getAllMenus() {
        List<MenuDto> menus = menuService.getAllMenus();
        return ResponseEntity.ok(ApiResponse.success(menus));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get menu by ID", description = "Get menu details by ID")
    @PreAuthorize("hasAuthority('VIEW')")
    public ResponseEntity<ApiResponse<MenuDto>> getMenuById(@PathVariable Long id) {
        MenuDto menu = menuService.getMenuById(id);
        return ResponseEntity.ok(ApiResponse.success(menu));
    }
    
    @PostMapping
    @Operation(summary = "Create menu", description = "Create a new menu")
    @PreAuthorize("hasAuthority('CREATE')")
    public ResponseEntity<ApiResponse<MenuDto>> createMenu(@Valid @RequestBody MenuCreateRequest request) {
        MenuDto menu = menuService.createMenu(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(menu, "Menu created successfully"));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update menu", description = "Update menu information")
    @PreAuthorize("hasAuthority('UPDATE')")
    public ResponseEntity<ApiResponse<MenuDto>> updateMenu(
            @PathVariable Long id,
            @Valid @RequestBody MenuCreateRequest request) {
        MenuDto menu = menuService.updateMenu(id, request);
        return ResponseEntity.ok(ApiResponse.success(menu, "Menu updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete menu", description = "Delete menu")
    @PreAuthorize("hasAuthority('DELETE')")
    public ResponseEntity<ApiResponse<String>> deleteMenu(@PathVariable Long id) {
        menuService.deleteMenu(id);
        return ResponseEntity.ok(ApiResponse.success("Menu deleted successfully"));
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user menus", description = "Get user-specific menu tree")
    @PreAuthorize("hasAuthority('VIEW') or #userId == authentication.principal.id")
    public ResponseEntity<ApiResponse<List<MenuTreeDto>>> getUserMenus(@PathVariable Long userId) {
        List<MenuTreeDto> menuTree = menuService.getUserMenuTree(userId);
        return ResponseEntity.ok(ApiResponse.success(menuTree));
    }
}