package softeer.be33ma3.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import softeer.be33ma3.domain.Center;
import softeer.be33ma3.domain.Offer;
import softeer.be33ma3.domain.Post;
import softeer.be33ma3.dto.request.PostCreateDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@ActiveProfiles("test")
@SpringBootTest
class OfferRepositoryTest {

    @Autowired
    private OfferRepository offerRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CenterRepository centerRepository;

    @BeforeEach
    void setUp() {
        // post 저장
        PostCreateDto postCreateDto = new PostCreateDto("승용차", "제네시스", 3,
                "서울시 강남구", "기스, 깨짐", "오일 교체, 타이어 교체",
                new ArrayList<>(), "게시글");
        Post post1 = Post.createPost(postCreateDto, null, null);
        Post post2 = Post.createPost(postCreateDto, null, null);
        Post savedPost1 = postRepository.save(post1);
        Post savedPost2 = postRepository.save(post2);
        // center 저장
        Center center1 = Center.createCenter("서비스센터1", 0, 0, null);
        Center center2 = Center.createCenter("서비스센터2", 0, 0, null);
        Center center3 = Center.createCenter("서비스센터3", 0, 0, null);
        Center savedCenter1 = centerRepository.save(center1);
        Center savedCenter2 = centerRepository.save(center2);
        Center savedCenter3 = centerRepository.save(center3);

        // offer 저장
        Offer offer1 = createOffer(1, "offer1", savedPost1, savedCenter1);
        Offer offer2 = createOffer(2, "offer2", savedPost1, savedCenter2);
        Offer offer3 = createOffer(3, "offer3", savedPost2, savedCenter3);
        offerRepository.saveAll(List.of(offer1, offer2, offer3));
    }

    @AfterEach
    void tearDown() {
        offerRepository.deleteAllInBatch();
        centerRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
    }

    @DisplayName("post id를 이용하여 게시글에 속해있는 모든 댓글 목록을 가져올 수 있다.")
    @Test
    void findByPost_PostId() {
        // given / when
        List<Offer> offerList = offerRepository.findByPost_PostId(1L);
        // then
        assertThat(offerList).hasSize(2)
                .extracting("price", "contents")
                .contains(
                        tuple(1, "offer1"),
                        tuple(2, "offer2")
                );
    }

    @DisplayName("해당 게시글에서 해당하는 서비스 센터가 작성한 댓글을 반환할 수 있다.")
    @Test
    void findByPost_PostIdAndCenter_CenterId() {
        // given / when
        Optional<Offer> actual = offerRepository.findByPost_PostIdAndCenter_CenterId(1L, 1L);
        // then
        assertThat(actual).isPresent()
                        .get().extracting("price", "contents")
                        .containsExactly(1, "offer1");
    }

    Offer createOffer(int price, String contents, Post post, Center center) {
        return Offer.builder()
                .price(price)
                .contents(contents)
                .post(post)
                .center(center).build();
    }
}
