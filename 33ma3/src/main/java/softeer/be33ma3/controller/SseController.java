package softeer.be33ma3.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import softeer.be33ma3.service.SseService;

@RestController
@RequiredArgsConstructor
public class SseController {

    private final SseService sseService;

    @GetMapping(value = "/sse/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> sseConnect() {
        SseEmitter sseEmitter = sseService.addConnection();
        return ResponseEntity.ok(sseEmitter);
    }
}
