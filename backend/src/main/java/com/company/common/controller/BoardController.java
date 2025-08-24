package com.company.common.controller;

import com.company.common.dto.board.*;
import com.company.common.service.BoardService;
import com.company.common.util.Auditable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
@Tag(name = "Board Management", description = "APIs for managing boards, posts, and comments")
public class BoardController {
    
    private final BoardService boardService;
    
    // Board APIs
    @GetMapping
    @Operation(summary = "Get all boards", description = "Retrieve a list of all active boards")
    public ResponseEntity<List<BoardDto>> getAllBoards(
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) Boolean isPublic) {
        List<BoardDto> boards = boardService.getAllBoards(isActive, isPublic);
        return ResponseEntity.ok(boards);
    }
    
    @GetMapping("/{boardId}")
    @Operation(summary = "Get board by ID", description = "Retrieve board details by ID")
    public ResponseEntity<BoardDto> getBoardById(@PathVariable Long boardId) {
        BoardDto board = boardService.getBoardById(boardId);
        return ResponseEntity.ok(board);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(action = "CREATE_BOARD")
    @Operation(summary = "Create new board", description = "Create a new board (Admin only)")
    public ResponseEntity<BoardDto> createBoard(@Valid @RequestBody BoardCreateRequest request) {
        BoardDto board = boardService.createBoard(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(board);
    }
    
    @PutMapping("/{boardId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(action = "UPDATE_BOARD")
    @Operation(summary = "Update board", description = "Update board details (Admin only)")
    public ResponseEntity<BoardDto> updateBoard(
            @PathVariable Long boardId,
            @Valid @RequestBody BoardCreateRequest request) {
        BoardDto board = boardService.updateBoard(boardId, request);
        return ResponseEntity.ok(board);
    }
    
    @DeleteMapping("/{boardId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(action = "DELETE_BOARD")
    @Operation(summary = "Delete board", description = "Delete a board (Admin only)")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.noContent().build();
    }
    
    // Post APIs
    @GetMapping("/{boardId}/posts")
    @Operation(summary = "Get posts by board", description = "Retrieve paginated posts for a specific board")
    public ResponseEntity<Page<PostDto>> getPostsByBoard(
            @PathVariable Long boardId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) List<String> tags,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostDto> posts = boardService.getPostsByBoard(boardId, keyword, category, tags, pageable);
        return ResponseEntity.ok(posts);
    }
    
    @GetMapping("/posts/{postId}")
    @Operation(summary = "Get post by ID", description = "Retrieve post details by ID")
    public ResponseEntity<PostDto> getPostById(
            @PathVariable Long postId,
            @RequestParam(required = false) String secretPassword) {
        PostDto post = boardService.getPostById(postId, secretPassword);
        boardService.incrementViewCount(postId);
        return ResponseEntity.ok(post);
    }
    
    @PostMapping("/posts")
    @PreAuthorize("isAuthenticated()")
    @Auditable(action = "CREATE_POST")
    @Operation(summary = "Create new post", description = "Create a new post in a board")
    public ResponseEntity<PostDto> createPost(
            @Valid @RequestBody PostCreateRequest request,
            @Parameter(hidden = true) @RequestAttribute("userId") Long userId) {
        PostDto post = boardService.createPost(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }
    
    @PutMapping("/posts/{postId}")
    @PreAuthorize("isAuthenticated()")
    @Auditable(action = "UPDATE_POST")
    @Operation(summary = "Update post", description = "Update an existing post")
    public ResponseEntity<PostDto> updatePost(
            @PathVariable Long postId,
            @Valid @RequestBody PostUpdateRequest request,
            @Parameter(hidden = true) @RequestAttribute("userId") Long userId) {
        PostDto post = boardService.updatePost(postId, request, userId);
        return ResponseEntity.ok(post);
    }
    
    @DeleteMapping("/posts/{postId}")
    @PreAuthorize("isAuthenticated()")
    @Auditable(action = "DELETE_POST")
    @Operation(summary = "Delete post", description = "Delete a post")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            @Parameter(hidden = true) @RequestAttribute("userId") Long userId) {
        boardService.deletePost(postId, userId);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/posts/{postId}/like")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Like/Unlike post", description = "Toggle like status for a post")
    public ResponseEntity<Map<String, Object>> togglePostLike(
            @PathVariable Long postId,
            @Parameter(hidden = true) @RequestAttribute("userId") Long userId) {
        boolean liked = boardService.togglePostLike(postId, userId);
        Long likeCount = boardService.getPostLikeCount(postId);
        return ResponseEntity.ok(Map.of("liked", liked, "likeCount", likeCount));
    }
    
    // Comment APIs
    @GetMapping("/posts/{postId}/comments")
    @Operation(summary = "Get comments by post", description = "Retrieve comments for a specific post")
    public ResponseEntity<List<CommentDto>> getCommentsByPost(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "false") Boolean includeDeleted) {
        List<CommentDto> comments = boardService.getCommentsByPost(postId, includeDeleted);
        return ResponseEntity.ok(comments);
    }
    
    @PostMapping("/comments")
    @PreAuthorize("isAuthenticated()")
    @Auditable(action = "CREATE_COMMENT")
    @Operation(summary = "Create comment", description = "Add a comment to a post")
    public ResponseEntity<CommentDto> createComment(
            @Valid @RequestBody CommentCreateRequest request,
            @Parameter(hidden = true) @RequestAttribute("userId") Long userId) {
        CommentDto comment = boardService.createComment(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }
    
    @PutMapping("/comments/{commentId}")
    @PreAuthorize("isAuthenticated()")
    @Auditable(action = "UPDATE_COMMENT")
    @Operation(summary = "Update comment", description = "Update an existing comment")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable Long commentId,
            @RequestBody @Valid CommentCreateRequest request,
            @Parameter(hidden = true) @RequestAttribute("userId") Long userId) {
        CommentDto comment = boardService.updateComment(commentId, request.getContent(), userId);
        return ResponseEntity.ok(comment);
    }
    
    @DeleteMapping("/comments/{commentId}")
    @PreAuthorize("isAuthenticated()")
    @Auditable(action = "DELETE_COMMENT")
    @Operation(summary = "Delete comment", description = "Delete a comment")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            @Parameter(hidden = true) @RequestAttribute("userId") Long userId) {
        boardService.deleteComment(commentId, userId);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/comments/{commentId}/like")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Like/Unlike comment", description = "Toggle like status for a comment")
    public ResponseEntity<Map<String, Object>> toggleCommentLike(
            @PathVariable Long commentId,
            @Parameter(hidden = true) @RequestAttribute("userId") Long userId) {
        boolean liked = boardService.toggleCommentLike(commentId, userId);
        Long likeCount = boardService.getCommentLikeCount(commentId);
        return ResponseEntity.ok(Map.of("liked", liked, "likeCount", likeCount));
    }
    
    // Search APIs
    @GetMapping("/search")
    @Operation(summary = "Search posts", description = "Search posts across all boards")
    public ResponseEntity<Page<PostDto>> searchPosts(
            @RequestParam String keyword,
            @RequestParam(required = false) String searchType, // title, content, author, all
            @RequestParam(required = false) Long boardId,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostDto> posts = boardService.searchPosts(keyword, searchType, boardId, dateFrom, dateTo, pageable);
        return ResponseEntity.ok(posts);
    }
    
    @GetMapping("/popular")
    @Operation(summary = "Get popular posts", description = "Retrieve popular posts based on views and likes")
    public ResponseEntity<List<PostDto>> getPopularPosts(
            @RequestParam(defaultValue = "7") Integer days,
            @RequestParam(defaultValue = "10") Integer limit) {
        List<PostDto> posts = boardService.getPopularPosts(days, limit);
        return ResponseEntity.ok(posts);
    }
    
    @GetMapping("/recent-comments")
    @Operation(summary = "Get recent comments", description = "Retrieve recently added comments")
    public ResponseEntity<List<CommentDto>> getRecentComments(
            @RequestParam(defaultValue = "10") Integer limit) {
        List<CommentDto> comments = boardService.getRecentComments(limit);
        return ResponseEntity.ok(comments);
    }
}