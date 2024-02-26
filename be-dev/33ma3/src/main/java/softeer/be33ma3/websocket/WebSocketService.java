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

    public void receiveExitMsg(WebSocketSession session, TextMessage message) throws IOException {
        String payload = message.getPayload();
        // 게시글 관련 웹 소켓 연결이 종료된 유저가 있다고 메세지를 받았을 경우
        if(payload.contains("post") && payload.contains("memberId")) {
            log.info("게시글에서 유저가 나갔습니다.");
            ExitRoomMember exitRoomMember = objectMapper.readValue(payload, ExitRoomMember.class);
            closePostConnection(exitRoomMember.getRoomId(), exitRoomMember.getMemberId());
        }
        if(payload.contains("chat") && payload.contains("memberId")) {
            log.info("채팅방에서 유저가 나갔습니다.");
            ExitRoomMember exitRoomMember = objectMapper.readValue(payload, ExitRoomMember.class);
            sendReceiverExit(exitRoomMember.getRoomId(), exitRoomMember.getMemberId()); //상대방이 나가는 경우 전송
            closeChatConnection(exitRoomMember.getRoomId(), exitRoomMember.getMemberId());
        }
        if(payload.contains("chatRoom") && payload.contains("memberId")){
            log.info("채팅 목록에서 유저가 나갔습니다.");
            ExitMember exitMember = objectMapper.readValue(payload, ExitMember.class);
            closeAllChatRoomConnection(exitMember.getMemberId());
        }

        log.info("메세지 수신 성공: {}", payload); // 수신한 메세지 log
        TextMessage textMessage = new TextMessage("메세지 수신 성공");
        session.sendMessage(textMessage);
    }

    // 서버 -> 클라이언트 데이터 전송
    public void sendData2Client(Long memberId, Object data){
        try{
            if(memberId == null || data == null)
                return;
            // 클라이언트에 해당하는 세션 가져오기
            WebSocketSession session = webSocketRepository.findSessionByMemberId(memberId);
            if(session == null || !session.isOpen()) {
                log.info("웹 소켓 연결이 되어있지 않음");
                return;
            }
            // data 직렬화
            String jsonString = objectMapper.writeValueAsString(data);
            synchronized (session) {        // 세션 동기화
                if (session.isOpen()) {
                    // 데이터 전송
                    session.sendMessage(new TextMessage(jsonString));
                } else {
                    log.info("웹 소켓 세션이 이미 닫혔습니다.");
                }
            }
        }catch (IOException e){
            log.error("실시간 데이터 전송 에러");
        }
    }

    public void sendAllChatData2Client(Long memberId, Object data) {
        try {
            // 클라이언트에 해당하는 세션 가져오기
            WebSocketSession session = webSocketRepository.findAllChatRoomSessionByMemberId(memberId);
            // data 직렬화
            String jsonString = objectMapper.writeValueAsString(data);
            // 데이터 전송
            session.sendMessage(new TextMessage(jsonString));
        } catch(IOException e) {
            log.error("실시간 데이터 전송 에러");
        }
    }

    public boolean isInPostRoom(Long postId, Long memberId) {
        Set<Long> memberIds = webSocketRepository.findAllMemberInPost(postId);
        if(memberIds == null){
            return false;
        }
        return memberIds.contains(memberId);
    }

    public void saveInPost(Long postId, Long memberId, WebSocketSession session) {
        webSocketRepository.saveMemberInPost(postId, memberId);
        webSocketRepository.saveSessionWithMemberId(memberId, session);
    }

    public void saveInChat(Long roomId, Long memberId, WebSocketSession session) throws IOException {
        webSocketRepository.saveMemberInChat(roomId, memberId);
        webSocketRepository.saveSessionWithMemberId(memberId, session);
        sendReceiverExists(roomId, memberId);
    }

    private void sendReceiverExists(Long roomId, Long memberId) throws IOException {
        if(webSocketRepository.findReceiverInChatRoom(roomId, memberId) != null){
            Long receiverId = webSocketRepository.findReceiverInChatRoom(roomId, memberId);
            WebSocketSession receiver = webSocketRepository.findSessionByMemberId(receiverId);
            WebSocketSession sender = webSocketRepository.findSessionByMemberId(memberId);

            TextMessage textMessage = new TextMessage("true");
            receiver.sendMessage(textMessage);
            sender.sendMessage(textMessage);
        }
    }

    private void sendReceiverExit(Long roomId, Long memberId) throws IOException {
        if(webSocketRepository.findReceiverInChatRoom(roomId, memberId) != null){
            Long receiverId = webSocketRepository.findReceiverInChatRoom(roomId, memberId);
            WebSocketSession session = webSocketRepository.findSessionByMemberId(receiverId);

            TextMessage textMessage = new TextMessage("false");
            session.sendMessage(textMessage);
        }
    }

    public void saveInAllChatRoom(Long memberId, WebSocketSession session) {
        webSocketRepository.saveAllChatRoomSessionWithMemberId(memberId, session);
    }

    private void closePostConnection(Long postId, Long memberId) {
        webSocketRepository.deleteMemberInPost(postId, memberId);
        webSocketRepository.deleteSessionWithMemberId(memberId);
    }

    public void closeChatConnection(Long roomId, Long memberId) {
        webSocketRepository.deleteMemberInChatRoom(roomId, memberId);
        webSocketRepository.deleteSessionWithMemberId(memberId);
    }

    private void closeAllChatRoomConnection(Long memberId) {
        webSocketRepository.deleteAllChatRoomSessionWithMemberId(memberId);
    }

    public void deletePostRoom(Long postId) {
        webSocketRepository.deletePostRoom(postId);
    }
    public Set<Long> findAllMemberInPost(Long postId) {
        return webSocketRepository.findAllMemberInPost(postId);
    }
}
