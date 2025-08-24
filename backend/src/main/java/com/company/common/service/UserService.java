package com.company.common.service;

import com.company.common.dto.user.UserDto;
import com.company.common.dto.user.UserUpdateRequest;
import com.company.common.entity.Role;
import com.company.common.entity.User;
import com.company.common.exception.BadRequestException;
import com.company.common.exception.ResourceNotFoundException;
import com.company.common.repository.RoleRepository;
import com.company.common.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    
    public Page<UserDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::convertToDto);
    }
    
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return convertToDto(user);
    }
    
    public UserDto updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new BadRequestException("Email already exists");
            }
            user.setEmail(request.getEmail());
        }
        
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        
        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        
        if (request.getProfileImage() != null) {
            user.setProfileImage(request.getProfileImage());
        }
        
        if (request.getNewPassword() != null) {
            if (request.getCurrentPassword() == null) {
                throw new BadRequestException("Current password is required to change password");
            }
            
            if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                throw new BadRequestException("Current password is incorrect");
            }
            
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }
        
        if (request.getIsMfaEnabled() != null) {
            user.setIsMfaEnabled(request.getIsMfaEnabled());
        }
        
        user = userRepository.save(user);
        log.info("User updated: {}", user.getEmail());
        
        return convertToDto(user);
    }
    
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        
        user.setIsActive(false);
        userRepository.save(user);
        log.info("User deactivated: {}", user.getEmail());
    }
    
    public UserDto assignRole(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));
        
        user.getRoles().add(role);
        user = userRepository.save(user);
        
        log.info("Role {} assigned to user {}", role.getName(), user.getEmail());
        
        return convertToDto(user);
    }
    
    public UserDto removeRole(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));
        
        user.getRoles().remove(role);
        user = userRepository.save(user);
        
        log.info("Role {} removed from user {}", role.getName(), user.getEmail());
        
        return convertToDto(user);
    }
    
    public UserDto getUserWithPermissions(Long userId) {
        User user = userRepository.findByEmailWithRoles(
                userRepository.findById(userId)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found"))
                        .getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        return convertToDto(user);
    }
    
    private UserDto convertToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .profileImage(user.getProfileImage())
                .isEmailVerified(user.getIsEmailVerified())
                .isMfaEnabled(user.getIsMfaEnabled())
                .isActive(user.getIsActive())
                .lastLoginAt(user.getLastLoginAt())
                .createdAt(user.getCreatedAt())
                .roles(user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet()))
                .permissions(user.getAuthorities().stream()
                        .map(auth -> auth.getAuthority())
                        .collect(Collectors.toSet()))
                .build();
    }
}