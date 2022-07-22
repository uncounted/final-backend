package com.hanghae0705.sbmoney.repository;

import com.hanghae0705.sbmoney.model.domain.chat.ChatRoom;
import com.hanghae0705.sbmoney.model.domain.chat.ChatRoomProsCons;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomProsConsRepository extends JpaRepository<ChatRoomProsCons, Long> {
    ChatRoomProsCons findByUserIdAndChatRoom(Long userId, ChatRoom chatRoom);
}
