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
    public ResponseEntity<Message> getBoard(@RequestHeader("Authorization") String authorization) {
        Message message = boardService.GetBoard(authorization);
        return ResponseEntity.ok(message);
    }
    @GetMapping("/api/board/detail/{boardId}")
    public ResponseEntity<Message> getDetailBoard(@PathVariable Long boardId,@RequestHeader("Authorization") String authorization){
        Message message = boardService.GetDetailBoard(boardId,authorization);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/api/board")
    public ResponseEntity<Message> postBoard(@RequestBody Board.Request request, @RequestHeader("Authorization") String authorization) {
        Message message = boardService.postBoard(request, authorization);
        return ResponseEntity.ok(message);
    }

    @PutMapping("/api/board/{boardId}")
    public ResponseEntity<Message> putBoard(@RequestBody Board.Update request, @PathVariable Long boardId, @RequestHeader("Authorization") String authorization) {
        Message message = boardService.putBoard(request, boardId, authorization);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/api/board/{boardId}")
    public ResponseEntity<Message> deleteBoard(@PathVariable Long boardId, @RequestHeader("Authorization") String authorization) {
        Message message = boardService.deleteBoard(boardId, authorization);
        return ResponseEntity.ok(message);
    }
}
