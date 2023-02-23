package com.example.replyblog.replay.entity;

import com.example.replyblog.blog.entity.Blog;
import com.example.replyblog.common.Timestamped;
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
    @JoinColumn(name = "blog_Id", nullable = false)
    private Blog blog;

    @ManyToOne
    @JoinColumn(name = "User_Id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String comments;     //댓글 내용

    @Builder
    public Reply(ReplyRequestDto replyrequestDto, User user, Blog blog) {
        this.blog = this.blog;
        this.user = user;
        this.comments= replyrequestDto.getComments();
    }

    public void update(ReplyRequestDto replyrequestDto, User user) {
        this.comments = replyrequestDto.getComments();
        this.user = user;
    }


}