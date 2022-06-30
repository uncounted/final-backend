package com.hanghae0705.sbmoney.controller;

import com.hanghae0705.sbmoney.data.Message;
import com.hanghae0705.sbmoney.model.domain.Board;
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
    public ResponseEntity<Message> postBoard(@RequestBody Board.Request request) {
        Message message = boardService.postBoard(request);
        return ResponseEntity.ok(message);
    }

    @PutMapping("/api/board/{boardId}")
    public ResponseEntity<Message> putBoard(@RequestBody Board.Update request, @PathVariable Long boardId) {
        Message message = boardService.putBoard(request, boardId);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/api/board/{boardId}")
    public ResponseEntity<Message> deleteBoard(@PathVariable Long boardId) {
        Message message = boardService.deleteBoard(boardId);
        return ResponseEntity.ok(message);
    }
}
