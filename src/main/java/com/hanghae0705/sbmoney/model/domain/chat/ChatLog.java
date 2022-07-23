package com.hanghae0705.sbmoney.model.domain.chat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

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
