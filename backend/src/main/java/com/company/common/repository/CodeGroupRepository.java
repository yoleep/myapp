package com.company.common.repository;

import com.company.common.entity.CodeGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CodeGroupRepository extends JpaRepository<CodeGroup, Long> {
    
    Optional<CodeGroup> findByGroupCode(String groupCode);
    
    Boolean existsByGroupCode(String groupCode);
    
    List<CodeGroup> findByIsActiveOrderBySortOrder(Boolean isActive);
    
    List<CodeGroup> findAllByOrderBySortOrder();
}