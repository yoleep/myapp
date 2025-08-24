package com.company.common.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "system_configs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemConfig extends BaseEntity {
    
    @Column(name = "config_key", unique = true, nullable = false)
    private String configKey;
    
    @Column(name = "config_value", columnDefinition = "TEXT")
    private String configValue;
    
    @Column(name = "config_type")
    @Enumerated(EnumType.STRING)
    private ConfigType configType = ConfigType.STRING;
    
    @Column(name = "config_group")
    private String configGroup;
    
    private String description;
    
    @Column(name = "is_encrypted")
    private Boolean isEncrypted = false;
    
    @Column(name = "is_editable")
    private Boolean isEditable = true;
    
    @Column(name = "validation_regex")
    private String validationRegex;
    
    @Column(name = "default_value")
    private String defaultValue;
    
    public enum ConfigType {
        STRING, NUMBER, BOOLEAN, JSON, FILE, ENCRYPTED
    }
}