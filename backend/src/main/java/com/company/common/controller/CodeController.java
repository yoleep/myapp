package com.company.common.controller;

import com.company.common.dto.ApiResponse;
import com.company.common.dto.code.CodeGroupDto;
import com.company.common.dto.code.CodeGroupRequest;
import com.company.common.dto.code.CodeItemDto;
import com.company.common.dto.code.CodeItemRequest;
import com.company.common.service.CodeService;
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
@RequestMapping("/api/v1/codes")
@RequiredArgsConstructor
@Tag(name = "Common Code Management", description = "Common code management APIs")
@SecurityRequirement(name = "bearerAuth")
public class CodeController {
    
    private final CodeService codeService;
    
    @GetMapping("/groups")
    @Operation(summary = "Get all code groups", description = "Get all code groups with items")
    public ResponseEntity<ApiResponse<List<CodeGroupDto>>> getAllCodeGroups() {
        List<CodeGroupDto> groups = codeService.getAllCodeGroups();
        return ResponseEntity.ok(ApiResponse.success(groups));
    }
    
    @GetMapping("/groups/{groupId}")
    @Operation(summary = "Get code group", description = "Get code group by ID")
    public ResponseEntity<ApiResponse<CodeGroupDto>> getCodeGroup(@PathVariable String groupId) {
        CodeGroupDto group = codeService.getCodeGroup(groupId);
        return ResponseEntity.ok(ApiResponse.success(group));
    }
    
    @PostMapping("/groups")
    @Operation(summary = "Create code group", description = "Create a new code group")
    @PreAuthorize("hasAuthority('CREATE')")
    public ResponseEntity<ApiResponse<CodeGroupDto>> createCodeGroup(
            @Valid @RequestBody CodeGroupRequest request) {
        CodeGroupDto group = codeService.createCodeGroup(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(group, "Code group created successfully"));
    }
    
    @PutMapping("/groups/{groupId}")
    @Operation(summary = "Update code group", description = "Update code group")
    @PreAuthorize("hasAuthority('UPDATE')")
    public ResponseEntity<ApiResponse<CodeGroupDto>> updateCodeGroup(
            @PathVariable String groupId,
            @Valid @RequestBody CodeGroupRequest request) {
        CodeGroupDto group = codeService.updateCodeGroup(groupId, request);
        return ResponseEntity.ok(ApiResponse.success(group, "Code group updated successfully"));
    }
    
    @DeleteMapping("/groups/{groupId}")
    @Operation(summary = "Delete code group", description = "Delete code group")
    @PreAuthorize("hasAuthority('DELETE')")
    public ResponseEntity<ApiResponse<String>> deleteCodeGroup(@PathVariable String groupId) {
        codeService.deleteCodeGroup(groupId);
        return ResponseEntity.ok(ApiResponse.success("Code group deleted successfully"));
    }
    
    @GetMapping("/groups/{groupId}/items")
    @Operation(summary = "Get code items", description = "Get all items in a code group")
    public ResponseEntity<ApiResponse<List<CodeItemDto>>> getCodeItems(@PathVariable String groupId) {
        List<CodeItemDto> items = codeService.getCodeItems(groupId);
        return ResponseEntity.ok(ApiResponse.success(items));
    }
    
    @PostMapping("/groups/{groupId}/items")
    @Operation(summary = "Create code item", description = "Create a new code item")
    @PreAuthorize("hasAuthority('CREATE')")
    public ResponseEntity<ApiResponse<CodeItemDto>> createCodeItem(
            @PathVariable String groupId,
            @Valid @RequestBody CodeItemRequest request) {
        CodeItemDto item = codeService.createCodeItem(groupId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(item, "Code item created successfully"));
    }
    
    @PutMapping("/items/{itemId}")
    @Operation(summary = "Update code item", description = "Update code item")
    @PreAuthorize("hasAuthority('UPDATE')")
    public ResponseEntity<ApiResponse<CodeItemDto>> updateCodeItem(
            @PathVariable Long itemId,
            @Valid @RequestBody CodeItemRequest request) {
        CodeItemDto item = codeService.updateCodeItem(itemId, request);
        return ResponseEntity.ok(ApiResponse.success(item, "Code item updated successfully"));
    }
    
    @DeleteMapping("/items/{itemId}")
    @Operation(summary = "Delete code item", description = "Delete code item")
    @PreAuthorize("hasAuthority('DELETE')")
    public ResponseEntity<ApiResponse<String>> deleteCodeItem(@PathVariable Long itemId) {
        codeService.deleteCodeItem(itemId);
        return ResponseEntity.ok(ApiResponse.success("Code item deleted successfully"));
    }
}