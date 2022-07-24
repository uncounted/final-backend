package com.hanghae0705.sbmoney.model.domain.chat.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hanghae0705.sbmoney.model.domain.baseEntity.CreatedTime;
import com.hanghae0705.sbmoney.model.domain.chat.ChatRoomProsCons;
import com.hanghae0705.sbmoney.model.domain.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class ChatRoom extends CreatedTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    String roomId;

//    @Column(nullable = false)
//    private Boolean proceeding;

    @Column(nullable = false)
    String comment;

    @Column(nullable = false)
    int timeLimit;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "chatRoom-fk")
    private List<ChatRoomProsCons> chatRoomProsConsList;

    @OneToMany (mappedBy = "chatRoom", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "chatLog-chatRoom-fk")
    private List<ChatLog> chatLogList;

    public ChatRoom(User user, int timeLimit, String comment, String roomId) {
        this.user = user;
        this.timeLimit = timeLimit;
        this.comment = comment;
        this.roomId = roomId;
    }

    public ChatRoom(User user, String roomId) {
        this.user = user;
        this.roomId = roomId;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private int timeLimit;
        private String comment;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Response {
        private String roomId;
        private String comment;
        private Long userId;
        private String nickname;
        private String profileImg;
        private Boolean prosCons;

        public static Response of(ChatRoom chatRoom) {
            return Response.builder()
                    .chatRoom(chatRoom)
                    .build();
        }

        @Builder
        public Response(ChatRoom chatRoom){
            this.roomId = chatRoom.getRoomId();
            this.comment = chatRoom.getComment();
            this.userId = chatRoom.getUser().getId();
            this.nickname = chatRoom.getUser().getNickname();
            this.profileImg = chatRoom.getUser().getProfileImg();
        }

        @Builder
        public Response(ChatRoom chatRoom, Boolean chatRoomProsCons){
            this.roomId = chatRoom.getRoomId();
            this.comment = chatRoom.getComment();
            this.userId = chatRoom.getUser().getId();
            this.nickname = chatRoom.getUser().getNickname();
            this.profileImg = chatRoom.getUser().getProfileImg();
            this.prosCons = chatRoomProsCons;
        }
    }


}
