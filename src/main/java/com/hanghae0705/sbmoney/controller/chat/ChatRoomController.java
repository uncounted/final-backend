package com.hanghae0705.sbmoney.controller.chat;


import com.hanghae0705.sbmoney.data.Message;
import com.hanghae0705.sbmoney.model.domain.chat.ChatLog;
import com.hanghae0705.sbmoney.model.domain.user.User;
import com.hanghae0705.sbmoney.model.domain.chat.ChatRoom;
import com.hanghae0705.sbmoney.model.domain.chat.ChatRoomProsCons;
import com.hanghae0705.sbmoney.model.domain.chat.RedisChatRoom;
import com.hanghae0705.sbmoney.repository.ChatRoomProsConsRepository;
import com.hanghae0705.sbmoney.repository.ChatRoomRepository;
import com.hanghae0705.sbmoney.repository.RedisChatRoomRepository;
import com.hanghae0705.sbmoney.service.ChatService;
import com.hanghae0705.sbmoney.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {

    private final RedisChatRoomRepository redisChatRoomRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomProsConsRepository chatRoomProsConsRepository;
    private final CommonService commonService;
    private final ChatService chatService;

    @GetMapping("/api/chat/rooms")
    public List<ChatRoom.Response> room() {
        Long userId = commonService.getUserId();
        List<ChatRoom> chatRooms = chatRoomRepository.findAll();
        List<ChatRoom.Response> chatRoomResponseList = new ArrayList<>();

        for(ChatRoom chatRoom : chatRooms){
            List<ChatRoomProsCons> chatRoomProsConsList = chatRoom.getChatRoomProsConsList();
            Boolean checkProsCons = null;
            //찬성 반대를 눌렀는 지 체크
            if(!chatRoomProsConsList.isEmpty()){
                for (ChatRoomProsCons chatRoomProsCons : chatRoomProsConsList){
                    if(chatRoomProsCons.getUserId().equals(userId)){
                        checkProsCons = chatRoomProsCons.getProsCons();
                        break;
                    }
                }
            }
            chatRoomResponseList.add(new ChatRoom.Response(chatRoom, checkProsCons)) ;
        }
        return chatRoomResponseList;
    }

    @PostMapping("/api/chat/room")
    public ChatRoom createRoom(@RequestBody ChatRoom.Request request) {
        User user = commonService.getUser();
        String RoomUuid = UUID.randomUUID().toString();
        ChatRoom chatRoom = chatRoomRepository.save(new ChatRoom(user, request.getTimeLimit(), request.getComment(), RoomUuid));;
        String redisChatRoomId = chatRoom.getRoomId();
        redisChatRoomRepository.createChatRoom(redisChatRoomId, request.getComment());
        return chatRoom;
    }

    @DeleteMapping("/api/chat/room/{roomId}")
    public void deleteRoom(@PathVariable String roomId){
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId).orElseThrow(
                () -> new IllegalArgumentException("존재하는 방이 없습니다.")
        );
        for(ChatLog chatLog : chatRoom.getChatLogList()){
            chatLog.update(null);
        }

        for(ChatRoomProsCons chatRoomProsCons : chatRoom.getChatRoomProsConsList()){
            chatRoomProsCons.setChatRoom(null);
        }
        chatRoomRepository.delete(chatRoom);
    }

    @Transactional
    @PostMapping("/api/chat/room/{roomId}/vote")
    public Boolean vote(@PathVariable String roomId, @RequestBody ChatRoomProsCons.Request chatRoomProsConsRequest){
        Long userId = commonService.getUserId();
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 방입니다.")
        );
        ChatRoomProsCons checkVote = chatRoomProsConsRepository.findByUserIdAndChatRoom(userId, chatRoom);
        if(checkVote != null){
            return checkVote.update(chatRoomProsConsRequest.getProsCons());
        } else {
            ChatRoomProsCons chatRoomProsCons = new ChatRoomProsCons(chatRoomProsConsRequest.getProsCons(), userId, chatRoom);
            chatRoomProsConsRepository.save(chatRoomProsCons);
            return chatRoomProsConsRequest.getProsCons();
        }
    }

    @GetMapping("/api/chat/room/{roomId}")
    public RedisChatRoom roomInfo(@PathVariable String roomId) {
        return redisChatRoomRepository.findRoomById(roomId);
    }

    @GetMapping("/api/room/{roomId}/save")
    public ResponseEntity<Message> saveChatLog(@PathVariable String roomId) {
        chatService.saveChatLog(roomId);
        return ResponseEntity.ok(Message.builder()
                .result(true)
                .respMsg("종료된 채팅 기록 저장에 성공했습니다.")
                .build());
    }
}
