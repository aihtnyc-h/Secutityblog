package com.example.replyblog.replay.repository;

import com.example.replyblog.replay.entity.ReplyLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReplyLikeRepository extends JpaRepository<ReplyLike,Long> {
    // id1 ==  commentId, id2 == UserId
//    Optional<ReplyLike> findByCommentIdAndUserId(Long id1, Long id2);
//    void deleteByCommentIdAndUserId(Long id1, Long id2);
    Long countReplyLikesByReplyId(Long replyId);


}
