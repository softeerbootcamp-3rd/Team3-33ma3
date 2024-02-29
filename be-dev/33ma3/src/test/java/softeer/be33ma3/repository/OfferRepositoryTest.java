package softeer.be33ma3.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.domain.Offer;
import softeer.be33ma3.domain.Post;
import softeer.be33ma3.dto.request.PostCreateDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class OfferRepositoryTest {

    @Autowired
    private OfferRepository offerRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        offerRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("post id를 이용하여 게시글에 속해있는 모든 댓글 목록을 가져올 수 있다.")
    void findByPost_PostId() {
        // given
        // post 저장
        Post savedPost = savePost(null);
        // offer 저장
        saveOffer(1, "offer1", savedPost, null);
        saveOffer(2, "offer2", savedPost, null);
        saveOffer(3, "offer3", savedPost, null);
        // when
        List<Offer> offerList = offerRepository.findByPost_PostId(savedPost.getPostId());
        // then
        assertThat(offerList).hasSize(3);
    }

    @Test
    @DisplayName("댓글이 하나도 달리지 않은 게시글의 댓글 목록을 가져올 경우 빈 배열이 반환된다.")
    void findByPost_PostId_WithNoOffer() {
        // given
        Post savedPost = savePost(null);
        // when
        List<Offer> offers = offerRepository.findByPost_PostId(savedPost.getPostId());
        // then
        assertThat(offers).hasSize(0);
    }

    @Test
    @DisplayName("해당 게시글에서 해당하는 서비스 센터가 작성한 댓글을 반환할 수 있다.")
    void findByPost_PostIdAndCenter_CenterId() {
        // given
        // post 저장하기
        Post savedPost = savePost(null);
        // center 저장하기
        Member savedCenter = saveMember(2, "center1", "center1");
        // Offer 저장하기
        saveOffer(10, "offer1", savedPost, savedCenter);
        // when
        Optional<Offer> actual = offerRepository.findByPost_PostIdAndCenter_MemberId(savedPost.getPostId(), savedCenter.getMemberId());
        // then
        assertThat(actual).isPresent().get().extracting("price", "contents").containsExactly(10, "offer1");
    }

    @Test
    @DisplayName("해당 게시글에 해당 센터가 작성한 견적을 찾고 싶을 때, 만약 센터가 견적을 작성한 이력이 없을 경우 Optional.empty()로 반환된다.")
    void findByPost_PostIdAndCenter_CenterId_WithNoOffer() {
        // given
        // post 저장하기
        Post savedPost = savePost(null);
        // center 저장하기
        Member savedCenter = saveMember(2, "center1", "center1");
        // when
        Optional<Offer> actual = offerRepository.findByPost_PostIdAndCenter_MemberId(savedPost.getPostId(), savedCenter.getMemberId());
        // then
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("post id와 offer id를 이용하여 해당 게시글에 달린 해당하는 견적을 하나 반환한다.")
    void findByPost_PostIdAndOfferId() {
        // given
        // post 저장히기
        Post savedPost = savePost(null);
        // Offer 저장하기
        Offer savedOffer = saveOffer(10, "offer1", savedPost, null);
        // when
        Optional<Offer> actual = offerRepository.findByPost_PostIdAndOfferId(savedPost.getPostId(), savedOffer.getOfferId());
        // then
        assertThat(actual).isPresent().get().extracting("price", "contents")
                .containsExactly(10, "offer1");
    }

    @Test
    @DisplayName("post id, offer id에 해당하는 견적이 없을 경우 Optional.empty()로 반환된다.")
    void findByPost_PostIdAndOfferId_WithNoOffer() {
        // given
        // post 저장히기
        Post savedPost = savePost(null);
        // when
        Optional<Offer> actual = offerRepository.findByPost_PostIdAndOfferId(savedPost.getPostId(), 999L);
        // then
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("해당 게시글에서 낙찰된 센터의 member entity를 가져온다.")
    void findSelectedCenterByPostId() {
        // given
        // post 저장히기
        Post savedPost = savePost(null);
        // offer 저장하기
        Member savedCenter1 = saveMember(2, "center1", "center1");
        Offer savedOffer = saveOffer(10, "offer1", savedPost, savedCenter1);
        savedOffer.setSelected();
        offerRepository.save(savedOffer);
        Member savedCenter2 = saveMember(2, "center2", "center2");
        saveOffer(1, "offer2", savedPost, savedCenter2);
        // when
        Optional<Member> actual = offerRepository.findSelectedCenterByPostId(savedPost.getPostId());
        // then
        assertThat(actual).isPresent().get().usingRecursiveComparison().isEqualTo(savedCenter1);
    }

    @Test
    @DisplayName("해당 게시글에서 낙찰된 센터를 찾고 싶을 때 낙찰된 센터가 없을 경우 Optional.empty()가 반환된다.")
    void findSelectedCenterByPostId_WithNoSelect() {
        // given
        // post 저장히기
        Post savedPost = savePost(null);
        // offer 저장하기
        Member center1 = saveMember(2, "center1", "center1");
        Member center2 = saveMember(2, "center2", "center2");
        saveOffer(1, "offer1", savedPost, center1);
        saveOffer(2, "offer2", savedPost, center2);
        // when
        Optional<Member> actual = offerRepository.findSelectedCenterByPostId(savedPost.getPostId());
        // then
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("해당 게시글에 달린 견적의 평균 제시 가격을 계산하여 반환한다.")
    void findAvgPriceByPostId() {
        // given
        // post 저장히기
        Post savedPost = savePost(null);
        // offer 저장하기
        Member center1 = saveMember(2, "center1", "center1");
        Member center2 = saveMember(2, "center2", "center2");
        Member center3 = saveMember(2, "center3", "center3");
        Member center4 = saveMember(2, "center4", "center4");
        saveOffer(1, "offer1", savedPost, center1);
        saveOffer(2, "offer2", savedPost, center2);
        saveOffer(2, "offer3", savedPost, center3);
        saveOffer(2, "offer4", savedPost, center4);
        // when
        Optional<Double> actual = offerRepository.findAvgPriceByPostId(savedPost.getPostId());
        // then
        assertThat(actual).isPresent().get().isEqualTo(1.75);
    }

    @Test
    @DisplayName("해당 게시글에 견적이 달리지 않았을 경우 평균 제시 가격은 Optional.empty()로 반환된다.")
    void findAvgPriceByPostId_WithNoOffer() {
        // given
        Post savedPost = savePost(null);
        // when
        Optional<Double> actual = offerRepository.findAvgPriceByPostId(savedPost.getPostId());
        // then
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("해당 게시글에 견적을 제시한 모든 센터의 멤버 아이디를 가져온다.")
    void findCenterMemberIdsByPost_PostId() {
        // given
        // post 저장히기
        Post savedPost = savePost(null);
        // offer 저장하기
        Member center1 = saveMember(2, "center1", "center1");
        Member center2 = saveMember(2, "center2", "center2");
        Member center3 = saveMember(2, "center3", "center3");
        Member center4 = saveMember(2, "center4", "center4");
        saveOffer(1, "offer1", savedPost, center1);
        saveOffer(2, "offer2", savedPost, center2);
        saveOffer(2, "offer3", savedPost, center3);
        saveOffer(2, "offer4", savedPost, center4);
        List<Long> expected = List.of(center1.getMemberId(), center2.getMemberId(), center3.getMemberId(), center4.getMemberId());
        // when
        List<Long> actual = offerRepository.findCenterMemberIdsByPost_PostId(savedPost.getPostId());
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("해당 게시글에 견적을 제시한 모든 센터의 멤버 아이디를 가져올 때, 견적이 하나도 없을 경우 빈 배열이 반환된다.")
    void findCenterMemberIdsByPost_PostId_WithNoOffer() {
        // given
        // post 저장히기
        Post savedPost = savePost(null);
        // when
        List<Long> actual = offerRepository.findCenterMemberIdsByPost_PostId(savedPost.getPostId());
        // then
        assertThat(actual).hasSize(0);
    }

    private Offer saveOffer(int price, String contents, Post post, Member center) {
        Offer offer = Offer.builder()
                .price(price)
                .contents(contents)
                .post(post)
                .center(center).build();
        return offerRepository.save(offer);
    }

    private Post savePost(Member member) {
        PostCreateDto postCreateDto = new PostCreateDto("승용차", "제네시스", 0,
                "서울시 강남구", "기스, 깨짐", "오일 교체, 타이어 교체", new ArrayList<>(), "게시글 내용");
        return postRepository.save(Post.createPost(postCreateDto, null, member));
    }

    private Member saveMember(int memberType, String loginId, String password) {
        Member member = null;
        if(memberType == 1) {
            member = Member.createClient(loginId, password, null);
        }
        else {
            member = Member.createCenter(loginId, password, null);
        }
        return memberRepository.save(member);
    }
}
