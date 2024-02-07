package softeer.be33ma3.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Slf4j
public class WebSocketRepository {
    private final Map<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public void save(Long memberId, WebSocketSession webSocketSession) {
        sessions.put(memberId, webSocketSession);
        log.info("WebSocketSession db : {}", sessions.size());
    }

    public WebSocketSession findById(Long memberId) {
        return sessions.get(memberId);
    }

    public void delete(WebSocketSession webSocketSession) {
        sessions.values().remove(webSocketSession);
    }
}
