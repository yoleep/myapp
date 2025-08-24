package com.company.common.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "code_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeItem extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private CodeGroup codeGroup;
    
    @Column(name = "code_value", nullable = false)
    private String codeValue;
    
    @Column(name = "code_label", nullable = false)
    private String codeLabel;
    
    @Column(name = "code_label_en")
    private String codeLabelEn;
    
    private String description;
    
    @Column(name = "sort_order")
    private Integer sortOrder = 0;
    
    @Column(name = "parent_code")
    private String parentCode;
    
    @Column(columnDefinition = "TEXT")
    private String attributes; // JSON string for additional attributes
    
    @Column(name = "is_default")
    private Boolean isDefault = false;
}