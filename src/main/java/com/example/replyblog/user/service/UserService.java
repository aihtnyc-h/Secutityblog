package com.example.replyblog.user.service;

import com.example.replyblog.jwt.JwtUtil;
import com.example.replyblog.user.dto.LoginRequestDto;
import com.example.replyblog.user.dto.SignupRequestDto;
import com.example.replyblog.user.entity.User;
import com.example.replyblog.user.entity.UserRoleEnum;
import com.example.replyblog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    @Transactional
    public void signup(SignupRequestDto signupRequestDto){
        String username = signupRequestDto.getUsername();
        System.out.println(username);
        if (username.length() < 4 || username.length() > 10){
            throw new RuntimeException("아이디를 조건에 맞게 입력해주세요.");
        }
        if (!username.matches("^[0-9|a-z]*$")){
            throw new RuntimeException("아이디를 조건에 맞게 입력해주세요.");
        }
        String password = signupRequestDto.getPassword();
        System.out.println(password);
        if (password.length() < 8 || password.length() > 15){
            throw new RuntimeException("비밀번호를 조건에 맞게 입력해주세요.");
        }
        if (!password.matches("^[0-9|a-z|A-Z]*$")){
            throw new RuntimeException("비밀번호를 조건에 맞게 입력해주세요.");
        }

        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()){
            throw  new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        UserRoleEnum role = UserRoleEnum.USER;
        if(signupRequestDto.isAdmin()){
            if(signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)){
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        User user = new User(username,password,role);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public void login(LoginRequestDto loginRequestDto, HttpServletResponse reponse) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );


        if(!user.getPassword().equals(password)){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }

        reponse.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));

    }
}