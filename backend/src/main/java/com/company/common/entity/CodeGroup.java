package com.company.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "code_groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeGroup extends BaseEntity {
    
    @Column(name = "group_id", unique = true, nullable = false)
    private String groupId;
    
    @Column(name = "group_name", nullable = false)
    private String groupName;
    
    private String description;
    
    @Column(name = "is_system")
    private Boolean isSystem = false;
    
    @Column(name = "is_cached")
    private Boolean isCached = true;
    
    @OneToMany(mappedBy = "codeGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC")
    private List<CodeItem> codeItems = new ArrayList<>();
}