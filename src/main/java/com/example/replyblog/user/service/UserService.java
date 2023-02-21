package com.example.replyblog.user.service;

import com.example.replyblog.blog.dto.MessageDto;
import com.example.replyblog.jwt.JwtUtil;
import com.example.replyblog.user.dto.LoginRequestDto;
import com.example.replyblog.user.dto.SignupRequestDto;
import com.example.replyblog.user.entity.User;
import com.example.replyblog.user.entity.UserRoleEnum;
import com.example.replyblog.user.repository.UserRepository;
import com.example.replyblog.util.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static com.example.replyblog.util.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public ResponseEntity<?> signup(SignupRequestDto signupRequestDto, BindingResult bindingResult) {
        // username 규칙!
        String username = signupRequestDto.getUsername();
//        if (username.length() < 4 || username.length() > 10) {
//            return new ResponseEntity(NOT_CONGITION_USERNAME.getHttpStatus());
//        }
//        if (!username.matches("^[0-9|a-z]*$")) {
//            return new ResponseEntity(NOT_CONGITION_USERNAME.getHttpStatus());
//        }
        String password = signupRequestDto.getPassword();
//        System.out.println(password);
//        if (password.length() < 8 || password.length() > 15) {
//            return new ResponseEntity(NOT_CONGITION_PASSWORD.getHttpStatus());
//        }
//        if (!password.matches("^[0-9|a-z|A-Z]*$")) {
//            return new ResponseEntity(NOT_CONGITION_PASSWORD.getHttpStatus());
//        }
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        if (bindingResult.hasErrors()){
//            return ResponseEntity.badRequest()  // status : bad request
//                    .body(MessageDto.builder()  // body : SuccessResponseDto (statusCode, msg)
//                            .statusCode(HttpStatus.BAD_REQUEST.value())
//                            .msg(bindingResult.getAllErrors().get(0).getDefaultMessage())
//                            .build());
//        }
        }
            // 회원 중복 확인
            Optional<User> found = userRepository.findByUsername(username);
            if (found.isPresent()) {
                return new ResponseEntity(DUPLICATE_USER.getHttpStatus());
//            return ResponseEntity.badRequest()  // status : bad request
//                    .body(MessageDto.builder()  // body : SuccessResponseDto (statusCode, msg)
//                            .statusCode(HttpStatus.BAD_REQUEST.value())
//                            .msg("중복된 사용자가 존재합니다.")
//                            .build());
//        }
            }
                //사용자 ROLE 확인
                UserRoleEnum role = UserRoleEnum.USER;
                if (signupRequestDto.isAdmin()) {
                    if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                        return new ResponseEntity(INVALID_ADMIN_TOKEN.getHttpStatus());
                    }
                    role = UserRoleEnum.ADMIN;
                }
                // 새로운 user 객체를 다시 만들어준 후
                User user = User.builder()
                        .username(username)
                        .password(password)
                        .role(role)
                        .build();
                // DB에 저장하기
                userRepository.save(user);
                return ResponseEntity.ok(new MessageDto("로그인 성공"));
                // ResponseEntity로 Return하기
//        return ResponseEntity.ok()
//                .body(MessageDto.builder()
//                        .statusCode(HttpStatus.OK.value())
//                        .msg("회원가입 성공")
//                        .build()
//                );
            }


    // 로그인 기능
//    @Transactional
//    public ResponseEntity<MessageDto> login(LoginRequestDto loginRequestDto) {
//        // username 규칙!
//        String username = loginRequestDto.getUsername();
//        if (username.length() < 4 || username.length() > 10) {
//            throw new CustomException(NOT_CONGITION_USERNAME);
//        }
//        if (!username.matches("^[0-9|a-z]*$")) {
//            throw new CustomException(NOT_CONGITION_USERNAME);
//        }
//
//
//        String password = loginRequestDto.getPassword();
//        if (password.length() < 8 || password.length() > 15) {
//            throw new CustomException(NOT_CONGITION_PASSWORD);
//        }
//        if (!password.matches("^[0-9|a-z|A-Z]*$")) {
//            throw new CustomException(NOT_CONGITION_PASSWORD);
//        }
//        // 회원 중복 확인
//        Optional<User> user = userRepository.findByUsername(username);
//        if (user.isPresent()) {
//            throw new CustomException(DUPLICATE_USER);
//        }
//
//        //사용자 ROLE 확인
////        UserRoleEnum role = UserRoleEnum.USER;
////        if (loginRequestDto.isAdmin()) {
////            if (!loginRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
////                throw new CustomException(INVALID_ADMIN_TOKEN);
////            }
////            role = UserRoleEnum.ADMIN;
////        }
//        // 새로운 user 객체를 다시 만들어준 후
//           // 비밀번호가 일치하는지 확인하기!
//        if (!user.get().getPassword().equals(password)){
//            throw new CustomException(NOT_MATCH_PASSWORD);
//        }
//        // header 에 들어갈 JWT 설정함.
//        HttpHeaders headers = new HttpHeaders();
//        headers.set(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.get().getUsername(), user.get().getRole()));
//        //reponse.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));
//
//        // ResponseEntity 로 Return
//        return ResponseEntity.ok()
//                .headers(headers)
//                .body(MessageDto.builder()
//                        .statusCode(HttpStatus.OK.value())
//                        .msg("로그인 성공")
//                        .build());
//    }
//}
//    @Transactional(readOnly = true)
//    public ResponseEntity<MessageDto> login(LoginRequestDto loginRequestDto) {
//        String username = loginRequestDto.getUsername();
//        String password = loginRequestDto.getPassword();
//
//        // 회원 중복 확인
//        Optional<User> user = userRepository.findByUsername(username);
//        if (user.isPresent()) {
//            throw new CustomException(DUPLICATE_USER);
//        }
//
//        // 비밀번호가 일치하는지 확인하기!
//        if (!user.get().getPassword().equals(password)){
//            throw new CustomException(NOT_MATCH_PASSWORD);
//        }
//        // header 에 들어갈 JWT 설정함.
//        HttpHeaders headers = new HttpHeaders();
//        headers.set(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.get().getUsername(), user.get().getRole()));
//        //reponse.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));
//
//        // ResponseEntity 로 Return
//        return ResponseEntity.ok()
//                .headers(headers)
//                .body(MessageDto.builder()
//                        .statusCode(HttpStatus.OK.value())
//                        .msg("로그인 성공")
//                        .build());
//    }



    @Transactional(readOnly = true)
    public ResponseEntity<MessageDto> login(LoginRequestDto loginRequestDto, HttpServletResponse reponse) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return new ResponseEntity(NOT_FOUND_USER.getHttpStatus());
        }

        // 비밀번호 일치 확인
        if(!user.get().getPassword().equals(password)){
            return new ResponseEntity(NOT_CONGITION_PASSWORD.getHttpStatus());
        }

        reponse.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.get().getUsername(), user.get().getRole()));
        return ResponseEntity.ok(new MessageDto("로그인 성공"));
//        return ResponseEntity.ok()
//
//                .body(MessageDto.builder()
//                        .statusCode(HttpStatus.OK.value())
//                        .msg("로그인 성공")
//                        .build());
    }
}