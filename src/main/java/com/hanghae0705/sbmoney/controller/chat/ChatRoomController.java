package com.hanghae0705.sbmoney.controller.chat;

import com.hanghae0705.sbmoney.data.Message;
import com.hanghae0705.sbmoney.model.domain.chat.entity.ChatRoom;
import com.hanghae0705.sbmoney.model.domain.chat.ChatRoomProsCons;
import com.hanghae0705.sbmoney.repository.chat.ChatRoomRepository;
import com.hanghae0705.sbmoney.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatService chatService;

    @GetMapping("/api/chat/rooms")
    public ResponseEntity<Message> getRooms() throws IOException {
        return ResponseEntity.ok(chatService.getRooms());
    }

    @GetMapping("/api/closedChat/rooms")
    public ResponseEntity<Message> getClosedRoom() {
        return ResponseEntity.ok(chatService.getClosedRooms());
    }

    @GetMapping("/api/chat/room/{roomId}")
    public Message roomInfo(@PathVariable String roomId) throws IOException {
        return chatService.getRoomDetail(roomId);
    }

    @PostMapping("/api/chat/room")
    public Message createRoom(@RequestBody ChatRoom.Request request) {
        return chatService.createRoom(request);
    }

    @DeleteMapping("/api/chat/room/{roomId}") //로그랑 메세지도 삭제
    public Message deleteRoom(@PathVariable String roomId){
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId).orElseThrow(
                () -> new IllegalArgumentException("존재하는 방이 없습니다.")
        );
        chatRoomRepository.delete(chatRoom);
        return Message.builder()
                .result(true)
                .respMsg("채팅방이 삭제되었습니다.")
                .build();
    }

    @Transactional
    @PostMapping("/api/chat/room/{roomId}/vote")
    public Message vote(@PathVariable String roomId, @RequestBody ChatRoomProsCons.Request chatRoomProsConsRequest){
        return chatService.vote(roomId, chatRoomProsConsRequest);

    }

    @GetMapping("/api/chat/room/{roomId}/save")
    public ResponseEntity<Message> saveChatLog(@PathVariable String roomId) {
        chatService.saveChatLog(roomId);
        return ResponseEntity.ok(Message.builder()
                .result(true)
                .respMsg("종료된 채팅 기록 저장에 성공했습니다.")
                .build());
    }

    // userCount가 높은 상위 5개 방을 호출함
    @GetMapping("/api/chat/room/top")
    public Message getTopRoom() {
        return chatService.getTopRoom();
    }

    @GetMapping("/api/closedChat/room/{closedRoomId}")
    public Message getClosedRoomDetail(@PathVariable String closedRoomId) {
        return chatService.getCloesdChatRoom(closedRoomId);
    }
}
