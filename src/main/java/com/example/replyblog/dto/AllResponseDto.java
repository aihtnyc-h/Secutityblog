package com.example.replyblog.dto;

import com.example.replyblog.blog.entity.Blog;
import com.example.replyblog.replay.dto.ReplyResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class AllResponseDto {
    private Long id;
    private String title;
    private String contents;
    private String username;
    private LocalDateTime createdat;
    private LocalDateTime modifiedat;
    private List<ReplyResponseDto> commentList = new ArrayList<>();


    public AllResponseDto(Blog blog, List<ReplyResponseDto> commentList) {
        this.id = blog.getId();
        this.title = blog.getTitle();
        this.contents = blog.getContents();
        this.username = blog.getUser().getUsername();
        this.createdat = blog.getCreatedAt();
        this.modifiedat = blog.getModifiedAt();
        this.commentList = commentList;
    }
}
