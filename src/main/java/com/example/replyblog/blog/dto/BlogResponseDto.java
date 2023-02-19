package com.example.replyblog.blog.dto;

import com.example.replyblog.blog.entity.Blog;
import com.example.replyblog.replay.dto.ReplyDto;
import com.example.replyblog.replay.dto.ReplyRequestDto;
import com.example.replyblog.replay.dto.ReplyResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class BlogResponseDto {
    private Long id;
    private String title;
    private String userName;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedat;
    private List<ReplyResponseDto> commentList = new ArrayList<>();


    public BlogResponseDto(Blog blog) {
        this.id = blog.getId();
        this.title = blog.getTitle();
        this.contents = blog.getContents();
        this.userName = blog.getUser().getUsername();
        this.createdAt = blog.getCreatedAt();
        this.modifiedat = blog.getModifiedAt();
    }
    public BlogResponseDto(Blog blog, List<ReplyResponseDto> commentList) {
        this.id = blog.getId();
        this.title = blog.getTitle();
        this.contents = blog.getContents();
        this.userName = blog.getUser().getUsername();
        this.createdAt = blog.getCreatedAt();
        this.modifiedat = blog.getModifiedAt();
        this.commentList = commentList;
    }
}
