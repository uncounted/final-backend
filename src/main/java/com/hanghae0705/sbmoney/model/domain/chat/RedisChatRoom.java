package com.hanghae0705.sbmoney.model.domain.chat;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
public class RedisChatRoom implements Serializable {

    private static final long serialVersionUID = 6494678977089006639L;

    private String roomId;
    private String name;
    private long userCount; // 채팅방 인원수

    public static RedisChatRoom create(String uuid, String name) {
        RedisChatRoom redisChatRoom = new RedisChatRoom();
        redisChatRoom.roomId = uuid;
        redisChatRoom.name = name;
        return redisChatRoom;
    }
}