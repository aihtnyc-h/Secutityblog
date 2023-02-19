package com.example.replyblog.replay.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReplyRequestDto {
    public ReplyRequestDto(String comments) {
        this.comments = comments;
    }

    private String comments;


}
