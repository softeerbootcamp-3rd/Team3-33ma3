package softeer.be33ma3.service;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.domain.Post;
import softeer.be33ma3.dto.request.PostCreateDto;
import softeer.be33ma3.repository.MemberRepository;
import softeer.be33ma3.repository.post.PostRepository;

import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@ActiveProfiles("test")
class SchedulingServiceTest {
    @Autowired private SchedulingService schedulingService;
    @Autowired private PostRepository postRepository;
    @Autowired private MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        postRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("")
    @Test
    void checkPostDDay(){
        //given
        Member client = Member.createClient("test1", "1234", null);
        Member savedClient = memberRepository.save(client);
        PostCreateDto postCreateDto = new PostCreateDto("승용차", "제네시스", 1, "서울시 강남구", "기스, 깨짐", "오일 교체", new ArrayList<>(),"게시글 생성 이미지 미포함");

        Post post1 = Post.createPost(postCreateDto, null, savedClient);
        Post post2 = Post.createPost(postCreateDto, null, savedClient);

        postRepository.saveAll(List.of(post1, post2));

        //when
        schedulingService.checkPostDDay();

        //then
        List<Post> posts = postRepository.findByDoneFalse();
        Assertions.assertThat(posts).hasSize(2);
    }
}
