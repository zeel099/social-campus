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
@Table(name="stories")
public class Story {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer storyId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    private String contentUrl;

    @ManyToMany
    @JoinTable(
            name = "story_views",
            joinColumns = @JoinColumn(name = "storyId"),
            inverseJoinColumns = @JoinColumn(name = "userId")
    )
    private Set<User> viewedBy = new HashSet<>();

    private Integer viewCount = 0;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;
}
