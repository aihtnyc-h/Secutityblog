package com.example.replyblog.blog.service;

import com.example.replyblog.blog.dto.BlogRequestDto;
import com.example.replyblog.blog.dto.BlogResponseDto;
import com.example.replyblog.blog.entity.Blog;
import com.example.replyblog.blog.entity.BlogLike;
import com.example.replyblog.blog.repository.BlogLikeRepository;
import com.example.replyblog.blog.repository.BlogRepository;
import com.example.replyblog.dto.AllResponseDto;
import com.example.replyblog.jwt.JwtUtil;
import com.example.replyblog.replay.dto.ReplyResponseDto;
import com.example.replyblog.replay.entity.Reply;
import com.example.replyblog.replay.repository.ReplyLikeRepository;
import com.example.replyblog.user.entity.User;
import com.example.replyblog.user.entity.UserRoleEnum;

import com.example.replyblog.util.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.replyblog.util.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class BlogService {
    // 0) DI 넣어주기
    private final BlogLikeRepository blogLikeRepository;
    private final BlogRepository blogRepository;
    private final ReplyLikeRepository replyLikeRepository;
    private final JwtUtil jwtUtil;

    // 1. 전체 게시글 목록 조회
    // 게시글 전체 조회하기 (유저 이름을 기준! -> 기존 id와 user의 username을 같게 만들어주기!
    @Transactional(readOnly = true)
    public ResponseEntity<List<BlogResponseDto>> getBlogs() {
        // 1) 객체선언하기!
        List<Blog> blogList = blogRepository.findAllByOrderByModifiedAtDesc();
        List<BlogResponseDto> blogResponseList = new ArrayList<>();
        // 2) 각각의 게시글 마다 댓글들을 전부 가져오기
        for (Blog blog : blogList) {
            List<ReplyResponseDto> commentList = new ArrayList<>();
            for (Reply reply : blog.getComments()) {
                commentList.add(ReplyResponseDto.from(reply, replyLikeRepository.countReplyLikesByReplyId(reply.getId())));
                // 이너 클래스 고치기 제발좀 고치자 자고 일어나서 하자
            }
            blogResponseList.add(BlogResponseDto.from(blog, commentList, blogLikeRepository.countBlogLikesByBlogId(blog.getId())));
        }
        // 3) 요청받은 DTO를 DB에 저장할 객체 만들기
        return ResponseEntity.ok().body(blogResponseList);
    }

    //  선택한 게시글 조회
    @Transactional(readOnly = true)
    public ResponseEntity<BlogResponseDto> getBlog(Long id) {

        Optional<Blog> blog = blogRepository.findById(id);
        if (blog.isEmpty()) {
            throw new CustomException(NOT_FOUND_BLOG);
        }

        List<ReplyResponseDto> commentList = new ArrayList<>();
        for (Reply reply : blog.get().getComments()) {
            commentList.add(ReplyResponseDto.from
                    (reply, replyLikeRepository.countReplyLikesByReplyId(reply.getId())));
        }

        return ResponseEntity.ok()
                .body(BlogResponseDto.from(blog.get(), commentList, blogLikeRepository.countBlogLikesByBlogId(blog.get().getId())));
    }
//    @Transactional(readOnly = true)
//    public ResponseEntity<BlogResponseDto> updateBlog(Long id, BlogRequestDto blogRequestDto, User user) {
//        UserRoleEnum userRoleEnum = user.getRole();
//        Blog blog;
//        if (userRoleEnum == UserRoleEnum.ADMIN) {
//            // 입력 받은 게시글 id와 일치하는 DB 조회
//            blog = blogRepository.findById(id).orElseThrow(
//                    () -> new CustomException(NOT_FOUND_BLOG)
//            );
//        } else {
//            // 입력 받은 게시글 id, 토큰에서 가져온 userId와 일치하는 DB 조회
//            blog = blogRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
//                    () -> new CustomException(AUTHORIZATION)
//            );
//        }
//        blog.update(blogRequestDto, user);
//
//        List<ReplyResponseDto> commentList = new ArrayList<>();
//        for (Reply reply : blog.getCommentList()) {
//            commentList.add(ReplyResponseDto.from(reply,replyLikeRepository.countreplyLikesByreplyId(reply.getId())));      }
//
//        return ResponseEntity.ok()
//                .body(BlogResponseDto.from(blog, commentList,blogLikeRepository.countBlogLikesByBlogId(blog.getId())));
//    }
//    public ResponseEntity<BlogResponseDto> getBlog(long id) {
//        // 1) id를 사용하여 DB조회 및 유무
//        Blog blog = blogRepository.findById(id).orElseThrow(
//                () -> new CustomException(NOT_FOUND_TOKEN) //DB에 존재하지 않으면
//        );
//        // 2) 가져온 blog에 comments을 commentList에 저장하기
//        List<ReplyResponseDto> commentList = new ArrayList<>();
//        for (Reply reply : blog.getComments()) {
//            commentList.add(new ReplyResponseDto(reply));
//        }
//        // 3) 요청받은 DTO를 DB에 저장할 객체 만들기
//        return ResponseEntity.ok().body(new BlogResponseDto(blog, commentList));
//    }
//        @Transactional(readOnly = true)
//    public List<Blog> getBlog() {
//
//        return blogRepository.findAllByOrderByModifiedAtDesc();
//    }
//    @Transactional(readOnly = true)
//
//    public List<BlogResponseDto> getBlog() {
//        List <Blog> blogList = blogRepository.findAllByOrderByModifiedAtDesc(); // 리스트 형태로 내림차순 정리
//        List <BlogResponseDto> blogResponseDtoList = new ArrayList<>(); // 리스트 생성
//        for (Blog blog : blogList) {
//            BlogResponseDto tmp = new BlogResponseDto(blog);
//            blogResponseDtoList.add(tmp);
//        }
//        return blogResponseDtoList;
//    }
//    public List<BlogResponseDto> getBlog() {
//        List<BlogResponseDto> blogResponseDtoList = new ArrayList<>();
//        List<Blog> blogList;
//
//        blogList = blogRepository.findAll();
//
//        for (Blog i : blogList) {
//            list.add(new BlogResponseDto(i));
//        }
//        return list;
//    }


    // 선택한 게시글 조회   500에 아이디가 존재하지 않습니다.

    //    public Optional<Blog> getBlogs(Long id) {
//        Blog blog = blogRepository.findById(id).orElseThrow(
//                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
//        );
//        return blogRepository.findById(id);
//    }

    // 게시글 작성
    @Transactional
    public ResponseEntity<BlogResponseDto> createBlog(BlogRequestDto blogrequestDto, User user) {
        Blog blog = blogRepository.saveAndFlush(new Blog(blogrequestDto, user));
        //return blogService.createBlog(blogrequestDto, userDetails.getUser());
        return ResponseEntity.ok()
                .body(BlogResponseDto.from(blog));
    }
//        // 1) Request에서 Token 가져오기
//        String token = jwtUtil.resolveToken(request);
//        Claims claims; // token 의 정보를 임시로 저장하는 곳.
//
//        // 2) 토큰이 있는 경우에만 게시글 추가 가능.
//        if (token != null) {
//            // 2-1) 토큰이 휴효한 토큰인지 판별
//            if (jwtUtil.validateToken(token)) {
//                claims = jwtUtil.getUserInfoFromToken(token); // 토큰에서 사용자 정보 가져오기
//            } else {
//                return new ResponseEntity(INVALID_TOKEN.getHttpStatus());
//            }
//
//            // 2-2) 토큰에서 가져온 사용자 정보를 사용하여 DB 조회 및 유무판단.
//            Optional<User> user = userRepository.findByUsername(claims.getSubject());
////            if (user.isEmpty()) {
////                return new ResponseEntity(NOT_FOUND_USER.getHttpStatus());
////            }
//            user.isEmpty();
//            new ResponseEntity(NOT_FOUND_USER.getHttpStatus()); // Optional은 if가 대체가 가능하기 때문에 if를 안쓰고 만드는 것이 좋다!
//
//            // 3) 요청받은 DTO 로 DB에 저장할 객체 만들기
//            Blog blog = blogRepository.save(Blog.builder()
//                    .blogRequestDto(blogrequestDto)
//                    .user(user.get())
//                    .build());
//
//            // 4) ResponseEntity에 Body 부분에 만든 객체 전달.
//            return ResponseEntity.ok()
//                    .body(new BlogResponseDto(blog));
//        } else { // 토큰이 없는 경우.
//            return new ResponseEntity(INVALID_TOKEN.getHttpStatus());
//        }
    //}

//    @Transactional
//    public ResponseEntity<?> createBlog(Long id, BlogRequestDto blogrequestDto, HttpServletRequest request) {
//        // 1) Request에서 Token 가져오기
//        String token = jwtUtil.resolveToken(request);
//        Claims claims;
//
//        // 2) 토큰이 있는 경우에만 게시글 작성 가능
//        if (token != null) {
//            // Token 검증 유효한 토큰인지 판별
//            if (jwtUtil.validateToken(token)) {
//                // 토큰에서 사용자 정보 가져오기
//                claims = jwtUtil.getUserInfoFromToken(token);
//            } else {
//                throw new IllegalArgumentException("Token Error");
//            }
//            // 3) 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
//            // 회원 토큰 확인
//            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
//                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
//            );
//            // 4) id와 user를 사용하여 DB조회
//            Blog blog = blogRepository.saveAndFlush(Blog.builder()
//                    .blogRequestDto(blogrequestDto)
//                    .user(user)
//                    .build());
//            return ResponseEntity.ok().body(new BlogResponseDto(blog));
////            if (blog.getUser().getUsername().equals(user.getUsername()) || user.getRole() == UserRoleEnum.ADMIN) {
////                blog.creat(blogrequestDto, user);
////
////                return ResponseEntity.ok(new BlogDto.blogrequestDto(blog, blog.getUser().getUsername()));
//            } else {
//                return ErrorResponse.toResponseEntity(new CustomException(ErrorCode.NO_AUTHORITY).getErrorCode());
//            }

//        } else {
//            return ErrorResponse.toResponseEntity(new CustomException(ErrorCode.INVALID_TOKEN).getErrorCode());

//    }
//    @Transactional
//    public ResponseEntity<BlogResponseDto> createBlog(BlogRequestDto blogrequestDto, HttpServletRequest request) {
//        // 1) Request에서 Token 가져오기
//        String token = jwtUtil.resolveToken(request);
//        Claims claims;
//
//        // 2) 토큰이 있는 경우에만 게시글 작성 가능
//        if (token != null) {
//            // Token 검증 유효한 토큰인지 판별
//            if (jwtUtil.validateToken(token)) {
//                // 토큰에서 사용자 정보 가져오기
//                claims = jwtUtil.getUserInfoFromToken(token);
//            } else {
//                throw new IllegalArgumentException("Token Error");
//            }
//            // 3) 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
//            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
//                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
//            );
//
//            // 4) 요청받은 DTO로 DB에 저잘할 객체 만들기
//            Blog blog = blogRepository.saveAndFlush(Blog.builder()
//                    .blogRequestDto(blogrequestDto)
//                    .user(user)
//                  .build());
//            // 5) 요청받은 DTO를 DB에 저장할 객체 만들기
//            return ResponseEntity.ok().body(new BlogResponseDto(blog));
//        } else {
//            return null;
//        }
//    }
// List 버전
//            // 사용자 권한 가져와서 ADMIN 이면 전체 조회, USER 면 본인이 추가한 부분 조회
//            UserRoleEnum userRoleEnum = user.getRole();
//            System.out.println("role = " + userRoleEnum);
//
//            List<BlogResponseDto> list = new ArrayList<>();
//            List<Blog> blogList;
//
//            if (userRoleEnum == UserRoleEnum.USER) {
//                // 사용자 권한이 USER일 경우
//                blogList = blogRepository.findAllByUserName(user.getUsername());
//            } else {
//                blogList = blogRepository.findAll();
//            }
//
//            for (Blog blog : blogList) {
//                list.add(new BlogResponseDto(blog));
//            }
//
//            return list;
//
//        } else {
//            return null;
//        }
//        }
//    }

    // 선택한 게시글 수정
    // - 토큰을 검사한 후, 유효한 토큰이면서 해당 사용자가 작성한 게시글만 수정 가능
    //- 제목, 작성 내용을 수정하고 수정된 게시글을 Client 로 반환하기
//    @Transactional
//    public Object updateBlog(Long id, BlogRequestDto requestDto) {
//        if (!validatePassword(id, requestDto.getPassword())) {      //비밀번호가 같지 않을 때 사용!
//            String success;
//            return blogRepository.findById(id);
//        }
//
//        Blog blog = blogRepository.findById(id).orElseThrow(
//                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
//        );
//        blog.update(requestDto);
//        return blog.getId();
//    }

    // 게시글 수정하기
    @Transactional
    public ResponseEntity<BlogResponseDto> updateBlog(Long id, BlogRequestDto blogRequestDto, User user) {
        UserRoleEnum userRoleEnum = user.getRole();
        Optional<Blog> blog;
        List<ReplyResponseDto> commentList = new ArrayList<>();

        if (userRoleEnum == UserRoleEnum.ADMIN) {
            blog = blogRepository.findById(id);
            if (blog.isEmpty()) {
                throw new CustomException(NOT_FOUND_BLOG);
            }
        } else {
            blog = blogRepository.findByIdAndUserId(id, user.getId());
            if (blog.isEmpty()) {
                throw new CustomException(AUTHORIZATION);
            }
        }

        blog.get().update(blogRequestDto, user);

        for (Reply reply : blog.get().getComments()) {
            commentList.add(ReplyResponseDto.from(reply, replyLikeRepository.countReplyLikesByReplyId(reply.getId())));
        }

        return ResponseEntity.ok()
                .body(BlogResponseDto.from(blog.get(), commentList, blogLikeRepository.countBlogLikesByBlogId(blog.get().getId())));
    }

//    public ResponseEntity<BlogResponseDto> updateBlog(Long id, BlogRequestDto blogrequestDto, HttpServletRequest request, User user) {
//        // 1) Request에서 Token 가져오기
//        String token = jwtUtil.resolveToken(request);
//        Claims claims;
//
//        // 2) 토큰이 있는 경우에만 게시글 작성 가능
//        if (token != null) {
//            // Token 검증 유효한 토큰인지 판별
//            if (jwtUtil.validateToken(token)) {
//                // 토큰에서 사용자 정보 가져오기
//                claims = jwtUtil.getUserInfoFromToken(token);
//            } else {
//                return new ResponseEntity(INVALID_TOKEN.getHttpStatus());
//            }
//            // 3) 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
//            // 회원 토큰 확인
////            Optional<User> user = userRepository.findByUsername(claims.getSubject());
////            if (user.isEmpty()) {
////                return new ResponseEntity(NOT_FOUND_USER.getHttpStatus());
////            }
//            //Optional<User> user = userRepository.findByUsername(claims.getSubject());
////            if (user.isEmpty()) {
////                return new ResponseEntity(NOT_FOUND_USER.getHttpStatus());
////
////            }
//            //user.isEmpty();
//            new ResponseEntity(NOT_FOUND_USER.getHttpStatus());
//
//            // 4) id와 user를 사용하여 DB조회
//            Optional<Blog> blog = blogRepository.findByIdAndUser(id, user.get());
////            if (blog.isEmpty()) {
////                return new ResponseEntity(AUTHORIZATION.getHttpStatus());
////            }
//            blog.isEmpty();
//            new ResponseEntity(AUTHORIZATION.getHttpStatus());
//
////            Blog blog = blogRepository.findById(id).orElseThrow(
////                    () -> new CustomException(ErrorCode.AUTHORIZATION)
////            );
////            if (blog.getUser().getUsername().equals(user.getUsername()) || user.getRole() == UserRoleEnum.ADMIN) {
//            blog.get().update(blogrequestDto, user.get());
//
//            List<ReplyResponseDto> commentList = new ArrayList<>();
//            for (Reply reply : blog.get().getComments()) {
//                commentList.add(new ReplyResponseDto(reply));
//            }
//            return ResponseEntity.ok()
//                    .body(new BlogResponseDto(blog.get(), commentList));
//        } else { // 토큰이 존재하지 않을 경우.
//            return new ResponseEntity(INVALID_TOKEN.getHttpStatus());
//        }
//                return ResponseEntity.ok(new BlogDto.Response(blog, blog.getUser().getUsername()));
//            } else {
//                return ErrorResponse.toResponseEntity(new CustomException(NO_AUTHORITY).getErrorCode());
//            }
//        } else {
//            return ErrorResponse.toResponseEntity(new CustomException(INVALID_TOKEN).getErrorCode());
//        }
//        UserRoleEnum userRoleEnum = user.getRole();
//        Blog blog;
//
//        if (userRoleEnum == UserRoleEnum.ADMIN) {
//            // 입력받은 게시글 id와 일치하는지 DB조회
//            blog = blogRepository.findById(id).orElseThrow(
//                    () -> new CustomException(NOT_FOUND_BLOG)
//            );
//        } else {
//            // 입력 받은 게시글 id와 토큰에서 가져온 user와 일치하는지 DB 조회
//            blog = blogRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
//                    () -> new CustomException(AUTHORIZATION)
//            );
//        }
//        blog.update(blogRequestDto, user);
//
//        List<ReplyResponseDto> commentList = new ArrayList<>();
//        for (Reply reply : blog.getCommentList()) {
//            commentList.add(ReplyResponseDto(reply,replyLikeRepository.countCommentLikesByCommentId(reply.getId())));      }
//
//        return ResponseEntity.ok()
//                .body(BlogResponseDto(blog, commentList,blogLikeRepository.countBlogLikesByBlogId(blog.getId())));



//            Optional<Blog> blog = blogRepository.findByIdAndUserId(id, user.get());
//            }if (blog.isEmpty()) {
//                throw new IllegalArgumentException("댓글이 존재하지 않습니다.");
//            }
//                    () -> new IllegalArgumentException("존재하지 않는 사용자입니다.")
//            );
    // 5) 블로그에 업데이트
//            blog.get().update(blogrequestDto, user.get());
//            BlogResponseDto blogResponseDto = new BlogResponseDto(blog);
//            // 6)   가져온 blog에 Reply를 commentList에 저장하기
//            List<ReplyResponseDto> commentlist = new ArrayList<>();
//            for (Reply reply : blog.getComments()) {
//                commentlist.add(new ReplyResponseDto(reply));
//            }
//            // 7) ResponseEntity에 Body 부분에 만든 객체 전달.
//            return ResponseEntity.ok().body(new BlogResponseDto(blog));
//        } else {
//            return null;
//        }
//    }
//            if (blog.getUser().getUsername().equals(user.getUsername()) || user.getRole() == UserRoleEnum.ADMIN) {
//                blog.update(blogrequestDto, user);
//
//                return ResponseEntity.ok(new BlogDto.blogrequestDto(blog, blog.getUser().getUsername()));
//            } else {
//                return ErrorResponse.toResponseEntity(new CustomException(ErrorCode.NO_AUTHORITY).getErrorCode());
//            }
//
//        } else {
//            return ErrorResponse.toResponseEntity(new CustomException(ErrorCode.INVALID_TOKEN).getErrorCode());
//        }
//    }


    // List로 사용했을 때
    // 사용자 권한 가져와서 ADMIN 이면 전체 조회, USER 면 본인이 추가한 부분 조회
//            UserRoleEnum userRoleEnum = user.getRole();
//            System.out.println("role = " + userRoleEnum);
//
//            List<BlogResponseDto> list = new ArrayList<>();
//            List<Blog> blogList;
//
//            if (userRoleEnum == UserRoleEnum.USER) {
//                // 사용자 권한이 USER일 경우
//                blogList = blogRepository.findAllByUserName(user.getUsername());
//            } else {
//                blogList = blogRepository.findAll();
//            }
//
//            for (Blog blog : blogList) {
//                list.add(new BlogResponseDto(blog));
//            }
//
//            return list;
//
//        } else {
//            return null;
//        }
//    }
    // 선택한 게시글 삭제
//    @Transactional
//     public BlogDto<? extends Object> deleteBlog(Long id, String password) {
//     if (!validatePassword(id, password)) {
//         return new BlogDto<BlogMessageDto>("failure", new BlogMessageDto("비밀번호를 다시 확인하세요."));
//     }
//     blogRepository.deleteById(id);
//     return new BlogDto<BlogMessageDto>("success", new BlogMessageDto("게시글 삭제 성공"));
// }

    // 게시글 삭제
    @Transactional
    public ResponseEntity<AllResponseDto> deleteBlog(Long id, User user) {
        UserRoleEnum userRoleEnum =user.getRole();
        Blog blog;
        if (userRoleEnum == UserRoleEnum.ADMIN) {
            // 입력 받은 게시글 id와 일치 여부 DB 확인
            blog = blogRepository.findById(id).orElseThrow(
                    () -> new CustomException(NOT_FOUND_BLOG)
            );
        } else {
            // 입력받은 게시글 id, 토큰에서 가져온  userId와 일치하는 DB 조회
            blog = blogRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                    () -> new CustomException(AUTHORIZATION)
            );
        }
        blogRepository.deleteById(id);
        // ResponseEntity 에 Body 부분에 객체 전달
        return ResponseEntity.ok()
                .body(AllResponseDto.builder()
                        .statusCode(HttpStatus.OK.value())
                        .msg("게시글 삭제 성공")
                        .build()
                );
    }

//    public ResponseEntity<ErrorResponse> deleteBlog(Long id, HttpServletRequest request) {
//        // 1) Request에서 Token 가져오기
//        String token = jwtUtil.resolveToken(request);
//        Claims claims;
//
//        // 2) 토큰이 있는 경우에만 게시글 작성 가능
//        if (token != null) {
//            // Token 검증 유효한 토큰인지 판별
//            if (jwtUtil.validateToken(token)) {
//                // 토큰에서 사용자 정보 가져오기
//                claims = jwtUtil.getUserInfoFromToken(token);
//            } else {
//                return new ResponseEntity(INVALID_TOKEN.getHttpStatus());
//            }
//            // 3) 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
//            // 회원 토큰 확인
////            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
////                    () -> new CustomException(ErrorCode.NOT_FOUND_USER)
////            );
//            Optional<User> user = userRepository.findByUsername(claims.getSubject());
////            if (user.isEmpty()) {
////                return new ResponseEntity(NOT_FOUND_USER.getHttpStatus());
////            }
//            user.isEmpty();
//            new ResponseEntity(NOT_FOUND_USER.getHttpStatus());
//            // 4) id와 user를 사용하여 DB조회
//
////            Blog blog = blogRepository.findById(id).orElseThrow(
////                    () -> new CustomException(ErrorCode.AUTHORIZATION)
////            );
////            if (blog.getUser().getUsername().equals(user.getUsername()) || user.getRole() == UserRoleEnum.ADMIN) {
//
//            Optional<Blog> blog = blogRepository.findByIdAndUser(id, user.get());
//            //if문으로 썼을 때
////            if (blog.isEmpty()) {
////                return new ResponseEntity(AUTHORIZATION.getHttpStatus());
////            }
//            // if 문으로 안 썼을 때
//            blog.isEmpty();
//            new ResponseEntity(AUTHORIZATION.getHttpStatus());
//
//
//            // 5) id를 통해서 DB 삭제.
//            blogRepository.deleteById(id);
////            JSONObject success = new JSONObject();
////            success.put("success", true);
//            //return success.toString();
//
//            // 6) ResponseEntity에 Body 부분에 만든 객체 전달.
////            return new BlogDto<MessageDto>("success", new MessageDto("게시글 삭제 성공"));
//
////            return ResponseEntity.ok().body(MessageDto.builder()
////                            .statusCode(HttpStatus.OK.value())
////                            .msg("게시글 삭제 성공.")
////                            .build()
////                    );
////        } else {
////            return null;
////        }
////    }
////                return ResponseEntity.ok(new MessageDto("삭제 성공"));
////            } else {
////                return ErrorResponse.toResponseEntity(new CustomException(NO_AUTHORITY).getErrorCode());
////            }
////        } else {
////            return ErrorResponse.toResponseEntity(new CustomException(INVALID_TOKEN).getErrorCode());
////        }
//            return ResponseEntity.ok()
//                    .body(ErrorResponse.builder()
//                            .statuscode(HttpStatus.OK.value())
//                            .msg("게시글 삭제 성공.")
//                            .build());
//        } else {
//            return new ResponseEntity(NOT_FOUND_TOKEN.getHttpStatus());
//        }
//    }
    // 게시물 좋아요
    @Transactional
    public ResponseEntity<AllResponseDto> createBlogLike(Long id, User user) {
        Blog blog = blogRepository.findById(id).orElseThrow(
                () -> new CustomException(NOT_FOUND_BLOG)
        );

        Optional<BlogLike> blogLike = blogLikeRepository.findByBlogIdAndUserId(id, user.getId());
        if (blogLike.isEmpty()) {
            blogLikeRepository.saveAndFlush(BlogLike.of(blog, user));
            return ResponseEntity.ok()
                    .body(AllResponseDto.builder()
                            .statusCode(HttpStatus.OK.value())
                            .msg("게시글 좋아요 선택")
                            .build()
                    );
        } else {
            blogLikeRepository.deleteByBlogIdAndUserId(id, user.getId());
            return ResponseEntity.ok()
                    .body(AllResponseDto.builder()
                            .statusCode((HttpStatus.OK.value()))
                            .msg("게시글 좋아요 취소")
                            .build()
                    );
        }
    }
}

