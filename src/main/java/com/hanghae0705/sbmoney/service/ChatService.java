package com.hanghae0705.sbmoney.service;


import com.hanghae0705.sbmoney.data.Message;
import com.hanghae0705.sbmoney.model.domain.chat.ChatLog;
import com.hanghae0705.sbmoney.model.domain.chat.ChatMessage;
import com.hanghae0705.sbmoney.model.domain.chat.ChatRoom;
import com.hanghae0705.sbmoney.repository.ChatLogRepository;
import com.hanghae0705.sbmoney.repository.ChatRoomRepository;
import com.hanghae0705.sbmoney.repository.RedisChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;
    private final ChatRoomRepository chatRoomRepository;
    private final RedisChatRoomRepository redisChatRoomRepository;
    private final ChatLogRepository chatLogRepository;

    /**
     * destination정보에서 roomId 추출
     */
    public String getRoomId(String destination) {
        int lastIndex = destination.lastIndexOf('/');
        if (lastIndex != -1)
            return destination.substring(lastIndex + 1);
        else
            return "";
    }

    /**
     * 채팅방에 메시지 발송
     */
    public void sendChatMessage(ChatMessage chatMessage) {
        chatMessage.setUserCount(redisChatRoomRepository.getUserCount(chatMessage.getRoomId()));
        if (ChatMessage.MessageType.ENTER.equals(chatMessage.getType())) {
            chatMessage.setMessage(chatMessage.getSender() + "님이 방에 입장했습니다.");
            chatMessage.setSender("[알림]");
        } else if (ChatMessage.MessageType.QUIT.equals(chatMessage.getType())) {
            chatMessage.setMessage(chatMessage.getSender() + "님이 방에서 나갔습니다.");
            chatMessage.setSender("[알림]");
        }
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
    }

    /**
     * 채팅 종료 시 채팅 기록 저장
     */
    public void saveChatLog(String roomId) {
        RedisOperations<String, ChatMessage> operations = redisTemplate.opsForList().getOperations();
        System.out.println((operations.opsForList().range(roomId, 0, -1)));

        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 방입니다.")
        );
        List<ChatMessage> chatMessageList = operations.opsForList().range(roomId, 0, -1);
        for(ChatMessage chatMessage : chatMessageList) {
            ChatLog chatLog = ChatLog.builder()
                    .id(null)
                    .type(chatMessage.getType())
                    .nickname(chatMessage.getSender())
                    .message(chatMessage.getMessage())
                    .chatRoom(chatRoom)
                    .build();
            chatLogRepository.save(chatLog);
        }
    }

    /**
     * 채팅 조회
     */
//    public Message getChatLog(Long id) {
//        List<ChatLog> chatLogList = chatLogRepository.findById(id)
//    }

}
