package com.hanghae0705.sbmoney.repository;

import com.hanghae0705.sbmoney.model.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {

    Optional<ChatRoom> findByRoomId(String roomUuid);
    List<ChatRoom> findAllByProceeding(Boolean proceeding);
}
