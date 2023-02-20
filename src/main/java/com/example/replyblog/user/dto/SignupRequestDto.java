package com.example.replyblog.user.dto;

import lombok.Getter;
import lombok.Setter;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Setter
@Getter
public class SignupRequestDto {
    @NotNull(message = "닉네임은 필수입니다.")
    // import org.jetbrains.annotations.NotNull; 을 적용할 때 message를 인식하지 못해서 Bean Validation API을 따로 설정함!
    private String username;
    @NotNull(message = "비밀번호는 필수입니다.")
    private String password;
    private boolean admin = false;
    private String adminToken = "";
}