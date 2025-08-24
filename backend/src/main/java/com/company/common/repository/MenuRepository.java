package com.company.common.repository;

import com.company.common.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    
    List<Menu> findByParentIsNullOrderBySortOrder();
    
    @Query("SELECT m FROM Menu m WHERE m.parent IS NULL AND m.isActive = true ORDER BY m.sortOrder")
    List<Menu> findActiveRootMenus();
}