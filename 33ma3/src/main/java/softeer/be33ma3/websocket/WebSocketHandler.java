package softeer.be33ma3.websocket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {

    private final WebSocketService webSocketService;

    // 클라이언트로부터 메세지를 수신
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            webSocketService.receiveExitMsg(session, message);
        } catch(Exception e) {
            log.error("메세지 수신 에러");
        }
    }
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        try {
            super.afterConnectionEstablished(session);
        } catch(Exception e) {
            log.error("연결 수립 후 에러 발생");
        } finally {
            Map<String, Object> attributes = session.getAttributes();
            String type = (String) attributes.get("type");
            // 게시글 조회 관련 실시간 통신 요청일 경우
            if(type.equals("post")) {
                Long postId = (Long) attributes.get("postId");
                Long memberId = (Long) attributes.get("memberId");
                webSocketService.saveInPost(postId, memberId, session);
            }
            if(type.equals("chatRoom")){
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

    // 서버 -> 클라이언트 데이터 전송
    public void sendData2Client(Long memberId, Object data) {
        try {
            webSocketService.sendData(memberId, data);
        } catch(IOException e) {
            log.error("실시간 데이터 전송 에러");
        }
    }

    public void deletePostRoom(Long postId) {
        webSocketService.deletePostRoom(postId);
    }
    public Set<Long> findAllMemberInPost(Long postId) {
        return webSocketService.findAllMemberInPost(postId);
    }
    public boolean isInPostRoom(Long postId, Long memberId) {
        return webSocketService.isInPostRoom(postId, memberId);
    }
}
