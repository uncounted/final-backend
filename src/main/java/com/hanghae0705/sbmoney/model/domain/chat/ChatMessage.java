package com.hanghae0705.sbmoney.model.domain.chat;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
@Getter
@Setter
public class ChatMessage {

    public ChatMessage() {
    }

    @Builder
    public ChatMessage(MessageType type, String roomId, String sender, String profileImg, String message, Long userCount) {
        this.type = type;
        this.roomId = roomId;
        this.sender = sender;
        this.profileImg = profileImg;
        this.message = message;
        this.userCount = userCount;
    }


    // 메시지 타입 : 입장, 퇴장, 채팅
    public enum MessageType {
        @Enumerated(value = EnumType.STRING)
        ENTER,
        @Enumerated(value = EnumType.STRING)
        QUIT,
        @Enumerated(value = EnumType.STRING)
        TALK
    }

    private MessageType type; // 메시지 타입
    private String roomId; // 방번호
    private String sender; // 메시지 보낸사람
    private String profileImg; // 메시지 보낸사람 프로필
    private String message; // 메시지
    private Long userCount; // 채팅방 인원수, 채팅방 내에서 메시지가 전달될때 인원수 갱신시 사용
}
