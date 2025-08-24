package com.company.common.service;

import com.company.common.dto.menu.MenuPermissionDto;
import com.company.common.entity.Menu;
import com.company.common.entity.Role;
import com.company.common.entity.User;
import com.company.common.repository.MenuRepository;
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
public class MenuPermissionService {
    
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;
    
    private static final String PERMISSION_CACHE = "menuPermissions";
    private static final String USER_MENU_CACHE = "userMenus";
    
    @Cacheable(value = PERMISSION_CACHE, key = "#userId + ':' + #menuId")
    public MenuPermissionDto getUserMenuPermission(Long userId, Long menuId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("Menu not found"));
        
        return calculatePermissions(user, menu);
    }
    
    @Cacheable(value = USER_MENU_CACHE, key = "#userId")
    public Map<Long, MenuPermissionDto> getUserMenuPermissions(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<Menu> allMenus = menuRepository.findAll();
        Map<Long, MenuPermissionDto> permissions = new HashMap<>();
        
        for (Menu menu : allMenus) {
            permissions.put(menu.getId(), calculatePermissions(user, menu));
        }
        
        return permissions;
    }
    
    public List<Menu> getAccessibleMenus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<Menu> allMenus = menuRepository.findByIsActiveTrue();
        
        return allMenus.stream()
                .filter(menu -> hasAccess(user, menu))
                .sorted(Comparator.comparing(Menu::getSortOrder)
                        .thenComparing(Menu::getId))
                .collect(Collectors.toList());
    }
    
    public List<Menu> getAccessibleMenusByType(Long userId, Menu.MenuType menuType) {
        return getAccessibleMenus(userId).stream()
                .filter(menu -> menu.getMenuType() == menuType)
                .collect(Collectors.toList());
    }
    
    @Transactional
    @CacheEvict(value = {PERMISSION_CACHE, USER_MENU_CACHE}, allEntries = true)
    public void grantMenuPermission(Long menuId, Long roleId, MenuPermissionDto permissions) {
        log.info("Granting menu permissions - Menu: {}, Role: {}", menuId, roleId);
        
        // Implementation would typically involve a MenuRolePermission entity
        // For now, this is a placeholder for the actual implementation
        // You would need to create a MenuRolePermission entity to store these permissions
    }
    
    @Transactional
    @CacheEvict(value = {PERMISSION_CACHE, USER_MENU_CACHE}, allEntries = true)
    public void revokeMenuPermission(Long menuId, Long roleId) {
        log.info("Revoking menu permissions - Menu: {}, Role: {}", menuId, roleId);
        
        // Implementation would typically involve removing MenuRolePermission records
    }
    
    @Transactional
    @CacheEvict(value = {PERMISSION_CACHE, USER_MENU_CACHE}, allEntries = true)
    public void copyPermissions(Long sourceMenuId, Long targetMenuId) {
        log.info("Copying permissions from menu {} to menu {}", sourceMenuId, targetMenuId);
        
        // Implementation would copy all role permissions from source to target menu
    }
    
    public boolean hasPermission(Long userId, Long menuId, String permission) {
        MenuPermissionDto permissions = getUserMenuPermission(userId, menuId);
        
        return switch (permission.toUpperCase()) {
            case "VIEW" -> permissions.getCanView();
            case "ACCESS" -> permissions.getCanAccess();
            case "CREATE" -> permissions.getCanCreate();
            case "UPDATE" -> permissions.getCanUpdate();
            case "DELETE" -> permissions.getCanDelete();
            case "EXECUTE" -> permissions.getCanExecute();
            default -> false;
        };
    }
    
    public boolean canAccessUrl(Long userId, String url) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Find menu by URL
        Optional<Menu> menuOpt = menuRepository.findByUrl(url);
        if (menuOpt.isEmpty()) {
            // If no menu is defined for this URL, allow access
            return true;
        }
        
        return hasAccess(user, menuOpt.get());
    }
    
    private MenuPermissionDto calculatePermissions(User user, Menu menu) {
        // Default permissions
        boolean canView = false;
        boolean canAccess = false;
        boolean canCreate = false;
        boolean canUpdate = false;
        boolean canDelete = false;
        boolean canExecute = false;
        
        // Check if menu is active and visible
        if (!menu.getIsActive() || !menu.getIsVisible()) {
            return MenuPermissionDto.builder()
                    .canView(false)
                    .canAccess(false)
                    .canCreate(false)
                    .canUpdate(false)
                    .canDelete(false)
                    .canExecute(false)
                    .build();
        }
        
        // Check user roles
        Set<String> userRoles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        
        // Admin has full access
        if (userRoles.contains("ROLE_ADMIN")) {
            return MenuPermissionDto.builder()
                    .canView(true)
                    .canAccess(true)
                    .canCreate(true)
                    .canUpdate(true)
                    .canDelete(true)
                    .canExecute(true)
                    .build();
        }
        
        // Calculate permissions based on menu type and user roles
        if (menu.getMenuType() == Menu.MenuType.PUBLIC) {
            canView = true;
            canAccess = true;
        } else if (menu.getMenuType() == Menu.MenuType.INTERNAL) {
            // Internal menus require authentication
            canView = true;
            canAccess = true;
            
            // Additional permissions based on roles
            if (userRoles.contains("ROLE_MANAGER")) {
                canCreate = true;
                canUpdate = true;
                canExecute = true;
            }
        } else if (menu.getMenuType() == Menu.MenuType.ADMIN) {
            // Admin menus are restricted
            if (userRoles.contains("ROLE_MANAGER")) {
                canView = true;
            }
        }
        
        // Apply custom role-based permissions
        // This would typically come from a MenuRolePermission table
        
        return MenuPermissionDto.builder()
                .canView(canView)
                .canAccess(canAccess)
                .canCreate(canCreate)
                .canUpdate(canUpdate)
                .canDelete(canDelete)
                .canExecute(canExecute)
                .build();
    }
    
    private boolean hasAccess(User user, Menu menu) {
        MenuPermissionDto permissions = calculatePermissions(user, menu);
        return permissions.getCanAccess();
    }
    
    @CacheEvict(value = {PERMISSION_CACHE, USER_MENU_CACHE}, allEntries = true)
    public void refreshPermissionCache() {
        log.info("Refreshing menu permission cache");
    }
    
    public Map<String, List<Menu>> getMenusByPermissionLevel(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<Menu> allMenus = menuRepository.findByIsActiveTrue();
        Map<String, List<Menu>> menusByPermission = new HashMap<>();
        
        List<Menu> viewableMenus = new ArrayList<>();
        List<Menu> accessibleMenus = new ArrayList<>();
        List<Menu> editableMenus = new ArrayList<>();
        List<Menu> adminMenus = new ArrayList<>();
        
        for (Menu menu : allMenus) {
            MenuPermissionDto perm = calculatePermissions(user, menu);
            
            if (perm.getCanDelete() || perm.getCanExecute()) {
                adminMenus.add(menu);
            } else if (perm.getCanCreate() || perm.getCanUpdate()) {
                editableMenus.add(menu);
            } else if (perm.getCanAccess()) {
                accessibleMenus.add(menu);
            } else if (perm.getCanView()) {
                viewableMenus.add(menu);
            }
        }
        
        menusByPermission.put("VIEW_ONLY", viewableMenus);
        menusByPermission.put("ACCESSIBLE", accessibleMenus);
        menusByPermission.put("EDITABLE", editableMenus);
        menusByPermission.put("ADMIN", adminMenus);
        
        return menusByPermission;
    }
    
    public boolean validateMenuHierarchyPermissions(Long userId, Long parentMenuId, Long childMenuId) {
        // Validate that user has appropriate permissions for menu hierarchy operations
        MenuPermissionDto parentPerms = getUserMenuPermission(userId, parentMenuId);
        MenuPermissionDto childPerms = getUserMenuPermission(userId, childMenuId);
        
        // User must have update permission on parent and at least view permission on child
        return parentPerms.getCanUpdate() && childPerms.getCanView();
    }
}