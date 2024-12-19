package com.social.campus.repository;

import com.social.campus.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeRepository extends JpaRepository<Like,Integer> {
    //Integer countByPostId(Integer postId);

   // boolean existsByUser_UserIdAndPost_PostId(Integer userId, Integer postId);

   // @Query("SELECT l FROM Like l WHERE l.user.userId = :userId AND l.post.postId = :postId")
   // Like findByUserIdAndPostId(@Param("postId") Integer postId, @Param("userId") Integer userId);

}
