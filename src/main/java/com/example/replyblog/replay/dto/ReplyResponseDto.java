package com.example.replyblog.replay.dto;

import com.example.replyblog.replay.entity.Reply;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ReplyResponseDto {
    private Long id;
    private String comments;
    private String username;
    private LocalDateTime createdat;
    private LocalDateTime modifiedat;

    public ReplyResponseDto(Reply reply) {
        this.id = reply.getId();
        this.comments = reply.getComments();
        this.createdat = reply.getCreatedAt();
        this.modifiedat = reply.getModifiedAt();
        this.username = reply.getUser().getUsername();
    }
}
