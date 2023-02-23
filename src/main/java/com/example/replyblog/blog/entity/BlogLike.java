package com.example.replyblog.blog.entity;

import com.example.replyblog.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class BlogLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "BLOG_ID",nullable = false)
    private Blog blog;

    @ManyToOne
    @JoinColumn(name = "USER_ID",nullable = false)
    private User user;

    @Builder
    private BlogLike(Blog blog, User user)
    {
        this.blog =blog;
        this.user = user;
    }

    public static BlogLike of(Blog blog, User user)
    {
        return BlogLike.builder()
                .blog(blog)
                .user(user)
                .build();
    }

}
