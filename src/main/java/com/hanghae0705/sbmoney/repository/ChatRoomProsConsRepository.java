package com.hanghae0705.sbmoney.repository;

import com.hanghae0705.sbmoney.model.domain.chat.ChatRoomProsCons;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomProsConsRepository extends JpaRepository<ChatRoomProsCons, Long> {
}
