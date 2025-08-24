package com.company.common.dto.menu;

import com.company.common.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuDto {
    
    private Long id;
    private String name;
    private String displayName;
    private String url;
    private String icon;
    private Menu.MenuType menuType;
    private String targetWindow;
    private Integer sortOrder;
    private Long parentId;
    private Integer menuLevel;
    private Boolean isVisible;
    private Boolean isActive;
}