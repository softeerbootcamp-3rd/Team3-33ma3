package softeer.be33ma3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import softeer.be33ma3.service.WebSocketService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {

    private final WebSocketService webSocketService;

    // 클라이언트로부터 메세지를 수신
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            webSocketService.receiveMsg(session, message);
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
            webSocketService.save(session);
        }
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        try {
            super.afterConnectionClosed(session, status);
        } catch(Exception e) {
            log.error("연결 종료 후 에러 발생");
        } finally {
            // 연결이 종료된 클라이언트의 세션을 맵에서 제거
            webSocketService.delete(session);
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
}
