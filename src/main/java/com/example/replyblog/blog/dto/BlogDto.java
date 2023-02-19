package com.example.replyblog.blog.dto;

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
}
