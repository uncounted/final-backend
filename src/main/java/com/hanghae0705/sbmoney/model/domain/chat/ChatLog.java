package com.hanghae0705.sbmoney.model.domain.chat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @ManyToOne
    @JoinColumn (name = "CHATROOM_ID")
    @JsonBackReference (value = "chatLog-chatRoom-fk")
    private ChatRoom chatRoom;


    @Builder
    public ChatLog(Long id, String nickname, ChatMessage.MessageType type, String message) {
        this.id = id;
        this.nickname = nickname;
        this.type = type;
        this.message = message;
    }
}
