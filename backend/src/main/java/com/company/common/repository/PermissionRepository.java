package com.company.common.repository;

import com.company.common.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    
    Optional<Permission> findByName(String name);
    
    List<Permission> findByResource(String resource);
    
    boolean existsByResourceAndAction(String resource, String action);
    
    @Query("SELECT DISTINCT p.resource FROM Permission p")
    List<String> findDistinctResources();
}