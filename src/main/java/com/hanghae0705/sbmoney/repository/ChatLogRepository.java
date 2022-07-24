package com.hanghae0705.sbmoney.repository;

import com.hanghae0705.sbmoney.model.domain.chat.entity.ChatLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatLogRepository extends JpaRepository<ChatLog, Long> {

}
