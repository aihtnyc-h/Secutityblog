package com.example.replyblog.replay.repository;

import com.example.replyblog.replay.entity.Reply;
import com.example.replyblog.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    Optional<Reply> findByIdAndUser(Long id, User user);
}
