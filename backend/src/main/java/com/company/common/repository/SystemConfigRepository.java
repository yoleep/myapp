package com.company.common.repository;

import com.company.common.entity.SystemConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SystemConfigRepository extends JpaRepository<SystemConfig, Long> {
    
    Optional<SystemConfig> findByConfigKey(String configKey);
    
    List<SystemConfig> findByConfigGroup(String configGroup);
    
    List<SystemConfig> findByCategory(String category);
    
    boolean existsByConfigKey(String configKey);
    
    @Query("SELECT DISTINCT s.category FROM SystemConfig s")
    List<String> findDistinctCategories();
    
    List<SystemConfig> findByConfigKeyContaining(String query);
    
    List<SystemConfig> findByConfigKeyContainingOrConfigValueContaining(String keyQuery, String valueQuery);
}