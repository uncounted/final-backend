package com.hanghae0705.sbmoney.controller;

import com.auth0.jwt.interfaces.Claim;
import com.hanghae0705.sbmoney.data.Message;
import com.hanghae0705.sbmoney.model.domain.Board;
import com.hanghae0705.sbmoney.model.domain.User;
import com.hanghae0705.sbmoney.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/api/board")
    public ResponseEntity<Message> getBoard() {
        Message message = boardService.GetBoard();
        return ResponseEntity.ok(message);
    }

    @PostMapping("/api/board")
    public ResponseEntity<Message> postBoard(@RequestBody Board.Request request,@RequestHeader("Authorization") String authorization) {
        Message message = boardService.postBoard(request);
        return ResponseEntity.ok(message);
    }

    @PutMapping("/api/board/{boardId}")
    public ResponseEntity<Message> putBoard(@RequestBody Board.Update request, @PathVariable Long boardId,@RequestHeader("Authorization") String authorization) {
        Message message = boardService.putBoard(request, boardId);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/api/board/{boardId}")
    public ResponseEntity<Message> deleteBoard(@PathVariable Long boardId,@RequestHeader("Authorization") String authorization) {
        Message message = boardService.deleteBoard(boardId);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/api/user/login")
    public String getUser(@RequestHeader("Authorization") String authorization){
        return boardService.getUser(authorization);
    }
}
