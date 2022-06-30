package com.hanghae0705.sbmoney.controller;

import com.hanghae0705.sbmoney.data.Message;
import com.hanghae0705.sbmoney.model.domain.Comment;
import com.hanghae0705.sbmoney.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/board")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{boardId}/comment")
    public ResponseEntity<Message> getCommentList(@PathVariable Long boardId) {
        Message message = commentService.getCommentList(boardId);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/{boardId}/comment")
    public ResponseEntity<Message> postComment(@PathVariable Long boardId, Comment.Request reqeust){
        Message message = commentService.postComment(reqeust);
        return ResponseEntity.ok(message);
    }
}
