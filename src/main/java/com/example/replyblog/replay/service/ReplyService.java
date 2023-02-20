package com.example.replyblog.replay.service;

import com.example.replyblog.blog.entity.Blog;
import com.example.replyblog.blog.repository.BlogRepository;
import com.example.replyblog.jwt.JwtUtil;
import com.example.replyblog.replay.dto.ReplyRequestDto;
import com.example.replyblog.replay.dto.ReplyResponseDto;
import com.example.replyblog.replay.entity.Reply;
import com.example.replyblog.replay.repository.ReplyRepository;
import com.example.replyblog.user.entity.User;
import com.example.replyblog.user.entity.UserRoleEnum;
import com.example.replyblog.user.repository.UserRepository;
import com.example.replyblog.util.CustomException;
import com.example.replyblog.util.ErrorResponse;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static com.example.replyblog.util.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ReplyService {
    // 1) 의존성 주입
    private final ReplyRepository replyRepository;
    private final UserRepository userRepository;
    private final BlogRepository blogRepository;
    private final JwtUtil jwtUtil;

    // 1. 댓글 작성
    @Transactional
    public ResponseEntity<ReplyResponseDto> createComment(Long id, ReplyRequestDto replyRequestDto, HttpServletRequest request) {
        // 1) Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;  // token 의 정보를 임시로 저장하는 곳.

        // 2) 토큰을 검사하여, 유효한 토큰일 경우에만 댓글 작성 가능
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token); // 토큰에서 사용자 정보 가져오기
            } else {
                return new ResponseEntity<ReplyResponseDto>(INVALID_TOKEN.getHttpStatus());
            }
            // 3) id 와 user를 사용하여 blogRepoDB 조회 및 유무
            Optional<Blog> blog = blogRepository.findById(id);
            if (blog.isEmpty()) {
                return new ResponseEntity<ReplyResponseDto>(NOT_FOUND_BLOG.getHttpStatus());
            }

            // 4) 토큰에서 가져온 사용자 정보를 사용하여 DB 조회 -> user 엔티티 get
            Optional<User> user = userRepository.findByUsername(claims.getSubject());
            if (user.isEmpty()) {
                return new ResponseEntity<ReplyResponseDto>(NOT_FOUND_USER.getHttpStatus());
            }

            // 5) 요청받은 DTO 로 DB에 저장할 객체 만들기
            Reply reply = replyRepository.save(Reply.builder()
                    .replyrequestDto(replyRequestDto)
                    .user(user.get())
                    .blog(blog.get())
                    .build()
            );

            // 6) ResponseEntity에 Body 부분에 만든 객체 전달.
            return ResponseEntity.ok()
                    .body(new ReplyResponseDto(reply));
        } else {
            return new ResponseEntity<ReplyResponseDto>(NOT_FOUND_TOKEN.getHttpStatus());
        }
    }

    // 2. 댓글 수정
    @Transactional
    public ResponseEntity<ReplyResponseDto> updateComment(Long id, ReplyRequestDto replyRequestDto, HttpServletRequest request) {
        // 1) Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 2) 토큰을 검사하여, 유효한 토큰일 경우에만 댓글 작성 가능
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            Optional<User> user = userRepository.findByUsername(claims.getSubject());
            if (user.isEmpty()) {
                throw new CustomException(NOT_FOUND_USER);
            }
            // 3) Admin 권한이 있는 친구는 전부 수정, 아닌경우 일부수정.
            UserRoleEnum userRoleEnum = user.get().getRole();
            Optional<Reply> reply;
            // 3-1) Admin 권환인 경우.
            if (userRoleEnum == UserRoleEnum.ADMIN) {
                reply = replyRepository.findById(id);
                if (reply.isEmpty()) { // 일치하는 댓글이 없다면
                    throw new  IllegalArgumentException("댓글이 존재하지 않습니다.");
                }

            } else { // 3-2) User 권한인 경우.
                reply = replyRepository.findByIdAndUser(id, user.get());
                if (reply.isEmpty()) { // 일치하는 댓글이 없다면
                    throw new  IllegalArgumentException("댓글이 존재하지 않습니다.");
                }
            }
            // 4) Comment Update
            reply.get().update(replyRequestDto, user.get());

            // 5) ResponseEntity에 Body 부분에 만든 객체 전달.
            return ResponseEntity.ok()
                    .body(new ReplyResponseDto(reply.get()));
        } else {
            throw new  IllegalArgumentException("댓글이 존재하지 않습니다.");
        }

    }

    // 요구사항 3) 댓글 삭제
    @Transactional
    public ResponseEntity<ErrorResponse> deleteComment(Long id, HttpServletRequest request) {
        // 1) Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 2) 토큰이 있는 경우에만 선택한 댓글 삭제 가능.
        if (token != null) {
            //  2-1) 토큰이 휴효한 토큰인지 판별.
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token); // 토큰에서 사용자 정보 가져오기

            } else {
                throw new  IllegalArgumentException("댓글이 존재하지 않습니다.");
            }

            //  2-2) 토큰에서 가져온 사용자 정보를 사용하여 DB 조회 및 유무 판단.
            Optional<User> user = userRepository.findByUsername(claims.getSubject());
            if (user.isEmpty()) {
                throw new  IllegalArgumentException("댓글이 존재하지 않습니다.");
            }
            // 3) Admin 권한이 있는 친구는 전부 수정, 아닌경우 일부수정.
            UserRoleEnum userRoleEnum = user.get().getRole();
            Optional<Reply> reply;

            // 3-1) Admin 권환인 경우.
            if (userRoleEnum == UserRoleEnum.ADMIN) {
                reply = replyRepository.findById(id);
                if (reply.isEmpty()) { // 일치하는 댓글이 없다면
                    throw new  IllegalArgumentException("댓글이 존재하지 않습니다.");
                }

            } else {    // 3-2) User 권한인 경우.
                reply = replyRepository.findByIdAndUser(id, user.get());
                if (reply.isEmpty()) { // 일치하는 댓글이 없다면
                    throw new  IllegalArgumentException("댓글이 존재하지 않습니다.");
                }
            }

            // 4) Comment Delete
            replyRepository.deleteById(id);

            // 5) ResponseEntity에 Body 부분에 만든 객체 전달.
            return ResponseEntity.ok()
                    .body(ErrorResponse.builder()
                            .statuscode(HttpStatus.OK.value())
                            .msg("댓글 삭제 성공.")
                            .build()
                    );
        } else { // 토큰이 없는 경우
            throw new  IllegalArgumentException("댓글이 존재하지 않습니다.");
        }
    }


}
