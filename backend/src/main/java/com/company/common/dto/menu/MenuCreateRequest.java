package com.company.common.dto.menu;

import com.company.common.entity.Menu;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuCreateRequest {
    
    @NotBlank(message = "Menu name is required")
    private String name;
    
    @NotBlank(message = "Display name is required")
    private String displayName;
    
    private String url;
    
    private String icon;
    
    @NotNull(message = "Menu type is required")
    private Menu.MenuType menuType;
    
    private String targetWindow = "_self";
    
    private Integer sortOrder = 0;
    
    private Long parentId;
    
    private Boolean isVisible = true;
    
    private Boolean isActive = true;
}