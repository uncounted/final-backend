package com.hanghae0705.sbmoney.model.domain.chat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hanghae0705.sbmoney.model.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@RequiredArgsConstructor
public class ChatRoom {
    @Id
    @GeneratedValue(generator = "hibernate-uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "CHATROOM_ID", unique = true)
    @Type(type = "uuid-binary")
    private UUID id;

    @Column(nullable = false)
    String name;

//    @Column(nullable = false)
//    private Boolean proceeding;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    @JsonIgnore
    User user;

    @OneToMany(mappedBy = "chatRoom")
    @JsonManagedReference(value = "chatRoom-fk")
    List<ChatRoomProsCons> chatRoomProsConsList;

    @OneToMany (mappedBy = "chatRoom")
    @JsonManagedReference(value = "chatLog-chatRoom-fk")
    List<ChatLog> chatLogList;

    @CreatedDate
    private LocalDateTime createdAt;

    public ChatRoom(User user, String name) {
        this.user = user;
        this.name = name;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String name;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Response {
        private String roomId;
        private String roomName;
        private Long userId;
        private String nickname;
        private String profileImg;
        private Boolean prosCons;

        public Response(ChatRoom chatRoom, Boolean chatRoomProsCons){
            this.roomId = chatRoom.getId().toString();
            this.roomName = chatRoom.getName();
            this.userId = chatRoom.getUser().getId();
            this.nickname = chatRoom.getUser().getNickname();
            this.profileImg = chatRoom.getUser().getProfileImg();
            this.prosCons = chatRoomProsCons;
        }
    }


}
