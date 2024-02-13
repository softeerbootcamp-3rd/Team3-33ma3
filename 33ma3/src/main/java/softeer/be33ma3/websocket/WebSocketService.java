package softeer.be33ma3.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketService {

    private final WebSocketRepository webSocketRepository;
    private final ObjectMapper objectMapper;

    public void receiveMsg(WebSocketSession session, TextMessage message) throws IOException {
        String msg = message.getPayload();
        log.info(msg); // 수신한 메세지 log
        TextMessage textMessage = new TextMessage("메세지 수신 성공");
        session.sendMessage(textMessage);
    }

    public void save(Long postId, Long memberId, WebSocketSession session) {
        webSocketRepository.save(postId, memberId);
        webSocketRepository.save(memberId, session);
    }
//    public void closeConnection(Long memberId) throws IOException {
//        WebSocketSession session = webSocketRepository.findById(memberId);
//        if(session == null)
//            return;
//        session.close(CloseStatus.NORMAL);
//        webSocketRepository.delete(memberId);
//    }
//
    // 데이터 (클래스 객체) 전송
    public void sendData(Long memberId, Object data) throws IOException {
        // 클라이언트에 해당하는 세션 가져오기
        WebSocketSession session = webSocketRepository.findSessionByMemberId(memberId);
        if(session == null) {
            log.info("웹 소켓 연결이 되어있지 않음");
            return;
        }
        // data 직렬화
        String jsonString = objectMapper.writeValueAsString(data);
        // 데이터 전송
        session.sendMessage(new TextMessage(jsonString));
    }
}
