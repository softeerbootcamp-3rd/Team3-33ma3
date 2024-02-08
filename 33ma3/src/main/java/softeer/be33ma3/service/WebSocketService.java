package softeer.be33ma3.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import softeer.be33ma3.dto.request.PostCreateDto;
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
        Long memberId = MemberService.getMemberId();
        webSocketRepository.save(memberId, session);
    }
    public void delete(WebSocketSession webSocketSession) {
        webSocketRepository.delete(webSocketSession);
    }

    // 데이터 (클래스 객체) 전송
    public void sendData(Long memberId, Object data) throws IOException {
        // 클라이언트에 해당하는 세션 가져오기
        WebSocketSession session = webSocketRepository.findById(memberId);
        // data 직렬화
        String jsonString = objectMapper.writeValueAsString(data);
        // 데이터 전송
        session.sendMessage(new TextMessage(jsonString));
    }
}
