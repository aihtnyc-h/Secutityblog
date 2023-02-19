package com.example.replyblog.replay.dto;

import com.example.replyblog.replay.entity.Reply;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplyDto {
    private Long id;
    private String userName;
    private String comments;
    private Long blogId;

    public ReplyDto(Reply reply) {
        this.id = reply.getId();
        this.userName = reply.getUser().getUsername();
        this.comments = reply.getComments();
        this.blogId = blogId;
    }
}
