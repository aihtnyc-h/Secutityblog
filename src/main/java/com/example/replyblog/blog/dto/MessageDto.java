package com.example.replyblog.blog.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MessageDto {
    private String msg;
    private int statusCode;
    @Builder
    public MessageDto(String msg, int statusCode) {
        this.msg = msg;
        this.statusCode = statusCode;
    }
}