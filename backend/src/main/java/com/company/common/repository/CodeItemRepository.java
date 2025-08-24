package com.company.common.repository;

import com.company.common.entity.CodeItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CodeItemRepository extends JpaRepository<CodeItem, Long> {
    
    List<CodeItem> findByGroupIdOrderBySortOrder(Long groupId);
    
    List<CodeItem> findByGroupIdAndIsActiveOrderBySortOrder(Long groupId, Boolean isActive);
    
    Optional<CodeItem> findByGroupIdAndCode(Long groupId, String code);
    
    boolean existsByGroupIdAndCode(Long groupId, String code);
    
    @Query("SELECT c FROM CodeItem c WHERE c.groupId = :groupId AND c.parentId IS NULL ORDER BY c.sortOrder")
    List<CodeItem> findRootItemsByGroupId(@Param("groupId") Long groupId);
    
    List<CodeItem> findByParentIdOrderBySortOrder(Long parentId);
    
    @Query("SELECT MAX(c.sortOrder) FROM CodeItem c WHERE c.groupId = :groupId")
    Integer findMaxSortOrderByGroupId(@Param("groupId") Long groupId);
    
    void deleteByGroupId(Long groupId);
    
    List<CodeItem> findByCodeNameContaining(String keyword);
    
    @Query("SELECT c FROM CodeItem c WHERE c.groupId = :groupId AND (c.code LIKE %:keyword% OR c.codeName LIKE %:keyword%)")
    List<CodeItem> searchByGroupIdAndKeyword(@Param("groupId") Long groupId, @Param("keyword") String keyword);
}