package com.company.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    
    private String category;
    
    @Column(name = "view_count")
    private Long viewCount = 0L;
    
    @Column(name = "like_count")
    private Long likeCount = 0L;
    
    @Column(name = "dislike_count")
    private Long dislikeCount = 0L;
    
    @Column(name = "is_notice")
    private Boolean isNotice = false;
    
    @Column(name = "is_pinned")
    private Boolean isPinned = false;
    
    @Column(name = "is_secret")
    private Boolean isSecret = false;
    
    @Column(name = "secret_password")
    private String secretPassword;
    
    @Column(name = "notice_start_date")
    private LocalDateTime noticeStartDate;
    
    @Column(name = "notice_end_date")
    private LocalDateTime noticeEndDate;
    
    @ElementCollection
    @CollectionTable(name = "post_tags", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "tag")
    private Set<String> tags = new HashSet<>();
    
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt ASC")
    private List<Comment> comments = new ArrayList<>();
    
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostAttachment> attachments = new ArrayList<>();
    
    @Column(name = "is_draft")
    private Boolean isDraft = false;
    
    @Column(name = "published_at")
    private LocalDateTime publishedAt;
    
    @Column(name = "post_type")
    private String postType;
    
    @Column(name = "allow_comments")
    private Boolean allowComments = true;
    
    @Column(name = "is_anonymous")
    private Boolean isAnonymous = false;
    
    @Column(name = "parent_post_id")
    private Long parentPostId;
    
    @Column(columnDefinition = "TEXT")
    private String metadata;
    
    private String tags;
    
    @Column(name = "sort_order")
    private Integer sortOrder = 0;
}