package com.example.replyblog.blog.dto;

import com.example.replyblog.blog.entity.Blog;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

@Getter
@RequiredArgsConstructor
public class BlogDto<T> {
    //    private final ResponseStatus status;
    private final String status;
    //    private final String message;
    private final T data;

    public static class blogrequestDto {
        public blogrequestDto(Blog blog, String username) {
        }
    }

    public static class Response {
        public Response(Blog blog, String username) {
        }
    }
}
