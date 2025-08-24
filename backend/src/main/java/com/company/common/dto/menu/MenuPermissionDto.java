package com.company.common.dto.menu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuPermissionDto {
    
    private Boolean canView;
    private Boolean canAccess;
    private Boolean canCreate;
    private Boolean canUpdate;
    private Boolean canDelete;
    private Boolean canExecute;
}