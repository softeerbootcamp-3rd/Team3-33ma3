package softeer.be33ma3.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Slf4j
public class WebSocketRepository {

    // postId : set of memberId
    private final Map<Long, Set<Long>> postRoom = new ConcurrentHashMap<>();
    // memberId : WebSocketSession
    private final Map<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public void save(Long postId, Long memberId) {
        Set<Long> members = new HashSet<>();
        System.out.println("here3");
        if(postRoom.containsKey(postId)) {
            members = postRoom.get(postId);
        }
        System.out.println("here1");
        members.add(memberId);
        System.out.println("here2");
        postRoom.put(postId, members);
        log.info("{}번 게시글에 {}번 유저 입장", postId, memberId);
    }

    public void save(Long memberId, WebSocketSession webSocketSession) {
        sessions.put(memberId, webSocketSession);
        log.info("{}번 유저 웹소켓 세션 저장 성공", memberId);
    }

    public WebSocketSession findSessionByMemberId(Long memberId) {
        return sessions.get(memberId);
    }

//    public void save(Long memberId, WebSocketSession webSocketSession) {
//        sessions.put(memberId, webSocketSession);
//        log.info("WebSocketSession db : {}", sessions.size());
//    }
//
//    public WebSocketSession findById(Long memberId) {
//        return sessions.get(memberId);
//    }
//
//    public void delete(Long memberId) {
//        sessions.remove(memberId);
//    }
}
