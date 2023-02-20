package com.example.replyblog.user.dto;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequestDto {
     private String username;
    private String password;
}
