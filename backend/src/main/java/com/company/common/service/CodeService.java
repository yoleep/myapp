package com.company.common.service;

import com.company.common.dto.code.CodeGroupDto;
import com.company.common.dto.code.CodeItemDto;
import com.company.common.entity.CodeGroup;
import com.company.common.entity.CodeItem;
import com.company.common.repository.CodeGroupRepository;
import com.company.common.repository.CodeItemRepository;
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
public class CodeService {
    
    private final CodeGroupRepository codeGroupRepository;
    private final CodeItemRepository codeItemRepository;
    
    private static final String CODE_GROUP_CACHE = "codeGroups";
    private static final String CODE_ITEM_CACHE = "codeItems";
    
    // Code Group Operations
    @Cacheable(value = CODE_GROUP_CACHE)
    public List<CodeGroupDto> getAllCodeGroups() {
        return codeGroupRepository.findAll().stream()
                .map(this::toGroupDto)
                .collect(Collectors.toList());
    }
    
    @Cacheable(value = CODE_GROUP_CACHE, key = "#id")
    public CodeGroupDto getCodeGroupById(Long id) {
        CodeGroup group = codeGroupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Code group not found: " + id));
        return toGroupDto(group);
    }
    
    @Cacheable(value = CODE_GROUP_CACHE, key = "#groupCode")
    public CodeGroupDto getCodeGroupByCode(String groupCode) {
        CodeGroup group = codeGroupRepository.findByGroupCode(groupCode)
                .orElseThrow(() -> new RuntimeException("Code group not found: " + groupCode));
        return toGroupDto(group);
    }
    
    @Transactional
    @CacheEvict(value = CODE_GROUP_CACHE, allEntries = true)
    public CodeGroupDto createCodeGroup(CodeGroupDto dto) {
        if (codeGroupRepository.existsByGroupCode(dto.getGroupCode())) {
            throw new RuntimeException("Code group already exists: " + dto.getGroupCode());
        }
        
        CodeGroup group = CodeGroup.builder()
                .groupCode(dto.getGroupCode())
                .groupName(dto.getGroupName())
                .description(dto.getDescription())
                .sortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0)
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .isSystem(dto.getIsSystem() != null ? dto.getIsSystem() : false)
                .build();
        
        group = codeGroupRepository.save(group);
        log.info("Created code group: {}", group.getGroupCode());
        return toGroupDto(group);
    }
    
    @Transactional
    @CacheEvict(value = CODE_GROUP_CACHE, allEntries = true)
    public CodeGroupDto updateCodeGroup(Long id, CodeGroupDto dto) {
        CodeGroup group = codeGroupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Code group not found: " + id));
        
        if (group.getIsSystem()) {
            throw new RuntimeException("Cannot modify system code group: " + group.getGroupCode());
        }
        
        group.setGroupName(dto.getGroupName());
        group.setDescription(dto.getDescription());
        group.setSortOrder(dto.getSortOrder());
        group.setIsActive(dto.getIsActive());
        
        group = codeGroupRepository.save(group);
        log.info("Updated code group: {}", group.getGroupCode());
        return toGroupDto(group);
    }
    
    @Transactional
    @CacheEvict(value = {CODE_GROUP_CACHE, CODE_ITEM_CACHE}, allEntries = true)
    public void deleteCodeGroup(Long id) {
        CodeGroup group = codeGroupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Code group not found: " + id));
        
        if (group.getIsSystem()) {
            throw new RuntimeException("Cannot delete system code group: " + group.getGroupCode());
        }
        
        // Delete all items in the group
        codeItemRepository.deleteByGroupId(id);
        
        // Delete the group
        codeGroupRepository.delete(group);
        log.info("Deleted code group: {}", group.getGroupCode());
    }
    
    // Code Item Operations
    @Cacheable(value = CODE_ITEM_CACHE, key = "#groupId")
    public List<CodeItemDto> getCodeItemsByGroup(Long groupId) {
        return codeItemRepository.findByGroupIdOrderBySortOrder(groupId).stream()
                .map(this::toItemDto)
                .collect(Collectors.toList());
    }
    
    @Cacheable(value = CODE_ITEM_CACHE, key = "#groupCode")
    public List<CodeItemDto> getCodeItemsByGroupCode(String groupCode) {
        CodeGroup group = codeGroupRepository.findByGroupCode(groupCode)
                .orElseThrow(() -> new RuntimeException("Code group not found: " + groupCode));
        
        return codeItemRepository.findByGroupIdAndIsActiveOrderBySortOrder(group.getId(), true).stream()
                .map(this::toItemDto)
                .collect(Collectors.toList());
    }
    
    @Cacheable(value = CODE_ITEM_CACHE, key = "#id")
    public CodeItemDto getCodeItemById(Long id) {
        CodeItem item = codeItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Code item not found: " + id));
        return toItemDto(item);
    }
    
    public CodeItemDto getCodeItemByGroupAndCode(String groupCode, String itemCode) {
        CodeGroup group = codeGroupRepository.findByGroupCode(groupCode)
                .orElseThrow(() -> new RuntimeException("Code group not found: " + groupCode));
        
        CodeItem item = codeItemRepository.findByGroupIdAndCode(group.getId(), itemCode)
                .orElseThrow(() -> new RuntimeException("Code item not found: " + itemCode));
        
        return toItemDto(item);
    }
    
    @Transactional
    @CacheEvict(value = CODE_ITEM_CACHE, allEntries = true)
    public CodeItemDto createCodeItem(CodeItemDto dto) {
        // Verify group exists
        CodeGroup group = codeGroupRepository.findById(dto.getGroupId())
                .orElseThrow(() -> new RuntimeException("Code group not found: " + dto.getGroupId()));
        
        // Check for duplicate code
        if (codeItemRepository.existsByGroupIdAndCode(dto.getGroupId(), dto.getCode())) {
            throw new RuntimeException("Code item already exists: " + dto.getCode());
        }
        
        // Calculate sort order if not provided
        if (dto.getSortOrder() == null) {
            Integer maxOrder = codeItemRepository.findMaxSortOrderByGroupId(dto.getGroupId());
            dto.setSortOrder(maxOrder != null ? maxOrder + 1 : 1);
        }
        
        CodeItem item = CodeItem.builder()
                .groupId(dto.getGroupId())
                .code(dto.getCode())
                .codeName(dto.getCodeName())
                .codeValue(dto.getCodeValue())
                .description(dto.getDescription())
                .parentId(dto.getParentId())
                .sortOrder(dto.getSortOrder())
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .isDefault(dto.getIsDefault() != null ? dto.getIsDefault() : false)
                .attribute1(dto.getAttribute1())
                .attribute2(dto.getAttribute2())
                .attribute3(dto.getAttribute3())
                .attribute4(dto.getAttribute4())
                .attribute5(dto.getAttribute5())
                .build();
        
        item = codeItemRepository.save(item);
        log.info("Created code item: {} in group: {}", item.getCode(), group.getGroupCode());
        return toItemDto(item);
    }
    
    @Transactional
    @CacheEvict(value = CODE_ITEM_CACHE, allEntries = true)
    public CodeItemDto updateCodeItem(Long id, CodeItemDto dto) {
        CodeItem item = codeItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Code item not found: " + id));
        
        // Check if changing code would create duplicate
        if (!item.getCode().equals(dto.getCode()) &&
            codeItemRepository.existsByGroupIdAndCode(item.getGroupId(), dto.getCode())) {
            throw new RuntimeException("Code item already exists: " + dto.getCode());
        }
        
        item.setCode(dto.getCode());
        item.setCodeName(dto.getCodeName());
        item.setCodeValue(dto.getCodeValue());
        item.setDescription(dto.getDescription());
        item.setParentId(dto.getParentId());
        item.setSortOrder(dto.getSortOrder());
        item.setIsActive(dto.getIsActive());
        item.setIsDefault(dto.getIsDefault());
        item.setAttribute1(dto.getAttribute1());
        item.setAttribute2(dto.getAttribute2());
        item.setAttribute3(dto.getAttribute3());
        item.setAttribute4(dto.getAttribute4());
        item.setAttribute5(dto.getAttribute5());
        
        item = codeItemRepository.save(item);
        log.info("Updated code item: {}", item.getCode());
        return toItemDto(item);
    }
    
    @Transactional
    @CacheEvict(value = CODE_ITEM_CACHE, allEntries = true)
    public void deleteCodeItem(Long id) {
        CodeItem item = codeItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Code item not found: " + id));
        
        // Check if item has children
        List<CodeItem> children = codeItemRepository.findByParentIdOrderBySortOrder(id);
        if (!children.isEmpty()) {
            throw new RuntimeException("Cannot delete code item with children");
        }
        
        codeItemRepository.delete(item);
        log.info("Deleted code item: {}", item.getCode());
    }
    
    // Hierarchy Operations
    public List<CodeItemDto> getCodeItemHierarchy(Long groupId) {
        List<CodeItem> rootItems = codeItemRepository.findRootItemsByGroupId(groupId);
        return rootItems.stream()
                .map(this::toItemDtoWithChildren)
                .collect(Collectors.toList());
    }
    
    private CodeItemDto toItemDtoWithChildren(CodeItem item) {
        CodeItemDto dto = toItemDto(item);
        List<CodeItem> children = codeItemRepository.findByParentIdOrderBySortOrder(item.getId());
        
        if (!children.isEmpty()) {
            dto.setChildren(children.stream()
                    .map(this::toItemDtoWithChildren)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }
    
    // Batch Operations
    @Transactional
    @CacheEvict(value = CODE_ITEM_CACHE, allEntries = true)
    public List<CodeItemDto> batchCreateCodeItems(Long groupId, List<CodeItemDto> items) {
        // Verify group exists
        CodeGroup group = codeGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Code group not found: " + groupId));
        
        List<CodeItemDto> created = new ArrayList<>();
        
        for (CodeItemDto dto : items) {
            dto.setGroupId(groupId);
            try {
                created.add(createCodeItem(dto));
            } catch (Exception e) {
                log.error("Failed to create code item: {}", dto.getCode(), e);
            }
        }
        
        log.info("Batch created {} code items in group: {}", created.size(), group.getGroupCode());
        return created;
    }
    
    @Transactional
    @CacheEvict(value = CODE_ITEM_CACHE, allEntries = true)
    public void reorderCodeItems(Long groupId, List<Long> itemIds) {
        for (int i = 0; i < itemIds.size(); i++) {
            CodeItem item = codeItemRepository.findById(itemIds.get(i))
                    .orElseThrow(() -> new RuntimeException("Code item not found: " + itemIds.get(i)));
            
            if (!item.getGroupId().equals(groupId)) {
                throw new RuntimeException("Code item does not belong to group: " + groupId);
            }
            
            item.setSortOrder(i + 1);
            codeItemRepository.save(item);
        }
        
        log.info("Reordered {} code items in group: {}", itemIds.size(), groupId);
    }
    
    // Search Operations
    public List<CodeItemDto> searchCodeItems(String keyword) {
        return codeItemRepository.findByCodeNameContaining(keyword).stream()
                .map(this::toItemDto)
                .collect(Collectors.toList());
    }
    
    public List<CodeItemDto> searchCodeItemsInGroup(Long groupId, String keyword) {
        return codeItemRepository.searchByGroupIdAndKeyword(groupId, keyword).stream()
                .map(this::toItemDto)
                .collect(Collectors.toList());
    }
    
    // Utility Methods
    public Map<String, String> getCodeMap(String groupCode) {
        List<CodeItemDto> items = getCodeItemsByGroupCode(groupCode);
        return items.stream()
                .filter(CodeItemDto::getIsActive)
                .collect(Collectors.toMap(
                        CodeItemDto::getCode,
                        CodeItemDto::getCodeName,
                        (v1, v2) -> v1,
                        LinkedHashMap::new
                ));
    }
    
    public String getCodeName(String groupCode, String itemCode) {
        try {
            CodeItemDto item = getCodeItemByGroupAndCode(groupCode, itemCode);
            return item.getCodeName();
        } catch (Exception e) {
            return itemCode; // Return code if not found
        }
    }
    
    public String getCodeValue(String groupCode, String itemCode) {
        try {
            CodeItemDto item = getCodeItemByGroupAndCode(groupCode, itemCode);
            return item.getCodeValue();
        } catch (Exception e) {
            return null;
        }
    }
    
    @CacheEvict(value = {CODE_GROUP_CACHE, CODE_ITEM_CACHE}, allEntries = true)
    public void refreshCache() {
        log.info("Refreshing code cache");
    }
    
    // DTO Conversion Methods
    private CodeGroupDto toGroupDto(CodeGroup entity) {
        return CodeGroupDto.builder()
                .id(entity.getId())
                .groupCode(entity.getGroupCode())
                .groupName(entity.getGroupName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .isActive(entity.getIsActive())
                .isSystem(entity.getIsSystem())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
    
    private CodeItemDto toItemDto(CodeItem entity) {
        return CodeItemDto.builder()
                .id(entity.getId())
                .groupId(entity.getGroupId())
                .code(entity.getCode())
                .codeName(entity.getCodeName())
                .codeValue(entity.getCodeValue())
                .description(entity.getDescription())
                .parentId(entity.getParentId())
                .sortOrder(entity.getSortOrder())
                .isActive(entity.getIsActive())
                .isDefault(entity.getIsDefault())
                .attribute1(entity.getAttribute1())
                .attribute2(entity.getAttribute2())
                .attribute3(entity.getAttribute3())
                .attribute4(entity.getAttribute4())
                .attribute5(entity.getAttribute5())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
    
    // Initialize default codes
    @Transactional
    public void initializeDefaultCodes() {
        // Create default code groups if they don't exist
        createDefaultCodeGroup("GENDER", "Gender", "Gender codes");
        createDefaultCodeGroup("STATUS", "Status", "General status codes");
        createDefaultCodeGroup("USER_STATUS", "User Status", "User account status codes");
        createDefaultCodeGroup("FILE_TYPE", "File Type", "File type codes");
        createDefaultCodeGroup("NOTIFICATION_TYPE", "Notification Type", "Notification type codes");
        
        // Create default code items
        createDefaultCodeItems("GENDER", Map.of(
                "M", "Male",
                "F", "Female",
                "O", "Other"
        ));
        
        createDefaultCodeItems("STATUS", Map.of(
                "ACTIVE", "Active",
                "INACTIVE", "Inactive",
                "PENDING", "Pending",
                "COMPLETED", "Completed",
                "CANCELLED", "Cancelled"
        ));
        
        createDefaultCodeItems("USER_STATUS", Map.of(
                "ACTIVE", "Active",
                "INACTIVE", "Inactive",
                "LOCKED", "Locked",
                "SUSPENDED", "Suspended",
                "DELETED", "Deleted"
        ));
        
        log.info("Initialized default codes");
    }
    
    private void createDefaultCodeGroup(String code, String name, String description) {
        if (!codeGroupRepository.existsByGroupCode(code)) {
            CodeGroup group = CodeGroup.builder()
                    .groupCode(code)
                    .groupName(name)
                    .description(description)
                    .sortOrder(0)
                    .isActive(true)
                    .isSystem(true)
                    .build();
            codeGroupRepository.save(group);
        }
    }
    
    private void createDefaultCodeItems(String groupCode, Map<String, String> items) {
        CodeGroup group = codeGroupRepository.findByGroupCode(groupCode).orElse(null);
        if (group != null) {
            int sortOrder = 1;
            for (Map.Entry<String, String> entry : items.entrySet()) {
                if (!codeItemRepository.existsByGroupIdAndCode(group.getId(), entry.getKey())) {
                    CodeItem item = CodeItem.builder()
                            .groupId(group.getId())
                            .code(entry.getKey())
                            .codeName(entry.getValue())
                            .sortOrder(sortOrder++)
                            .isActive(true)
                            .isDefault(false)
                            .build();
                    codeItemRepository.save(item);
                }
            }
        }
    }
}