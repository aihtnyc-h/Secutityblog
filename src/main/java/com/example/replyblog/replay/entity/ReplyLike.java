package com.example.replyblog.replay.entity;

import com.example.replyblog.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class ReplyLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "Reply_ID",nullable = false)
    private Reply reply;

    @ManyToOne
    @JoinColumn(name = "USER_ID",nullable = false)
    private User user;

    // 생성자
    @Builder
    private ReplyLike(Reply reply, User user)
    {
        this.reply = reply;
        this.user = user;
    }

    public static ReplyLike of(Reply reply, User user)
    {
        return ReplyLike.builder()
                .reply(reply)
                .user(user)
                .build();
    }
}
