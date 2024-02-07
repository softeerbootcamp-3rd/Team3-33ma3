package softeer.be33ma3.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import softeer.be33ma3.repository.WebSocketRepository;

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

    public void save(WebSocketSession session) {
        // TODO: 로그인한 유저 정보를 이용하여 member id 가져오기
        Long memberId = getMemberId();
        webSocketRepository.save(memberId, session);
    }
    public void delete(WebSocketSession webSocketSession) {
        webSocketRepository.delete(webSocketSession);
    }

    // TODO: 로그인한 유저 정보를 이용하여 member id 가져오기
    private Long getMemberId() {
        return 1L;
    }
}
