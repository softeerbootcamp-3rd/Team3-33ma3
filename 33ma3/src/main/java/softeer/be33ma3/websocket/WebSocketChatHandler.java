package softeer.be33ma3.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import softeer.be33ma3.dto.request.ChatMessageDto;

import java.util.Map;

import static softeer.be33ma3.dto.request.ChatMessageDto.MessageType.EXIT;
import static softeer.be33ma3.dto.request.ChatMessageDto.MessageType.TALK;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketChatHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper;
    private final WebSocketRepository webSocketRepository;
    private final WebSocketService webSocketService;
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        try {
            super.afterConnectionEstablished(session);
        } catch(Exception e) {
            log.error("연결 수립 후 에러 발생");
        } finally {
            Map<String, Object> attributes = session.getAttributes();
            Long roomId = (Long) attributes.get("roomId");
            Long memberId = (Long) attributes.get("memberId");

            webSocketRepository.saveMemberInChat(roomId, memberId);
            webSocketRepository.saveSessionWithMemberId(memberId, session);
        }
    }
    @Override   //소켓 통신 시 메세지 다루는 부분
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload {}", payload);
        //payload -> chatMessageDto 로 변환
        ChatMessageDto chatMessageDto = objectMapper.readValue(payload, ChatMessageDto.class);

        if(chatMessageDto.getMessageType().equals(TALK)){
            webSocketService.sendChatMessage(chatMessageDto);
        }
        if(chatMessageDto.getMessageType().equals(EXIT)){    //채팅방을 나간경우
            webSocketService.closeChatConnection(chatMessageDto.getRoomId(), chatMessageDto.getSenderId());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        try {
            super.afterConnectionClosed(session, status);
        } catch(Exception e) {
            log.error("연결 종료 후 에러 발생");
        }
    }
}
