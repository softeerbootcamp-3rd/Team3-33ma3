package softeer.be33ma3.repository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SseRepository {

    private final Map<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

}
