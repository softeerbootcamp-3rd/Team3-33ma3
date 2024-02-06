package softeer.be33ma3.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import softeer.be33ma3.service.SseService;

@RestController
@RequiredArgsConstructor
public class SseController {

    private SseService sseService;
}
