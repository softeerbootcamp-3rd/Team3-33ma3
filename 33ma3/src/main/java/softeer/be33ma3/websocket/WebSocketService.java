package softeer.be33ma3.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketService {

    private final WebSocketRepository webSocketRepository;
    private final ObjectMapper objectMapper;

    public void receiveMsg(WebSocketSession session, TextMessage message) throws IOException {
        String payload = message.getPayload();
        // 게시글 관련 웹 소켓 연결이 종료된 유저가 있다고 메세지를 받았을 경우
        if(payload.contains("post") && payload.contains("memberId")) {
            ExitMember exitMember = objectMapper.readValue(payload, ExitMember.class);
            closePostConnection(exitMember.getRoomId(), exitMember.getMemberId());
        }
        if(payload.contains("chat") && payload.contains("memberId")) {
            ExitMember exitMember = objectMapper.readValue(payload, ExitMember.class);
            closeChatConnection(exitMember.getRoomId(), exitMember.getMemberId());
        }

        log.info("메세지 수신 성공: {}", payload); // 수신한 메세지 log
        TextMessage textMessage = new TextMessage("메세지 수신 성공");
        session.sendMessage(textMessage);
    }

    public void saveInPost(Long postId, Long memberId, WebSocketSession session) {
        webSocketRepository.saveMemberInPost(postId, memberId);
        webSocketRepository.saveSessionWithMemberId(memberId, session);
    }

    public void saveInChat(Long roomId, Long memberId, WebSocketSession session) {
        webSocketRepository.saveMemberInChat(roomId, memberId);
        webSocketRepository.saveSessionWithMemberId(memberId, session);
    }

    public void saveInChatRoom(Long memberId, WebSocketSession session) {
        webSocketRepository.saveAllChatRoomSessionWithMemberId(memberId, session);
    }

    // 데이터 (클래스 객체) 전송
    public void sendData(Long memberId, Object data) throws IOException {
        if(memberId == null || data == null)
            return;
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

    public void sendAllChatRoomData(Long memberId, Object data) throws IOException {
        if(memberId == null || data == null)
            return;
        // 클라이언트에 해당하는 세션 가져오기
        WebSocketSession session = webSocketRepository.findAllChatRoomSessionByMemberId(memberId);
        if(session == null) {
            log.info("웹 소켓 연결이 되어있지 않음");
            return;
        }
        // data 직렬화
        String jsonString = objectMapper.writeValueAsString(data);
        // 데이터 전송
        session.sendMessage(new TextMessage(jsonString));
    }

    private void closePostConnection(Long postId, Long memberId) {
        webSocketRepository.deleteMemberInPost(postId, memberId);
        webSocketRepository.deleteSessionWithMemberId(memberId);
    }

    private void closeChatConnection(Long roomId, Long memberId) {
        webSocketRepository.deleteMemberInChatRoom(roomId, memberId);
        webSocketRepository.deleteSessionWithMemberId(memberId);
    }

    public void deletePostRoom(Long postId) {
        webSocketRepository.deletePostRoom(postId);
    }

    // 해당 게시글에 접속해있는 유저 목록 반환
    public Set<Long> findAllMemberInPost(Long postId) {
        return webSocketRepository.findAllMemberInPost(postId);
    }

    public boolean isInPostRoom(Long postId, Long memberId) {
        Set<Long> memberIds = webSocketRepository.findAllMemberInPost(postId);
        if(memberIds == null)
            return false;
        return memberIds.contains(memberId);
    }
}
