package com.social.campus.payload;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserDto {

    private Integer userId; // Unique identifier for the user

    private String first_name;

    private String last_name;

    private String userName;

    private String email; // User's email address

    private String collegeName; // User's college name

    private String password;

    private Integer batchYear; // User's batch year

    private String course; // User's course or department

    private LocalDateTime createdAt; // Account creation timestamp

    private LocalDateTime updatedAt; // Account update timestamp

    private String profilePictureUrl; // User's profile picture URL

    private String bio; // User's bio or description
}
