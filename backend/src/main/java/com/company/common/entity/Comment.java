package com.company.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;
    
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt ASC")
    private List<Comment> children = new ArrayList<>();
    
    @Column(name = "comment_level")
    private Integer commentLevel = 0;
    
    @Column(name = "like_count")
    private Long likeCount = 0L;
    
    @Column(name = "dislike_count")
    private Long dislikeCount = 0L;
    
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
    
    @Column(name = "is_anonymous")
    private Boolean isAnonymous = false;
    
    @Column(name = "is_secret")
    private Boolean isSecret = false;
    
    @Column(name = "secret_password")
    private String secretPassword;
    
    @Column(columnDefinition = "TEXT")
    private String metadata;
    
    @PrePersist
    @PreUpdate
    public void calculateLevel() {
        if (parent != null) {
            this.commentLevel = parent.getCommentLevel() + 1;
        } else {
            this.commentLevel = 0;
        }
    }
}