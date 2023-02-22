package com.example.replyblog.blog.repository;

import com.example.replyblog.blog.entity.Blog;
import com.example.replyblog.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findAllByOrderByModifiedAtDesc();
    Optional<Blog> findByIdAndUser(Long id, User user);

    Blog findByIdAndUserId(Long id, Long id1);
}
