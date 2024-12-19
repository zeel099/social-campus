package com.social.campus.repository;

import com.social.campus.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post,Integer> {
    Integer countByUserUserId(Integer userId);
    @Query("SELECT COUNT(u) FROM Post p JOIN p.likedBy u WHERE p.postId = :postId")
    Integer countLikesByPostId(@Param("postId") Integer postId);

}
