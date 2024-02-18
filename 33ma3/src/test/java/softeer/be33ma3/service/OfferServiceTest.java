package softeer.be33ma3.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import softeer.be33ma3.domain.Center;
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.domain.Offer;
import softeer.be33ma3.domain.Post;
import softeer.be33ma3.dto.request.PostCreateDto;
import softeer.be33ma3.dto.response.OfferDetailDto;
import softeer.be33ma3.repository.CenterRepository;
import softeer.be33ma3.repository.MemberRepository;
import softeer.be33ma3.repository.OfferRepository;
import softeer.be33ma3.repository.PostRepository;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
class OfferServiceTest {

    @Autowired private OfferService offerService;
    @Autowired private OfferRepository offerRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private CenterRepository centerRepository;

    @AfterEach
    void tearDown() {
        offerRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
        centerRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("성공적으로 해당 견적 제시 댓글을 가져와 반환한다.")
    @Test
    void showOffer() {
        // given
        // post 저장하기
        PostCreateDto postCreateDto = new PostCreateDto("승용차", "제네시스", 3, "서울시 강남구",
                "기스, 깨짐", "오일 교체", new ArrayList<>(),"게시글 내용");
        Post post = Post.createPost(postCreateDto, null, null);
        Post savedPost = postRepository.save(post);
        // offer 저장하기
        Member member = Member.createMember(2, "center1Id", "center1Pw");
        Member savedMember = memberRepository.save(member);
        Center center = Center.createCenter("center1", 0.0, 0.0, savedMember);
        Center savedCenter = centerRepository.save(center);
        Offer savedOffer = createAndSaveOffer(1, "offer1", savedPost, savedCenter);
        // when
        OfferDetailDto actual = offerService.showOffer(savedPost.getPostId(), savedOffer.getOfferId());
        // then
        assertThat(actual).extracting("offerId", "price", "centerName", "contents")
                .containsExactly(savedOffer.getOfferId(), savedOffer.getPrice(), savedCenter.getCenterName(), savedOffer.getContents());
    }

    @DisplayName("존재하지 않는 게시글에 대한 견적 댓글 조회 요청일 경우 예외가 발생한다.")
    @Test
    void showOfferWithNoPost() {
        // given
        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            offerService.showOffer(1L, 1L);
        });
        // then
        assertThat(exception.getMessage()).isEqualTo("존재하지 않는 게시글");
    }

    @DisplayName("존재하지 않는 댓글에 대한 조회 요청일 경우 예외가 발생한다.")
    @Test
    void showOfferWithNoOffer() {
        // given
        PostCreateDto postCreateDto = new PostCreateDto("승용차", "제네시스", 3, "서울시 강남구",
                "기스, 깨짐", "오일 교체", new ArrayList<>(),"게시글 내용");
        Post post = Post.createPost(postCreateDto, null, null);
        Post savedPost = postRepository.save(post);
        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            offerService.showOffer(savedPost.getPostId(), 1L);
        });
        // then
        assertThat(exception.getMessage()).isEqualTo("존재하지 않는 견적");
    }

    @Test
    void createOffer() {
    }

    @Test
    void updateOffer() {
    }

    @Test
    void deleteOffer() {
    }

    @Test
    void selectOffer() {
    }

    @Test
    void calculateAvgPrice() {
    }

    @Test
    void sendAboutOfferUpdate() {
    }

    @Test
    void sendOfferList2Writer() {
    }

    @Test
    void sendAvgPrice2Others() {
    }

    private Offer createAndSaveOffer(int price, String contents, Post post, Center center) {
        Offer offer = Offer.builder()
                .price(price)
                .contents(contents)
                .post(post)
                .center(center).build();
        return offerRepository.save(offer);
    }
}
