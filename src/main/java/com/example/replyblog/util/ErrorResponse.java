package com.example.replyblog.util;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
public class ErrorResponse {
    private final String msg;
    private final int statuscode;
    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResponse.builder()
                        .msg(errorCode.getMsg())
                        .statuscode(errorCode.getHttpStatus().value())
                        .build());
    }
}
