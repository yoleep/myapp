package com.company.common.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    
    @Email(message = "Invalid email format")
    private String email;
    
    private String firstName;
    
    private String lastName;
    
    private String phoneNumber;
    
    private String profileImage;
    
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String newPassword;
    
    private String currentPassword;
    
    private Boolean isMfaEnabled;
}