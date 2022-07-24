package com.hanghae0705.sbmoney.controller.chat;


import com.hanghae0705.sbmoney.data.Message;
import com.hanghae0705.sbmoney.model.domain.user.User;
import com.hanghae0705.sbmoney.model.domain.chat.entity.ChatRoom;
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
    public ChatRoom.Response createRoom(@RequestBody ChatRoom.Request request) {
        User user = commonService.getUser();
        String RoomUuid = UUID.randomUUID().toString();
        ChatRoom chatRoom = chatRoomRepository.save(new ChatRoom(user, request.getTimeLimit(), request.getComment(), RoomUuid, true));
        String redisChatRoomId = chatRoom.getRoomId();
        redisChatRoomRepository.createChatRoom(redisChatRoomId, request.getComment());
        return new ChatRoom.Response(chatRoom);
    }

    @DeleteMapping("/api/chat/room/{roomId}") //로그랑 메세지도 삭제
    public void deleteRoom(@PathVariable String roomId){
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId).orElseThrow(
                () -> new IllegalArgumentException("존재하는 방이 없습니다.")
        );
        chatRoomRepository.delete(chatRoom);
    }

    @Transactional
    @PostMapping("/api/chat/room/{roomId}/vote")
    public ChatRoom vote(@PathVariable String roomId, @RequestBody ChatRoomProsCons.Request chatRoomProsConsRequest){
        Long userId = commonService.getUserId();
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 방입니다.")
        );
        Boolean prosCons = chatRoomProsConsRequest.getProsCons();
        ChatRoomProsCons checkVote = chatRoomProsConsRepository.findByUserIdAndChatRoom(userId, chatRoom);
        if(checkVote != null){
            chatRoom.MinusVoteCount(!prosCons);
            chatRoom.PlusVoteCount(prosCons);
            checkVote.update(chatRoomProsConsRequest.getProsCons());
        } else {
            ChatRoomProsCons chatRoomProsCons = new ChatRoomProsCons(chatRoomProsConsRequest.getProsCons(), userId, chatRoom);
            chatRoomProsConsRepository.save(chatRoomProsCons);
            chatRoom.PlusVoteCount(prosCons);
        }
        return chatRoom;
    }

    @GetMapping("/api/chat/room/{roomId}")
    public RedisChatRoom roomInfo(@PathVariable String roomId) {
        return redisChatRoomRepository.findRoomById(roomId);
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

}
