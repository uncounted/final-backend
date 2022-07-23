package com.hanghae0705.sbmoney.controller.chat;


import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hanghae0705.sbmoney.model.domain.chat.ChatMessage;
import com.hanghae0705.sbmoney.service.ChatService;
import com.hanghae0705.sbmoney.service.CommonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {
    private final ChatService chatService;
    private final CommonService commonService;
    /**
     * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
     */
    @MessageMapping("/chat/message")
    public void message(ChatMessage message, @Header("token") String token) {
//        DecodedJWT decodedJWT = JWT.decode(token);
//        String claim = decodedJWT.getClaim("sub").toString();
//        String nickname = claim.substring(1, claim.length()-1);
//        // 로그인 회원 정보로 대화명 설정
//        message.setSender(nickname);
        // Websocket에 발행된 메시지를 redis로 발행(publish)
        chatService.sendChatMessage(message);
    }
}
