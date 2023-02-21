package com.example.replyblog.blog.controller;

import com.example.replyblog.blog.dto.MessageDto;
import com.example.replyblog.blog.dto.BlogRequestDto;
import com.example.replyblog.blog.dto.BlogResponseDto;
import com.example.replyblog.blog.service.BlogService;
import com.example.replyblog.dto.AllResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BlogController {


    private final BlogService blogService;  // 의존성 주입!

    // 메인 페이지
//    @GetMapping("/")
//    public ModelAndView home() {
//        return new ModelAndView("index");
//    }

    // 전체조회
    @GetMapping("/api/blogs")
    public ResponseEntity<List<AllResponseDto>> getBlogs() {
        return blogService.getBlogs();
    }
//    public ResponseEntity<BlogResponseDto>getBlogs() {
//        return blogService.getBlogs(); }

    //게시글 1개 조회하기 서버
    @GetMapping("/api/blog/{id}")
    public ResponseEntity<BlogResponseDto> getBlog(@PathVariable Long id) {
        return blogService.getBlog(id);
    }
//    @GetMapping("/api/blog")
//    public List<Blog> getBlog(@PathVariable long id) {
//        return blogService.getBlog();
//    }

    // 게시글 생성하기 서버
    @PostMapping("/api/blog")
    public  ResponseEntity<BlogResponseDto> createBlog(@RequestBody BlogRequestDto blogRequestDto, HttpServletRequest request) {
        return blogService.createBlog(blogRequestDto, request);
    }
    //    public List<BlogResponseDto> createBlog(@RequestBody BlogRequestDto requestDto, HttpServletRequest request) {
//        return blogService.createBlog(requestDto, request);        //변수명사용하기 + 필드명 requestDto.getContents() -> 유저네임이 제목아름으로 들어갈 수 있기 때문에 대부분 통으로 보내준다.  // 카멜 케이스 단어랑 단어를 이어줄때, 뒤에오는 글자는 대문자       //왜 빨간줄이 뜨는거니..?
//    }
// 컨트롤러의 리턴값 서비스의 리턴값과 같아야 한다. -> 서비스에서 리턴값이 list였기 때문에 컨트롤러 타입도 같이 만들어야한다.
    //게시글 전체 조회하기 서버
//    @GetMapping("/api/blogs")
//    public List<BlogResponseDto> getBlogs(){
//        return blogService.getBlogs();
//    }
//    public Optional<Blog> getBlogs(@PathVariable Long id) {
//        return blogService.getBlogs(id);
//    }

    //게시글 변경하기 서버
    @PutMapping("/api/blog/{id}")
    public ResponseEntity<?> updateBlog(@PathVariable Long id, @RequestBody BlogRequestDto blogRequestDto, HttpServletRequest request) {
        return blogService.updateBlog(id, blogRequestDto, request);
    }
//    public List<BlogResponseDto> updateBlog(@RequestBody BlogRequestDto requestDto, HttpServletRequest request) {
//        return blogService.updateBlog(requestDto, request);
//    }
    //게시글 변경하기 서버
//    @PutMapping("/api/blog/{id}")
//    public BlogDto<BlogMessageDto> updatePutBlog(@PathVariable Long id, @RequestBody BlogRequestDto requestDto) {
//        return blogService.updateBlog(id, requestDto);
//        return ResponseUtil.SUCCESS("유저를 추가하였습니다.", null);
//    }

    //게시글 삭제하기 서버
    @DeleteMapping("/api/blog/{id}")
    public ResponseEntity<MessageDto> deleteBlog(@PathVariable long id, HttpServletRequest request){
        return blogService.deleteBlog(id, request);
    }
//    @DeleteMapping("/api/blog/{id}")
//    public BlogDto<?> deleteBlog(@PathVariable Long id, @RequestBody BlogRequestDto requestDto) {
//        return blogService.deleteBlog(id, requestDto.getPassword());
//    }
//    public List<BlogResponseDto> deleteBlog(@PathVariable Long id, @RequestBody BlogRequestDto requestDto, HttpServletRequest request) {
//        return blogService.deleteBlog(requestDto, request);
//    }

    // 게시글 삭제하기 서버
//    @DeleteMapping("/api/blog/{id}")
//    public BlogDto<?> deleteBlog(@PathVariable Long id, @RequestBody BlogRequestDto requestDto) {
//        return blogService.deleteBlog(id, requestDto.getPassword());
//    }
    // 커밋 푸쉬 연습하자
}