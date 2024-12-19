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
@Table(name="user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate unique IDs
    private Integer userId;

    private String first_name; // Full name of the user

    private String last_name;

    @Column(name="user_name")
    private String userName;

    private String email; // Email for login and communication

    private String password; // Encrypted password for authentication

    private String collegeName; // College name

    private Integer batchYear; // Batch start year (e.g., 2020)

    private String course; // Course or department (e.g., Computer Science)

    private String profilePictureUrl; // URL to the user's profile picture

    private String bio; // A short bio or about me section

    private LocalDateTime createdAt; // Account creation timestamp

    private LocalDateTime updatedAt; // Profile update timestamp

    private LocalDateTime lastLogin; // Last login timestamp

    private Boolean isActive; // Whether the account is active or deactivated

    private String role; // Role of the user (e.g., STUDENT, ADMIN)

    // Many-to-Many Relationship for Following
    @ManyToMany
    @JoinTable(
            name = "user_following",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "followed_id")
    )
    private Set<User> following = new HashSet<>();

    // Many-to-Many Relationship for Followers
    @ManyToMany(mappedBy = "following")
    private Set<User> followers = new HashSet<>();

    @ManyToMany(mappedBy = "likedBy")
    private Set<Post> likedPosts = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_saved_posts",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "postId")
    )
    private Set<Post> savedPosts = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_pending_followers",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "pending_follower_id")
    )
    private Set<User> pendingFollowers = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_blocked",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "blocked_user_id")
    )
    private Set<User> blockedUsers = new HashSet<>();

    @ManyToMany(mappedBy = "blockedUsers")
    private Set<User> blockedByUsers = new HashSet<>();

}
