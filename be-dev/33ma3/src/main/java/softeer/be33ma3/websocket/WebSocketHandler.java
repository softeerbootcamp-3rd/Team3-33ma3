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
import softeer.be33ma3.service.ChatService;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final WebSocketService webSocketService;
    private final ChatService chatService;

    // 클라이언트로부터 메세지를 수신
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            String payload = message.getPayload();
            if (payload.contains("senderId") && payload.contains("receiverId")) {   //채팅인 경우
                ChatMessageDto chatMessageDto = objectMapper.readValue(payload, ChatMessageDto.class);//payload -> chatMessageDto 로 변환

                chatService.sendChatMessage(chatMessageDto);
            }
            else{
                webSocketService.receiveExitMsg(session, message);  //나가는 경우
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
  
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        try {
            super.afterConnectionEstablished(session);
        } catch(Exception e) {
            log.error("연결 수립 후 에러 발생");
        } finally {
            Map<String, Object> attributes = session.getAttributes();
            String type = (String) attributes.get("type");

            if(type.equals("post")) {   //게시글 관련
                Long postId = (Long) attributes.get("postId");
                Long memberId = (Long) attributes.get("memberId");
                webSocketService.saveInPost(postId, memberId, session);
            }
            if(type.equals("chat")){    //채팅 관련
                Long roomId = (Long) attributes.get("roomId");
                Long memberId = (Long) attributes.get("memberId");

                webSocketService.saveInChat(roomId, memberId, session);
            }
            if(type.equals("chatRoom")){    //목록 관련
                Long memberId = (Long) attributes.get("memberId");
                webSocketService.saveInAllChatRoom(memberId, session);
            }

        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        try {
            super.afterConnectionClosed(session, status);
        } catch(Exception e) {
            log.error("연결 종료 후 에러 발생");
        }
    }
}
