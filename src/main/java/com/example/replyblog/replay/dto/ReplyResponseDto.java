package com.example.replyblog.replay.dto;

import com.example.replyblog.replay.entity.Reply;
import lombok.Builder;
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
    private Long commentLikeCount;
    @Builder
    public ReplyResponseDto(Reply reply, Long commentLikeCount) {
        this.id = reply.getId();
        this.comments = reply.getComments();
        this.createdat = reply.getCreatedAt();
        this.modifiedat = reply.getModifiedAt();
        this.username = reply.getUser().getUsername();
        this.commentLikeCount = commentLikeCount;
    }
    public static ReplyResponseDto from(Reply reply, Long commentLikeCount)
    {
        return ReplyResponseDto.builder()
                .reply(reply)
                .commentLikeCount(commentLikeCount)
                .build();
    }

    public static ReplyResponseDto from(Reply reply)
    {
        return ReplyResponseDto.builder()
                .reply(reply)
                .commentLikeCount(0L)
                .build();
    }

}
