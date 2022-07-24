package com.hanghae0705.sbmoney.model.domain.chat.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hanghae0705.sbmoney.model.domain.chat.ChatMessage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class ChatLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nickname;
    private ChatMessage.MessageType type;
    private String message;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn (name = "CHATROOM_ID")
    @JsonBackReference (value = "chatLog-chatRoom-fk")
    private ChatRoom chatRoom;

    public void update(ChatRoom chatRoom){
        this.chatRoom = chatRoom;
    }

    @Builder
    public ChatLog(Long id, String nickname, ChatMessage.MessageType type, String message, ChatRoom chatRoom) {
        this.id = id;
        this.nickname = nickname;
        this.type = type;
        this.message = message;
        this.chatRoom = chatRoom;
    }
}
