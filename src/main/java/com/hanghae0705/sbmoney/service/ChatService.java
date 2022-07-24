package com.hanghae0705.sbmoney.service;


import com.hanghae0705.sbmoney.data.Message;
import com.hanghae0705.sbmoney.exception.ApiException;
import com.hanghae0705.sbmoney.exception.ApiRequestException;
import com.hanghae0705.sbmoney.model.domain.chat.RedisChatRoom;
import com.hanghae0705.sbmoney.model.domain.chat.entity.ChatLog;
import com.hanghae0705.sbmoney.model.domain.chat.ChatMessage;
import com.hanghae0705.sbmoney.model.domain.chat.entity.ChatRoom;
import com.hanghae0705.sbmoney.repository.ChatLogRepository;
import com.hanghae0705.sbmoney.repository.ChatRoomRepository;
import com.hanghae0705.sbmoney.repository.RedisChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
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
    @Transactional
    public void saveChatLog(String roomId) {
        RedisOperations<String, ChatMessage> operations = redisTemplate.opsForList().getOperations();
        System.out.println((operations.opsForList().range(roomId, 0, -1)));

        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 방입니다.")
        );

        // proceeding 종료방(false)으로 변경
        chatRoom.changeProceeding(false);

        // roomId에 해당하는 ChatMessage를 찾아서 ChatLog에 저장
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

    public Message getTopRoom() {
        // 모든 roomId 호출
        List<RedisChatRoom> allRooms =  redisChatRoomRepository.findAllRoom();
        for(RedisChatRoom room : allRooms) {
            log.info("allrooms :"+room.getRoomId());
        }

        // roomId에 해당하는 userCount 찾기
        Map<String, Long> roomMap = allRooms.stream()
                .collect(Collectors.toMap(
                        RedisChatRoom::getRoomId,
                        RedisChatRoom::getUserCount
                ));

        for(Map.Entry<String, Long> room : roomMap.entrySet()) {
            log.info("mapkey :"+room.getKey());
            log.info("mapvalue :"+room.getValue());
        }

        // 상위 5개 찾기
        Map<String, Long> topRoom = roomMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new)
                );

        // userCount가 높은 5개만 DB에서 chatRoom 데이터 읽어오기
        List<ChatRoom.Response> chatRoomList = topRoom.keySet().stream()
                .map(chatRoomRepository::findByRoomId)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(ChatRoom.Response::of)
                .collect(Collectors.toList());

        return Message.builder()
                .result(true)
                .respMsg("상위 5개 결과를 조회하였습니다.")
                .data(chatRoomList)
                .build();
    }

    public void getCloesdChatRoom(Long closedRoomId) {

        // 챗룸 정보(닉네임, 프로필 정보, 코멘트, 찬/반 비율) 가져오기
        ChatRoom chatRoom = chatRoomRepository.findById(closedRoomId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 방입니다.")
        );



        // 채팅 로그 읽어오기
        List<ChatLog> chatLogList = chatLogRepository.findChatLogByChatRoomId(closedRoomId);

    }

}
