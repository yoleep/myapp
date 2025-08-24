package com.company.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "boards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board extends BaseEntity {
    
    @Column(name = "board_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private BoardType boardType;
    
    @Column(name = "board_name", nullable = false)
    private String boardName;
    
    private String description;
    
    @Column(name = "use_comment")
    private Boolean useComment = true;
    
    @Column(name = "use_attachment")
    private Boolean useAttachment = true;
    
    @Column(name = "use_secret")
    private Boolean useSecret = false;
    
    @Column(name = "use_reply")
    private Boolean useReply = true;
    
    @Column(name = "use_category")
    private Boolean useCategory = false;
    
    @Column(name = "attachment_limit")
    private Integer attachmentLimit = 5;
    
    @Column(name = "attachment_size_limit")
    private Long attachmentSizeLimit = 10485760L; // 10MB
    
    @Column(name = "allowed_extensions")
    private String allowedExtensions = "jpg,jpeg,png,gif,pdf,doc,docx,xls,xlsx,ppt,pptx,zip";
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "is_public")
    private Boolean isPublic = true;
    
    @Column(name = "sort_order")
    private Integer sortOrder = 0;
    
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();
    
    public enum BoardType {
        NOTICE, FREE, FAQ, FILE, GALLERY, QNA
    }
}