package com.hanghae0705.sbmoney.controller.chat;


import com.hanghae0705.sbmoney.model.domain.User;
import com.hanghae0705.sbmoney.model.domain.chat.ChatRoom;
import com.hanghae0705.sbmoney.model.domain.chat.ChatRoomProsCons;
import com.hanghae0705.sbmoney.model.domain.chat.RedisChatRoom;
import com.hanghae0705.sbmoney.repository.ChatRoomProsConsRepository;
import com.hanghae0705.sbmoney.repository.ChatRoomRepository;
import com.hanghae0705.sbmoney.repository.RedisChatRoomRepository;
import com.hanghae0705.sbmoney.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/api/chat/rooms")
    public List<ChatRoom.Response> room() {
        Long userId = commonService.getUserId();
        List<ChatRoom> chatRooms = chatRoomRepository.findAll();
        Boolean checkProsCons = null;
        List<ChatRoom.Response> chatRoomResponseList = new ArrayList<>();

        for(ChatRoom chatRoom : chatRooms){
            List<ChatRoomProsCons> chatRoomProsConsList = chatRoom.getChatRoomProsConsList();
            //찬성 반대를 눌렀는 지 체크
            for (ChatRoomProsCons chatRoomProsCons : chatRoomProsConsList){
                if(chatRoomProsCons.getUserId().equals(userId)){
                    checkProsCons = chatRoomProsCons.getProsCons();
                    break;
                }
            }
            chatRoomResponseList.add(new ChatRoom.Response(chatRoom, checkProsCons)) ;
        }
        return chatRoomResponseList;
    }

    @PostMapping("/api/chat/room")
    public ChatRoom createRoom(@RequestBody ChatRoom.Request name) {
        User user = commonService.getUser();
        System.out.println(user.getId() + user.getUsername());
        ChatRoom chatRoom = chatRoomRepository.save(new ChatRoom(user, name.getName()));;
        String redisChatRoomId = chatRoom.getId().toString();
        redisChatRoomRepository.createChatRoom(redisChatRoomId, name.getName());
        return chatRoom;
    }

    @PostMapping("/api/chat/room/{roomId}/vote")
    public Boolean vote(@PathVariable String roomId, @RequestBody ChatRoomProsCons.Request chatRoomProsConsRequest){
        Long userId = commonService.getUserId();
        UUID uuid = UUID.fromString(roomId);
        ChatRoom chatRoom = chatRoomRepository.findById(uuid).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 방입니다.")
        );
        ChatRoomProsCons chatRoomProsCons = new ChatRoomProsCons(chatRoomProsConsRequest.getProsCons(), userId, chatRoom);
        chatRoomProsConsRepository.save(chatRoomProsCons);
        return chatRoomProsConsRequest.getProsCons();
    }

    @GetMapping("/api/chat/room/{roomId}")
    public RedisChatRoom roomInfo(@PathVariable String roomId) {
        return redisChatRoomRepository.findRoomById(roomId);
    }

}
