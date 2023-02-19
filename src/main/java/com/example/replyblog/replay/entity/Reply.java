package com.example.replyblog.replay.entity;

import com.example.replyblog.blog.entity.Blog;
import com.example.replyblog.entity.Timestamped;
import com.example.replyblog.user.entity.User;
import com.example.replyblog.replay.dto.ReplyRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Reply extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본키 설정!

    @ManyToOne
    @JoinColumn(name = "BLOG_ID", nullable = false)
    private Blog blog;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Column(nullable = false)
    private String comments;     //댓글 내용

    @Builder
    public Reply(ReplyRequestDto replyrequestDto, Blog blog, User user) {
        this.blog = blog;
        this.user = user;
        this.comments= replyrequestDto.getComments();
    }

    public void update(ReplyRequestDto replyrequestDto, User user) {
        this.comments = replyrequestDto.getComments();
        this.user = user;
    }


}