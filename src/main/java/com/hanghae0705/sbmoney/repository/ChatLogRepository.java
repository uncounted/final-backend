package com.hanghae0705.sbmoney.repository;

import com.hanghae0705.sbmoney.model.domain.chat.ChatLog;
import com.hanghae0705.sbmoney.model.domain.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChatLogRepository extends JpaRepository<ChatLog, Long> {

}
