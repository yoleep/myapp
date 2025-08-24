package com.company.common.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "role_menu_permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleMenuPermission extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
    
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
}