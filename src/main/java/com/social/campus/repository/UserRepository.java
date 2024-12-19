package com.social.campus.repository;

import com.social.campus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface UserRepository extends JpaRepository<User,Integer> {
    // Count followers for a user
    @Query("SELECT COUNT(u) FROM User u JOIN u.following f WHERE f.userId = :userId")
    Integer countFollowersByUserId(@Param("userId") Integer userId);

    // Count following for a user
    @Query("SELECT COUNT(f) FROM User u JOIN u.following f WHERE u.userId = :userId")
    Integer countFollowingByUserId(@Param("userId") Integer userId);

    @Query("SELECT u FROM User u JOIN u.pendingFollowers p WHERE p.userId = :userId")
    List<User> findPendingFollowRequestsByUserId(@Param("userId") Integer userId);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END " +
            "FROM User u JOIN u.blockedUsers b WHERE u.userId = :userId AND b.userId = :blockedUserId")
    boolean isUserBlocked(@Param("userId") Integer userId, @Param("blockedUserId") Integer blockedUserId);

    @Query("SELECT b FROM User u JOIN u.blockedUsers b WHERE u.userId = :userId")
    List<User> findBlockedUsersByUserId(@Param("userId") Integer userId);

    List<User> findByUserNameContainingIgnoreCase(String userName);
}
