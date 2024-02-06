package softeer.be33ma3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softeer.be33ma3.repository.SseRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SseService {

    private SseRepository sseRepository;
}
