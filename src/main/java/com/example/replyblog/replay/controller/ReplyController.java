package com.example.replyblog.replay.controller;

import com.example.replyblog.replay.dto.ReplyRequestDto;
import com.example.replyblog.replay.dto.ReplyResponseDto;
import com.example.replyblog.replay.service.ReplyService;
import com.example.replyblog.util.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReplyController {

    // 0) DI
    private final ReplyService replyService;

    // 요구사항1) 댓글 작성 API (POST)
    @PostMapping("/comment/{id}")
    public ResponseEntity<ReplyResponseDto> createComment
    (@PathVariable Long id, @RequestBody ReplyRequestDto replyRequestDto, HttpServletRequest request) {
        return replyService.createComment(id, replyRequestDto, request);
    }

    // 요구사항2) 댓글 수정 API (PUT)
    @PutMapping("/comment/{id}")
    public ResponseEntity<ReplyResponseDto> updateComment(@PathVariable Long id, @RequestBody ReplyRequestDto replyRequestDto, HttpServletRequest request) {
        return replyService.updateComment(id, replyRequestDto, request);
    }

    // 요구사항3) 댓글 삭제 API (DEL)
    @DeleteMapping("/comment/{id}")
    public ResponseEntity<ErrorResponse> deleteComment(@PathVariable Long id, HttpServletRequest request) {
        return replyService.deleteComment(id, request);
    }


}