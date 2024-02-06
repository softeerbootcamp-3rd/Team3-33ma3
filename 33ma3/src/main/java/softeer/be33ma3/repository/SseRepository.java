package softeer.be33ma3.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class SseRepository {

    private final Map<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    public SseEmitter save(Long memberId, SseEmitter sseEmitter) {
        sseEmitters.put(memberId, sseEmitter);
        return sseEmitter;
    }

}
