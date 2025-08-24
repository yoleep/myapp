package com.company.common.dto.menu;

import com.company.common.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuTreeDto {
    
    private Long id;
    private String name;
    private String displayName;
    private String url;
    private String icon;
    private Menu.MenuType menuType;
    private String targetWindow;
    private Integer sortOrder;
    private Integer menuLevel;
    private Boolean isVisible;
    private Boolean isActive;
    private List<MenuTreeDto> children = new ArrayList<>();
    private MenuPermissionDto permissions;
}