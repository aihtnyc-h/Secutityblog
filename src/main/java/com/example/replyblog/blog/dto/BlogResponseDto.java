package com.example.replyblog.blog.dto;

import com.example.replyblog.blog.entity.Blog;
import com.example.replyblog.replay.dto.ReplyDto;
import com.example.replyblog.replay.dto.ReplyRequestDto;
import com.example.replyblog.replay.dto.ReplyResponseDto;
import lombok.Builder;
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
    private String contents;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private  Long blogLikeCount;
    private List<ReplyResponseDto> commentList = new ArrayList<>();

    private BlogResponseDto(Blog blog, List<ReplyResponseDto> commentList, Long blogLikeCount) {
        this.id = blog.getId();
        this.title = blog.getTitle();
        this.contents = blog.getContents();
        this.username = blog.getUser().getUsername();
        this.createdAt = blog.getCreatedAt();
        this.modifiedAt = blog.getModifiedAt();
        this.blogLikeCount = blogLikeCount;
        this.commentList = commentList;
    }
    @Builder
    public static BlogResponseDto from(Blog blog, List<ReplyResponseDto> commentList, Long blogLikeCount) {
        return BlogResponseDto.builder()
                .blog(blog)
                .commentList(commentList)
                .blogLikeCount(blogLikeCount)
                .build();
    }

    public static BlogResponseDto from(Blog blog)
    {
        return BlogResponseDto.builder()
                .blog(blog)
                .blogLikeCount(0L)
                .commentList(new ArrayList<>())
                .build();
    }
}
