package com.social.campus.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name="post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate unique IDs for posts
    private Integer postId;

    @ManyToOne(fetch = FetchType.LAZY) // Many posts can belong to one user
    @JoinColumn(name = "userId", nullable = false) // Foreign key to the User table
    private User user;

    private String content; // Text content of the post (optional)

    private String mediaUrl; // URL of the media (image/video) attached to the post

    private Integer likesCount = 0; // Number of likes on the post (default to 0)

    private Integer commentsCount = 0; // Number of comments on the post (default to 0)

    private LocalDateTime createdAt; // Timestamp when the post was created

    private LocalDateTime updatedAt; // Timestamp when the post was last updated

    @ManyToMany
    @JoinTable(
            name = "post_likes",
            joinColumns = @JoinColumn(name = "postId"),
            inverseJoinColumns = @JoinColumn(name = "userId")
    )
    private Set<User> likedBy = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
