package softeer.be33ma3.repository.post;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.domain.Post;
import softeer.be33ma3.domain.Region;
import softeer.be33ma3.dto.request.PostCreateDto;
import softeer.be33ma3.repository.MemberRepository;
import softeer.be33ma3.repository.RegionRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PostRepositoryTest {
    @Autowired private PostRepository postRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private RegionRepository regionRepository;

    @AfterEach
    void tearDown() {
        postRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("")
    @Test
    void findByDoneFalse(){
        //given
        Member client = Member.createClient("test1", "1234");
        PostCreateDto postCreateDto = new PostCreateDto("승용차", "제네시스", 3, "서울시 강남구", "기스, 깨짐", "오일 교체", new ArrayList<>(),"내용");
        regionRepository.save(new Region(1L, "강남구"));
        Post post = Post.createPost(postCreateDto, null, client);
        postRepository.save(post);

        //when
        List<Post> posts = postRepository.findByDoneFalse();

        //then
        assertThat(posts).hasSize(1);
    }
}
