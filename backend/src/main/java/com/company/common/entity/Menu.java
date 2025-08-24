package com.company.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "menus")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Menu extends BaseEntity {
    
    @Column(nullable = false)
    private String name;
    
    @Column(name = "display_name")
    private String displayName;
    
    private String url;
    
    private String icon;
    
    @Column(name = "menu_type")
    @Enumerated(EnumType.STRING)
    private MenuType menuType = MenuType.INTERNAL;
    
    @Column(name = "target_window")
    private String targetWindow = "_self";
    
    @Column(name = "sort_order")
    private Integer sortOrder = 0;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Menu parent;
    
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC")
    private List<Menu> children = new ArrayList<>();
    
    @Column(name = "menu_level")
    private Integer menuLevel = 0;
    
    @Column(name = "is_visible")
    private Boolean isVisible = true;
    
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RoleMenuPermission> rolePermissions = new HashSet<>();
    
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserMenuPermission> userPermissions = new HashSet<>();
    
    public enum MenuType {
        INTERNAL, EXTERNAL, DIVIDER, GROUP
    }
    
    @PrePersist
    @PreUpdate
    public void calculateLevel() {
        if (parent != null) {
            this.menuLevel = parent.getMenuLevel() + 1;
        } else {
            this.menuLevel = 0;
        }
    }
}