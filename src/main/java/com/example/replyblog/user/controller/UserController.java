package com.example.replyblog.user.controller;

import com.example.replyblog.user.dto.LoginRequestDto;
import com.example.replyblog.user.dto.SignupRequestDto;
import com.example.replyblog.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")
    public ModelAndView signupPage() {
        return new ModelAndView("signup");
    }

    @GetMapping("/login")
    public ModelAndView loginPage() {
        return new ModelAndView("login");
    }

    @PostMapping("/signup")
    public String signup(@RequestBody SignupRequestDto signupRequestDto) {
        userService.signup(signupRequestDto);   //이메일이 왜 안들어갔는지 찾아보 여기서부터 값을 못받아온다아아아아 받아오게 만들자!!
        return "redirect:/api/user/login";
    }

    @ResponseBody
    @PostMapping("/login")  //400에러
    public String login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        userService.login(loginRequestDto, response);
        return "success";
    }


}