package com.example.replyblog.dto;

import com.example.replyblog.blog.entity.Blog;
import com.example.replyblog.replay.dto.ReplyResponseDto;
import com.example.replyblog.util.SuccessCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class AllResponseDto {
    private String msg;
    private int statusCode;
    @Builder
    private AllResponseDto(String msg, int statusCode)
    {
        this.msg = msg;
        this.statusCode = statusCode;
    }

    public static AllResponseDto of(HttpStatus status, String msg)
    {
        return AllResponseDto.builder()
                .statusCode(status.value())
                .msg(msg)
                .build();
    }
    public static AllResponseDto of(SuccessCode successCode)
    {
        return AllResponseDto.builder()
                .statusCode(successCode.getHttpStatus().value())
                .msg(successCode.getMsg())
                .build();
    }
}
