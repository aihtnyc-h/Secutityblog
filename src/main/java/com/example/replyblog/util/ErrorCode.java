package com.example.replyblog.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    NOT_CONGITION_USERNAME(HttpStatus.BAD_REQUEST, "닉네임이 조건에 맞지 않습니다."),
    NOT_CONGITION_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 조건에 맞지 않습니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "토큰이 유효하지 않습니다."),
    AUTHORIZATION(HttpStatus.BAD_REQUEST, "작성자만 수정/삭제할 수 있습니다."),
    DUPLICATED_USERNAME(HttpStatus.BAD_REQUEST, "중복된 username 입니다"),
    NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "회원을 찾을 수 없습니다."),
    NOT_FOUND_BLOG(HttpStatus.BAD_REQUEST, "게시글을 찾을 수 없습니다."),
    NOT_FOUND_COMMENT(HttpStatus.BAD_REQUEST, "댓글을 찾을 수 없습니다."),
    DUPLICATE_USER(HttpStatus.BAD_REQUEST, "중복된 닉네임입니다."),
    NOT_MATCH_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    NOT_MATCH_USERNAME(HttpStatus.BAD_REQUEST, "토큰이 일치하지 않습니다."),
    INVALID_ADMIN_TOKEN(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    NOT_FOUND_TOKEN (HttpStatus.BAD_REQUEST, "토큰을 찾을 수 없습니다."),
    NO_AUTHORITY(HttpStatus.BAD_REQUEST, "토큰을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String msg;
}
