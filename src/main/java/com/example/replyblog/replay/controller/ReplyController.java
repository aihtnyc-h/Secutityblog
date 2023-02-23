package com.example.replyblog.replay.controller;

import com.example.replyblog.dto.AllResponseDto;
import com.example.replyblog.jwt.UserDetailsImpl;
import com.example.replyblog.replay.dto.ReplyRequestDto;
import com.example.replyblog.replay.dto.ReplyResponseDto;
import com.example.replyblog.replay.service.ReplyService;
import com.example.replyblog.util.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    (Long id, ReplyRequestDto replyRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return replyService.createComment(id, replyRequestDto, userDetails.getUser());
    }

    // 요구사항2) 댓글 수정 API (PUT)
    @PutMapping("/comment/{id}")
    public ResponseEntity<ReplyResponseDto> updateComment(@PathVariable Long id, @RequestBody ReplyRequestDto replyRequestDto,
                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return replyService.updateComment(id, replyRequestDto, userDetails.getUser());
    }

    // 요구사항3) 댓글 삭제 API (DEL)
    @DeleteMapping("/comment/{id}")
    public ResponseEntity<AllResponseDto> deleteComment(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return replyService.deletComment(id, userDetails.getUser());
    }


}