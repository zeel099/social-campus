package com.social.campus.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="post_media")
public class PostMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate unique IDs
    private Integer mediaId;

    @Column(nullable = false)
    private String mediaUrl; // URL or path to the media file

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId", nullable = false) // Link to the Post entity
    private Post post;

    private String mediaType; // Type of media (e.g., IMAGE, VIDEO, etc.)

    private Long mediaSize; // Size of the media in bytes
}
