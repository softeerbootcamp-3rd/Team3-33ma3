package softeer.be33ma3.service;

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
import softeer.be33ma3.repository.PostRepository;
import softeer.be33ma3.repository.RegionRepository;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("test")
@SpringBootTest
@Transactional
class PostServiceTest {
//    @Autowired private MemberRepository memberRepository;
//    @Autowired private PostService postService;
//    @Autowired private PostRepository postRepository;
//    @Autowired private RegionRepository regionRepository;
//
//    @DisplayName("주어진 정보로 게시글을 생성한다.")
//    @Test
//    void createPost(){
//        //given
//        Region region = new Region(1L, "강남구");
//        regionRepository.save(region);
//
//        Member member = new Member(1, "member1", "1234");
//        Member center1 = new Member(2, "center1", "1235");
//        Member center2 = new Member(2, "center2", "1236");
//
//        memberRepository.saveAll(List.of(member, center1, center2));
//
//        PostCreateDto postCreateDto = new PostCreateDto("승용차", "제네시스", 3,
//                "서울시 강남구 10Km 반경", "깨짐, 기스", "오일 교체", Arrays.asList(1L), Arrays.asList(), "댓글 댓글");
//
//        //when
//        postService.createPost(member, postCreateDto);
//
//        //then
//        Post post = postRepository.findById(1L).get();
//        assertThat(post).extracting("carType", "modelName", "deadline", "repairService","tuneUpService", "contents")
//                .containsExactly("승용차", "제네시스", 3, "깨짐, 기스", "오일 교체", "댓글 댓글");
//    }
}
