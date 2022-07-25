package com.hanghae0705.sbmoney.model.domain.chat.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hanghae0705.sbmoney.model.domain.baseEntity.CreatedTime;
import com.hanghae0705.sbmoney.model.domain.chat.ChatRoomProsCons;
import com.hanghae0705.sbmoney.model.domain.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    @Column(nullable = false)
    private Boolean proceeding;

    @Column(nullable = false)
    String comment;

    @Column(nullable = false)
    int timeLimit;

    int voteTrueCount;
    int voteFalseCount;

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

    public ChatRoom(User user, int timeLimit, String comment, String roomId, Boolean proceeding) {
        this.user = user;
        this.timeLimit = timeLimit;
        this.comment = comment;
        this.roomId = roomId;
        this.proceeding = true;
    }

    public ChatRoom(User user, String roomId) {
        this.user = user;
        this.roomId = roomId;
    }
    public void PlusVoteCount(Boolean prosCons) {
        if (prosCons) {
            this.voteTrueCount++;
        } else {
            this.voteFalseCount++;
        }
    }

    public void MinusVoteCount(Boolean prosCons) {
        if (prosCons) {
            this.voteTrueCount--;
        } else {
            this.voteFalseCount--;
        }
    }

    public void changeProceeding(Boolean proceeding) {
        this.proceeding = proceeding;
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
        private String authorNickname;
        private String authorProfileImg;
        private Long userCount;
        private Boolean prosCons;
        private LocalDateTime createdAt;

        public static Response of(ChatRoom chatRoom) {
            return Response.builder()
                    .chatRoom(chatRoom)
                    .build();
        }

        @Builder
        public Response(ChatRoom chatRoom, Long userCount){
            this.roomId = chatRoom.getRoomId();
            this.comment = chatRoom.getComment();
            this.authorNickname = chatRoom.getUser().getNickname();
            this.authorProfileImg = chatRoom.getUser().getProfileImg();
            this.userCount = userCount;
            this.createdAt = chatRoom.getCreatedDate();
        }

        @Builder
        public Response(ChatRoom chatRoom, Boolean chatRoomProsCons, Long userCount){
            this.roomId = chatRoom.getRoomId();
            this.comment = chatRoom.getComment();
            this.authorNickname = chatRoom.getUser().getNickname();
            this.authorProfileImg = chatRoom.getUser().getProfileImg();
            this.userCount = userCount;
            this.createdAt = chatRoom.getCreatedDate();
            this.prosCons = chatRoomProsCons;
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class ClosedResponse {
        private String roomId;
        private String comment;
        private String authorNickname;
        private String authorProfileImg;
        private int voteTruePecent;
        private int voteFalsePecent;
        private LocalDateTime createdAt;

        @Builder
        public ClosedResponse(ChatRoom chatRoom){
            this.roomId = chatRoom.getRoomId();
            this.comment = chatRoom.getComment();
            this.authorNickname = chatRoom.getUser().getNickname();
            this.authorProfileImg = chatRoom.getUser().getProfileImg();
            this.voteTruePecent =chatRoom.getVoteTrueCount()  / (chatRoom.getVoteTrueCount() + chatRoom.getVoteFalseCount()) * 100;
            this.voteFalsePecent =chatRoom.getVoteFalseCount()  / (chatRoom.getVoteTrueCount() + chatRoom.getVoteFalseCount()) * 100;
            this.createdAt = chatRoom.getCreatedDate();
        }
    }


}
