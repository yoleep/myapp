package com.company.common.repository;

import com.company.common.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    List<Comment> findByPostIdAndParentIsNullOrderByCreatedAtAsc(Long postId);
    
    List<Comment> findByPostIdOrderByCreatedAtAsc(Long postId);
    
    List<Comment> findByPostIdAndIsDeletedFalseOrderByCreatedAtAsc(Long postId);
    
    Long countByPostId(Long postId);
    
    void deleteByPostId(Long postId);
    
    @Query("SELECT c FROM Comment c WHERE c.isDeleted = false ORDER BY c.createdAt DESC")
    Page<Comment> findRecentComments(Pageable pageable);
}