package com.example.replyblog.replay.service;

import com.example.replyblog.blog.dto.BlogRequestDto;
import com.example.replyblog.blog.entity.Blog;
import com.example.replyblog.blog.repository.BlogRepository;
import com.example.replyblog.dto.AllResponseDto;
import com.example.replyblog.jwt.JwtUtil;
import com.example.replyblog.replay.dto.ReplyRequestDto;
import com.example.replyblog.replay.dto.ReplyResponseDto;
import com.example.replyblog.replay.entity.Reply;
import com.example.replyblog.replay.repository.ReplyLikeRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.replyblog.util.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ReplyService {
    // 1) 의존성 주입
    private final ReplyRepository replyRepository;
    private final BlogRepository blogRepository;
    private final ReplyLikeRepository replyLikeRepository;
    private final JwtUtil jwtUtil;

    // 1. 댓글 작성
    @Transactional
    public ResponseEntity<ReplyResponseDto> createComment(Long id, ReplyRequestDto replyRequestDto, User user) {
        Optional<Blog> blog = blogRepository.findById(id);
        if (blog.isEmpty()) {
            throw new CustomException(NOT_FOUND_BLOG);
        }
            Reply reply = replyRepository.save(new Reply(replyRequestDto, user, blog.get()));
            return ResponseEntity.ok().body(ReplyResponseDto.from(reply));
        }

//    public ResponseEntity<ReplyResponseDto> createComment(Long id, ReplyRequestDto replyRequestDto, HttpServletRequest request) {
//        // 1) Request에서 Token 가져오기
//        String token = jwtUtil.resolveToken(request);
//        Claims claims;  // token 의 정보를 임시로 저장하는 곳.
//
//        // 2) 토큰을 검사하여, 유효한 토큰일 경우에만 댓글 작성 가능
//        if (token != null) {
//            if (jwtUtil.validateToken(token)) {
//                claims = jwtUtil.getUserInfoFromToken(token); // 토큰에서 사용자 정보 가져오기
//            } else {
//                return new ResponseEntity(INVALID_TOKEN.getHttpStatus());
//            }
//            // 3) id 와 user를 사용하여 blogRepoDB 조회 및 유무
//            Optional<Blog> blog = blogRepository.findById(id);
//            if (blog.isEmpty()) {
//                return new ResponseEntity(NOT_FOUND_BLOG.getHttpStatus());
//            }
//            blog.isEmpty();
//            new ResponseEntity(NOT_FOUND_BLOG.getHttpStatus());
//            // 4) 토큰에서 가져온 사용자 정보를 사용하여 DB 조회 -> user 엔티티 get
//            Optional<User> user = userRepository.findByUsername(claims.getSubject());
////            if (user.isEmpty()) {
////                //return new ResponseEntity<ReplyResponseDto>(NOT_FOUND_USER.getHttpStatus());
////                return new ResponseEntity(NOT_FOUND_USER.getHttpStatus());
////            }
//            user.isEmpty();
//            new ResponseEntity(NOT_FOUND_USER.getHttpStatus());
//            // 5) 요청받은 DTO 로 DB에 저장할 객체 만들기
//            Reply reply = replyRepository.save(Reply.builder()
//                    .replyrequestDto(replyRequestDto)
//                    .user(user.get())
//                    .blog(blog.get())
//                    .build()
//            );
//
//            // 6) ResponseEntity에 Body 부분에 만든 객체 전달.
//            return ResponseEntity.ok()
//                    .body(new ReplyResponseDto(reply));
//        } else {
//            return new ResponseEntity(NOT_FOUND_TOKEN.getHttpStatus());
//        }
//    }

    // 2. 댓글 수정
    @Transactional
    public ResponseEntity<ReplyResponseDto> updateComment(Long id,ReplyRequestDto replyRequestDto, User user) {
        UserRoleEnum userRoleEnum =user.getRole();
        Optional<Reply> reply;
        //List<ReplyResponseDto> commentList = new ArrayList<>();

        if (userRoleEnum == UserRoleEnum.ADMIN) {
            reply = replyRepository.findByIdAndUser(id, user);
            if(reply.isEmpty()) {
                throw new CustomException(NOT_FOUND_BLOG);
            }
        } else {
            reply = replyRepository.findByIdAndUser(id, user);
            if (reply.isEmpty()){
                throw new CustomException(AUTHORIZATION);
            }
        }
        reply.get().update(replyRequestDto, user);
        return ResponseEntity.ok()
                .body(ReplyResponseDto.from(reply.get())
                );
    }

    // 요구사항 3) 댓글 삭제
    @Transactional
    public ResponseEntity<AllResponseDto> deletComment(Long id, User user) {
        UserRoleEnum userRoleEnum =user.getRole();
        Reply reply;
        if (userRoleEnum == UserRoleEnum.ADMIN) {
            // 입력 받은 게시글 id와 일치 여부 DB 확인
            reply = replyRepository.findById(id).orElseThrow(
                    () -> new CustomException(NOT_FOUND_BLOG)
            );
        } else {
            // 입력받은 게시글 id, 토큰에서 가져온  userId와 일치하는 DB 조회
            reply = replyRepository.findByIdAndUser(id, user).orElseThrow(
                    () -> new CustomException(AUTHORIZATION)
            );
        }
        replyRepository.deleteById(id);
        // ResponseEntity 에 Body 부분에 객체 전달
        return ResponseEntity.ok()
                .body(AllResponseDto.builder()
                        .statusCode(HttpStatus.OK.value())
                        .msg("게시글 삭제 성공")
                        .build()
                );
    }

//    public ResponseEntity<ErrorResponse> deleteComment(Long id, HttpServletRequest request) {
//        // 1) Request에서 Token 가져오기
//        String token = jwtUtil.resolveToken(request);
//        Claims claims;
//
//        // 2) 토큰이 있는 경우에만 선택한 댓글 삭제 가능.
//        if (token != null) {
//            //  2-1) 토큰이 휴효한 토큰인지 판별.
//            if (jwtUtil.validateToken(token)) {
//                claims = jwtUtil.getUserInfoFromToken(token); // 토큰에서 사용자 정보 가져오기
//
//            } else {
//                return new ResponseEntity(INVALID_TOKEN.getHttpStatus());
//            }
//
//            //  2-2) 토큰에서 가져온 사용자 정보를 사용하여 DB 조회 및 유무 판단.
//            Optional<User> user = userRepository.findByUsername(claims.getSubject());
////            if (user.isEmpty()) {
////                return new ResponseEntity(NOT_FOUND_USER.getHttpStatus());
////            }
//            user.isEmpty();
//            new ResponseEntity(NOT_FOUND_USER.getHttpStatus());
//            // 3) Admin 권한이 있는 친구는 전부 수정, 아닌경우 일부수정.
//            UserRoleEnum userRoleEnum = user.get().getRole();
//            Optional<Reply> reply;
//
//            // Admin 권환인 경우.
//            if (userRoleEnum == UserRoleEnum.ADMIN) {
//                reply = replyRepository.findById(id);
////                if (reply.isEmpty()) { // 일치하는 댓글이 없다면
////                    return new ResponseEntity(NOT_FOUND_COMMENT.getHttpStatus());
////                }
//                reply.isEmpty();
//                new ResponseEntity(NOT_FOUND_COMMENT.getHttpStatus());
//            } else {    // User 권한인 경우.
//                reply = replyRepository.findByIdAndUser(id, user.get());
////                if (reply.isEmpty()) { // 일치하는 댓글이 없다면
////                    return new ResponseEntity(NOT_FOUND_COMMENT.getHttpStatus());
////                }
//                reply.isEmpty();
//                new ResponseEntity(NOT_FOUND_COMMENT.getHttpStatus());
//            }
//
//            // 4) Comment Delete
//            replyRepository.deleteById(id);
//
//            // 5) ResponseEntity에 Body 부분에 만든 객체 전달.
//            return ResponseEntity.ok()
//                    .body(ErrorResponse.builder()
//                            .statuscode(HttpStatus.OK.value())
//                            .msg("댓글 삭제 성공.")
//                            .build()
//                    );
//        } else { // 토큰이 없는 경우
//            return new ResponseEntity(NOT_FOUND_TOKEN.getHttpStatus());
//        }
//    }


}
