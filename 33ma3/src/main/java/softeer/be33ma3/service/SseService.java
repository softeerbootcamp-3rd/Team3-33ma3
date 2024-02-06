package softeer.be33ma3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import softeer.be33ma3.exception.SseException;
import softeer.be33ma3.repository.SseRepository;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class SseService {

    private final SseRepository sseRepository;

    // 새로운 sse 연결 생성 후 SseEmitter 반환
    public SseEmitter addConnection() {
        SseEmitter sseEmitter = new SseEmitter();
        // TODO: 현재 유저의 member id 가져오기
        Long memberId = 1L;
        sseEmitter = sseRepository.save(memberId, sseEmitter);
        try {
            sseEmitter.send(SseEmitter.event()
                    .name("connect")
                    .data("SSE 연결 성공!"));
        }
        catch(IOException e) {
            throw new SseException("SSE 통신에 실패");
        }
        return sseEmitter;
    }
}
