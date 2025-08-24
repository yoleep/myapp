package com.company.common.repository;

import com.company.common.entity.Board;
import com.company.common.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    
    Page<Post> findByBoardIdAndIsDraftFalseOrderByCreatedAtDesc(Long boardId, Pageable pageable);
    
    Page<Post> findByBoardIdAndCategoryAndIsDraftFalseOrderByCreatedAtDesc(Long boardId, String category, Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE p.board.id = :boardId AND p.isDraft = false AND " +
           "(LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Post> searchPosts(@Param("boardId") Long boardId, @Param("keyword") String keyword, Pageable pageable);
    
    List<Post> findByAuthorIdAndIsDraftTrue(Long authorId);
    
    @Query("SELECT p FROM Post p WHERE p.isNotice = true AND p.noticeStartDate <= :now AND " +
           "(p.noticeEndDate IS NULL OR p.noticeEndDate >= :now) ORDER BY p.createdAt DESC")
    List<Post> findActiveNotices(@Param("now") LocalDateTime now);
    
    List<Post> findByBoardIdAndIsPinnedTrueOrderBySortOrder(Long boardId);
    
    @Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.id = :postId")
    void incrementViewCount(@Param("postId") Long postId);
    
    Page<Post> findByBoardId(Long boardId, Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE p.board.id = :boardId AND " +
           "(LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Post> searchByBoardAndKeyword(@Param("boardId") Long boardId, 
                                       @Param("keyword") String keyword, 
                                       Pageable pageable);
    
    Page<Post> findByBoardIdAndCategory(Long boardId, String category, Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE p.board.id = :boardId AND p.tags IN :tags")
    Page<Post> findByBoardIdAndTagsIn(@Param("boardId") Long boardId, 
                                      @Param("tags") List<String> tags, 
                                      Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "AND (:boardId IS NULL OR p.board.id = :boardId) " +
           "AND (:startDate IS NULL OR p.createdAt >= :startDate) " +
           "AND (:endDate IS NULL OR p.createdAt <= :endDate)")
    Page<Post> searchByTitle(@Param("keyword") String keyword,
                            @Param("boardId") Long boardId,
                            @Param("startDate") LocalDateTime startDate,
                            @Param("endDate") LocalDateTime endDate,
                            Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "AND (:boardId IS NULL OR p.board.id = :boardId) " +
           "AND (:startDate IS NULL OR p.createdAt >= :startDate) " +
           "AND (:endDate IS NULL OR p.createdAt <= :endDate)")
    Page<Post> searchByContent(@Param("keyword") String keyword,
                              @Param("boardId") Long boardId,
                              @Param("startDate") LocalDateTime startDate,
                              @Param("endDate") LocalDateTime endDate,
                              Pageable pageable);
    
    @Query("SELECT p FROM Post p JOIN p.author a WHERE " +
           "(LOWER(a.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:boardId IS NULL OR p.board.id = :boardId) " +
           "AND (:startDate IS NULL OR p.createdAt >= :startDate) " +
           "AND (:endDate IS NULL OR p.createdAt <= :endDate)")
    Page<Post> searchByAuthor(@Param("keyword") String keyword,
                             @Param("boardId") Long boardId,
                             @Param("startDate") LocalDateTime startDate,
                             @Param("endDate") LocalDateTime endDate,
                             Pageable pageable);
    
    @Query("SELECT p FROM Post p JOIN p.author a WHERE " +
           "(LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:boardId IS NULL OR p.board.id = :boardId) " +
           "AND (:startDate IS NULL OR p.createdAt >= :startDate) " +
           "AND (:endDate IS NULL OR p.createdAt <= :endDate)")
    Page<Post> searchAll(@Param("keyword") String keyword,
                        @Param("boardId") Long boardId,
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate,
                        Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE p.createdAt >= :since " +
           "ORDER BY p.viewCount DESC, p.likeCount DESC")
    Page<Post> findPopularPosts(@Param("since") LocalDateTime since, Pageable pageable);
}