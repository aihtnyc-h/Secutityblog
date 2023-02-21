package com.example.replyblog.user.controller;

import com.example.replyblog.blog.dto.MessageDto;
import com.example.replyblog.user.dto.LoginRequestDto;
import com.example.replyblog.user.dto.SignupRequestDto;
import com.example.replyblog.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

//    @GetMapping("/signup")
//    public ModelAndView signupPage() {
//        return new ModelAndView("signup");
//    }
//
//    @GetMapping("/login")
//    public ModelAndView loginPage() {
//        return new ModelAndView("login");
//    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignupRequestDto signupRequestDto, BindingResult bindingResult) {
        //userService.signup(signupRequestDto);   //이메일이 왜 안들어갔는지 찾아보 여기서부터 값을 못받아온다아아아아 받아오게 만들자!!
        return userService.signup(signupRequestDto, bindingResult);
    }

    @ResponseBody
    @PostMapping("/login")
    public ResponseEntity<MessageDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto, HttpServletResponse reponse) {
        //userService.login(loginRequestDto);
        return userService.login(loginRequestDto, reponse);
    }
//ResponseEntity<MessageDto>

}