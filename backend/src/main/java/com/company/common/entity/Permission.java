package com.company.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission extends BaseEntity {
    
    @Column(unique = true, nullable = false)
    private String name;
    
    @Column(name = "display_name")
    private String displayName;
    
    private String description;
    
    @Column(name = "resource_type")
    private String resourceType;
    
    @Column(name = "permission_type")
    @Enumerated(EnumType.STRING)
    private PermissionType permissionType;
    
    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles = new HashSet<>();
    
    public enum PermissionType {
        VIEW, CREATE, UPDATE, DELETE, EXECUTE, ADMIN
    }
}