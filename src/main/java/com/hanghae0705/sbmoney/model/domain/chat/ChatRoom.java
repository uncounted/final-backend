package com.hanghae0705.sbmoney.model.domain.chat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hanghae0705.sbmoney.model.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    String roomId;

//    @Column(nullable = false)
//    private Boolean proceeding;

    @Column(nullable = false)
    String comment;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "chatRoom")
    @JsonManagedReference(value = "chatRoom-fk")
    private List<ChatRoomProsCons> chatRoomProsConsList;

    @OneToMany (mappedBy = "chatRoom")
    @JsonManagedReference(value = "chatLog-chatRoom-fk")
    private List<ChatLog> chatLogList;

    public ChatRoom(User user, String comment, String roomId) {
        this.user = user;
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
