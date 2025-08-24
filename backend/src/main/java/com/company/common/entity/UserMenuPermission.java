package com.company.common.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_menu_permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMenuPermission extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;
    
    @Column(name = "can_view")
    private Boolean canView = true;
    
    @Column(name = "can_access")
    private Boolean canAccess = true;
    
    @Column(name = "can_create")
    private Boolean canCreate = false;
    
    @Column(name = "can_update")
    private Boolean canUpdate = false;
    
    @Column(name = "can_delete")
    private Boolean canDelete = false;
    
    @Column(name = "can_execute")
    private Boolean canExecute = false;
    
    @Column(name = "is_favorite")
    private Boolean isFavorite = false;
    
    @Column(name = "is_override")
    private Boolean isOverride = false;
}