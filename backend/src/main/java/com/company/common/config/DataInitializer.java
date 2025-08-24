package com.company.common.config;

import com.company.common.entity.*;
import com.company.common.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@Slf4j
@Profile("dev")
public class DataInitializer {
    
    private final PasswordEncoder passwordEncoder;
    
    @Bean
    CommandLineRunner init(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PermissionRepository permissionRepository,
                           MenuRepository menuRepository,
                           CodeGroupRepository codeGroupRepository,
                           BoardRepository boardRepository,
                           SystemConfigRepository systemConfigRepository) {
        
        return args -> {
            log.info("Initializing data...");
            
            // Create permissions
            Permission viewPermission = createPermission(permissionRepository, "VIEW", "View", "View resource");
            Permission createPermission = createPermission(permissionRepository, "CREATE", "Create", "Create resource");
            Permission updatePermission = createPermission(permissionRepository, "UPDATE", "Update", "Update resource");
            Permission deletePermission = createPermission(permissionRepository, "DELETE", "Delete", "Delete resource");
            Permission adminPermission = createPermission(permissionRepository, "ADMIN", "Admin", "Admin access");
            
            // Create roles
            Role adminRole = createRole(roleRepository, "ROLE_ADMIN", "Administrator", 
                    new HashSet<>(Arrays.asList(viewPermission, createPermission, updatePermission, deletePermission, adminPermission)));
            Role userRole = createRole(roleRepository, "ROLE_USER", "User", 
                    new HashSet<>(Arrays.asList(viewPermission)));
            
            // Create admin user
            if (!userRepository.existsByEmail("admin@example.com")) {
                User admin = User.builder()
                        .email("admin@example.com")
                        .password(passwordEncoder.encode("admin123"))
                        .firstName("Admin")
                        .lastName("User")
                        .isEmailVerified(true)
                        .roles(new HashSet<>(Arrays.asList(adminRole)))
                        .build();
                userRepository.save(admin);
                log.info("Admin user created");
            }
            
            // Create test user
            if (!userRepository.existsByEmail("user@example.com")) {
                User user = User.builder()
                        .email("user@example.com")
                        .password(passwordEncoder.encode("user123"))
                        .firstName("Test")
                        .lastName("User")
                        .isEmailVerified(true)
                        .roles(new HashSet<>(Arrays.asList(userRole)))
                        .build();
                userRepository.save(user);
                log.info("Test user created");
            }
            
            // Create menus
            createMenuStructure(menuRepository);
            
            // Create code groups and items
            createCommonCodes(codeGroupRepository);
            
            // Create boards
            createBoards(boardRepository);
            
            // Create system configurations
            createSystemConfigs(systemConfigRepository);
            
            log.info("Data initialization completed");
        };
    }
    
    private Permission createPermission(PermissionRepository repository, String name, String displayName, String description) {
        return repository.findByName(name).orElseGet(() -> {
            Permission permission = Permission.builder()
                    .name(name)
                    .displayName(displayName)
                    .description(description)
                    .build();
            return repository.save(permission);
        });
    }
    
    private Role createRole(RoleRepository repository, String name, String displayName, Set<Permission> permissions) {
        return repository.findByName(name).orElseGet(() -> {
            Role role = Role.builder()
                    .name(name)
                    .displayName(displayName)
                    .permissions(permissions)
                    .isSystem(true)
                    .build();
            return repository.save(role);
        });
    }
    
    private void createMenuStructure(MenuRepository repository) {
        if (repository.count() == 0) {
            // Dashboard
            Menu dashboard = Menu.builder()
                    .name("dashboard")
                    .displayName("Dashboard")
                    .url("/dashboard")
                    .icon("dashboard")
                    .sortOrder(1)
                    .menuType(Menu.MenuType.INTERNAL)
                    .build();
            repository.save(dashboard);
            
            // System Management
            Menu system = Menu.builder()
                    .name("system")
                    .displayName("System Management")
                    .icon("settings")
                    .sortOrder(2)
                    .menuType(Menu.MenuType.GROUP)
                    .build();
            system = repository.save(system);
            
            // Sub-menus
            Menu users = Menu.builder()
                    .name("users")
                    .displayName("User Management")
                    .url("/system/users")
                    .icon("people")
                    .parent(system)
                    .sortOrder(1)
                    .menuType(Menu.MenuType.INTERNAL)
                    .build();
            repository.save(users);
            
            Menu menus = Menu.builder()
                    .name("menus")
                    .displayName("Menu Management")
                    .url("/system/menus")
                    .icon("menu")
                    .parent(system)
                    .sortOrder(2)
                    .menuType(Menu.MenuType.INTERNAL)
                    .build();
            repository.save(menus);
            
            Menu codes = Menu.builder()
                    .name("codes")
                    .displayName("Code Management")
                    .url("/system/codes")
                    .icon("code")
                    .parent(system)
                    .sortOrder(3)
                    .menuType(Menu.MenuType.INTERNAL)
                    .build();
            repository.save(codes);
            
            log.info("Menu structure created");
        }
    }
    
    private void createCommonCodes(CodeGroupRepository repository) {
        if (repository.count() == 0) {
            // User Status codes
            CodeGroup userStatus = CodeGroup.builder()
                    .groupId("USER_STATUS")
                    .groupName("User Status")
                    .description("User account status codes")
                    .isSystem(true)
                    .build();
            
            CodeItem active = CodeItem.builder()
                    .codeGroup(userStatus)
                    .codeValue("ACTIVE")
                    .codeLabel("Active")
                    .sortOrder(1)
                    .build();
            
            CodeItem inactive = CodeItem.builder()
                    .codeGroup(userStatus)
                    .codeValue("INACTIVE")
                    .codeLabel("Inactive")
                    .sortOrder(2)
                    .build();
            
            CodeItem locked = CodeItem.builder()
                    .codeGroup(userStatus)
                    .codeValue("LOCKED")
                    .codeLabel("Locked")
                    .sortOrder(3)
                    .build();
            
            userStatus.getCodeItems().addAll(Arrays.asList(active, inactive, locked));
            repository.save(userStatus);
            
            log.info("Common codes created");
        }
    }
    
    private void createBoards(BoardRepository repository) {
        if (repository.count() == 0) {
            Board notice = Board.builder()
                    .boardType(Board.BoardType.NOTICE)
                    .boardName("Notice Board")
                    .description("System notices and announcements")
                    .useComment(false)
                    .build();
            repository.save(notice);
            
            Board free = Board.builder()
                    .boardType(Board.BoardType.FREE)
                    .boardName("Free Board")
                    .description("General discussion board")
                    .build();
            repository.save(free);
            
            log.info("Boards created");
        }
    }
    
    private void createSystemConfigs(SystemConfigRepository repository) {
        if (repository.count() == 0) {
            SystemConfig appName = SystemConfig.builder()
                    .configKey("app.name")
                    .configValue("Common Module System")
                    .configType(SystemConfig.ConfigType.STRING)
                    .configGroup("application")
                    .description("Application name")
                    .build();
            repository.save(appName);
            
            SystemConfig sessionTimeout = SystemConfig.builder()
                    .configKey("session.timeout")
                    .configValue("1800")
                    .configType(SystemConfig.ConfigType.NUMBER)
                    .configGroup("security")
                    .description("Session timeout in seconds")
                    .build();
            repository.save(sessionTimeout);
            
            log.info("System configurations created");
        }
    }
}