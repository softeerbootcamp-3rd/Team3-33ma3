package softeer.be33ma3.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softeer.be33ma3.domain.Post;
import softeer.be33ma3.repository.PostRepository;
import softeer.be33ma3.websocket.WebSocketService;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class SchedulingService {
    private final PostRepository postRepository;
    private final WebSocketService webSocketService;

    @Scheduled(cron = "0 0 * * * * ", zone = "Asia/Seoul") //매 00시 00분 마다 실행
    @Transactional
    public void checkPostDDay(){
        log.info("게시글 마감 스케줄러 작동 시작");
        List<Post> posts = postRepository.findByDoneFalse();    //마감이 되지 않은 게시글 가져오기
        LocalDateTime currentDate = LocalDateTime.now();

        for (Post post : posts) {
            LocalDateTime postCreationTime = post.getCreateTime();  //게시글 생성 시간
            int dday = post.getDeadline(); // D-day

            LocalDateTime deadlineTime = postCreationTime.plusDays(dday)
                    .withHour(23)
                    .withMinute(59)
                    .withSecond(59);   //마감 시간: 마감일 기준 23시 59분 59초

            if (currentDate.isAfter(deadlineTime)) {
                post.setDone();
                webSocketService.deletePostRoom(post.getPostId());
            }
        }
    }
}
