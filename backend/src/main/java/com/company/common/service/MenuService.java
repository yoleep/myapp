package com.company.common.service;

import com.company.common.dto.menu.MenuCreateRequest;
import com.company.common.dto.menu.MenuDto;
import com.company.common.dto.menu.MenuTreeDto;
import com.company.common.entity.Menu;
import com.company.common.entity.User;
import com.company.common.exception.ResourceNotFoundException;
import com.company.common.repository.MenuRepository;
import com.company.common.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MenuService {
    
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;
    
    @Cacheable(value = "menuTree", key = "#roleId + '_' + #userId")
    public List<MenuTreeDto> getMenuTree(Long roleId, Long userId) {
        List<Menu> rootMenus = menuRepository.findActiveRootMenus();
        return buildMenuTree(rootMenus);
    }
    
    public List<MenuDto> getAllMenus() {
        return menuRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public MenuDto getMenuById(Long id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found with id: " + id));
        return convertToDto(menu);
    }
    
    @CacheEvict(value = "menuTree", allEntries = true)
    public MenuDto createMenu(MenuCreateRequest request) {
        Menu menu = Menu.builder()
                .name(request.getName())
                .displayName(request.getDisplayName())
                .url(request.getUrl())
                .icon(request.getIcon())
                .menuType(request.getMenuType())
                .targetWindow(request.getTargetWindow())
                .sortOrder(request.getSortOrder())
                .isVisible(request.getIsVisible())
                .build();
        
        if (request.getParentId() != null) {
            Menu parent = menuRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent menu not found"));
            menu.setParent(parent);
        }
        
        menu = menuRepository.save(menu);
        log.info("Menu created: {}", menu.getName());
        
        return convertToDto(menu);
    }
    
    @CacheEvict(value = "menuTree", allEntries = true)
    public MenuDto updateMenu(Long id, MenuCreateRequest request) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found with id: " + id));
        
        menu.setName(request.getName());
        menu.setDisplayName(request.getDisplayName());
        menu.setUrl(request.getUrl());
        menu.setIcon(request.getIcon());
        menu.setMenuType(request.getMenuType());
        menu.setTargetWindow(request.getTargetWindow());
        menu.setSortOrder(request.getSortOrder());
        menu.setIsVisible(request.getIsVisible());
        menu.setIsActive(request.getIsActive());
        
        if (request.getParentId() != null && !request.getParentId().equals(menu.getParent()?.getId())) {
            Menu parent = menuRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent menu not found"));
            menu.setParent(parent);
        } else if (request.getParentId() == null) {
            menu.setParent(null);
        }
        
        menu = menuRepository.save(menu);
        log.info("Menu updated: {}", menu.getName());
        
        return convertToDto(menu);
    }
    
    @CacheEvict(value = "menuTree", allEntries = true)
    public void deleteMenu(Long id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found with id: " + id));
        
        menuRepository.delete(menu);
        log.info("Menu deleted: {}", menu.getName());
    }
    
    @Cacheable(value = "userMenuTree", key = "#userId")
    public List<MenuTreeDto> getUserMenuTree(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        // This is a simplified version. In production, you would check user permissions
        List<Menu> rootMenus = menuRepository.findActiveRootMenus();
        return buildMenuTree(rootMenus);
    }
    
    private List<MenuTreeDto> buildMenuTree(List<Menu> menus) {
        List<MenuTreeDto> tree = new ArrayList<>();
        for (Menu menu : menus) {
            if (menu.getIsActive() && menu.getIsVisible()) {
                MenuTreeDto dto = convertToTreeDto(menu);
                if (menu.getChildren() != null && !menu.getChildren().isEmpty()) {
                    dto.setChildren(buildMenuTree(menu.getChildren()));
                }
                tree.add(dto);
            }
        }
        return tree;
    }
    
    private MenuDto convertToDto(Menu menu) {
        return MenuDto.builder()
                .id(menu.getId())
                .name(menu.getName())
                .displayName(menu.getDisplayName())
                .url(menu.getUrl())
                .icon(menu.getIcon())
                .menuType(menu.getMenuType())
                .targetWindow(menu.getTargetWindow())
                .sortOrder(menu.getSortOrder())
                .parentId(menu.getParent() != null ? menu.getParent().getId() : null)
                .menuLevel(menu.getMenuLevel())
                .isVisible(menu.getIsVisible())
                .isActive(menu.getIsActive())
                .build();
    }
    
    private MenuTreeDto convertToTreeDto(Menu menu) {
        return MenuTreeDto.builder()
                .id(menu.getId())
                .name(menu.getName())
                .displayName(menu.getDisplayName())
                .url(menu.getUrl())
                .icon(menu.getIcon())
                .menuType(menu.getMenuType())
                .targetWindow(menu.getTargetWindow())
                .sortOrder(menu.getSortOrder())
                .menuLevel(menu.getMenuLevel())
                .isVisible(menu.getIsVisible())
                .isActive(menu.getIsActive())
                .children(new ArrayList<>())
                .build();
    }
}