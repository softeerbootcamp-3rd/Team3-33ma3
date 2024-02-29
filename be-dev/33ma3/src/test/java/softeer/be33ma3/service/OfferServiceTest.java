package softeer.be33ma3.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import softeer.be33ma3.domain.Image;
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.domain.Offer;
import softeer.be33ma3.domain.Post;
import softeer.be33ma3.dto.request.OfferCreateDto;
import softeer.be33ma3.dto.request.PostCreateDto;
import softeer.be33ma3.dto.response.OfferDetailDto;
import softeer.be33ma3.exception.BusinessException;
import softeer.be33ma3.repository.ImageRepository;
import softeer.be33ma3.repository.MemberRepository;
import softeer.be33ma3.repository.OfferRepository;
import softeer.be33ma3.repository.PostRepository;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class OfferServiceTest {

    @Autowired private OfferRepository offerRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private ImageRepository imageRepository;
    @Autowired private OfferService offerService;

    @AfterEach
    void tearDown() {
        offerRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
        imageRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("원하는 견적 제시 댓글 하나를 반환한다.")
    void showOffer() {
        // given
        // post 저장
        Member member = saveClient("user1", "user1");
        Post post = savePost(member);
        // offer 저장
        Member center = saveCenter("center1", "center1");
        Offer offer = saveOffer(10, "offer1", post, center);
        OfferDetailDto expected = OfferDetailDto.fromEntity(offer, 0.0);
        // when
        OfferDetailDto actual = offerService.showOffer(post.getPostId(), expected.getOfferId());
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("존재하지 않는 게시글에 대해 견적 제시 댓글 조회 요청 시 예외가 발생한다.")
    void showOffer_withNoPost() {
        // given
        // post 저장
        Member member = saveClient("user1", "user1");
        Post post = savePost(member);
        // offer 저장
        Member center = saveCenter("center1", "center1");
        Offer offer = saveOffer(10, "offer1", post, center);
        // when
        BusinessException exception = assertThrows(BusinessException.class,
                () -> offerService.showOffer(post.getPostId() + 1, offer.getOfferId()));
        // then
        assertThat(exception.getErrorCode().getErrorMessage()).isEqualTo("존재하지 않는 게시글");
    }

    @Test
    @DisplayName("존재하지 않는 댓글에 대해 조회 요청 시 예외가 발생한다.")
    void showOffer_withNoOffer() {
        // given
        // post 저장
        Member member = saveClient("user1", "user1");
        Post post = savePost(member);
        // when
        BusinessException exception = assertThrows(BusinessException.class,
                () -> offerService.showOffer(post.getPostId(), 999L));
        // then
        assertThat(exception.getErrorCode().getErrorMessage()).isEqualTo("존재하지 않는 견적");
    }

    @Test
    @DisplayName("성공적으로 댓글을 작성할 수 있다.")
    void createOffer() {
        // given
        Member member = saveClient("user1", "user1");
        Post post = savePost(member);
        Member center = saveCenter("center1", "center1");
        OfferCreateDto offerCreateDto = new OfferCreateDto(10, "create offer");
        // when
        Long offerId = offerService.createOffer(post.getPostId(), offerCreateDto, center);
        // then
        Optional<Offer> actual = offerRepository.findByPost_PostIdAndOfferId(post.getPostId(), offerId);
        assertThat(actual).isPresent().get().extracting("price", "contents", "post", "center")
                .containsExactly(10, "create offer", post, center);
    }

    @Test
    @DisplayName("존재하지 않는 게시글에 대해 댓글 작성 요청 시 예외가 발생한다.")
    void createOffer_withNoPost() {
        // given
        Member center = saveCenter("center1", "center1");
        OfferCreateDto offerCreateDto = new OfferCreateDto(10, "create offer");
        // when
        BusinessException exception = assertThrows(BusinessException.class,
                () -> offerService.createOffer(999L, offerCreateDto, center));
        // then
        assertThat(exception.getErrorCode().getErrorMessage()).isEqualTo("존재하지 않는 게시글");
    }

    @Test
    @DisplayName("이미 마감된 게시글에 대해 댓글 작성 요청 시 예외가 발생한다.")
    void createOffer_withAlreadyDonPost() {
        // given
        Member member = saveClient("user1", "user1");
        PostCreateDto postCreateDto = new PostCreateDto("승용차", "제네시스", 0,
                "서울시 강남구", "기스, 깨짐", "오일 교체, 타이어 교체", new ArrayList<>(), "게시글 내용");
        Post post = Post.createPost(postCreateDto, null, member);
        post.setDone();
        Post savedPost = postRepository.save(post);
        Member center = saveCenter("center1", "center1");
        OfferCreateDto offerCreateDto = new OfferCreateDto(10, "create offer");
        // when
        BusinessException exception = assertThrows(BusinessException.class,
                () -> offerService.createOffer(savedPost.getPostId(), offerCreateDto, center));
        // then
        assertThat(exception.getErrorCode().getErrorMessage()).isEqualTo("마감된 게시글");
    }

    @Test
    @DisplayName("센터가 아닌 일반 유저가 댓글 작성 요청 시 예외가 발생한다.")
    void createOffer_withNotCenter() {
        // given
        Member member = saveClient("user1", "user1");
        Post post = savePost(member);
        Member member2 = saveClient("user2", "user2");
        OfferCreateDto offerCreateDto = new OfferCreateDto(10, "create offer");
        // when
        BusinessException exception = assertThrows(BusinessException.class,
                () -> offerService.createOffer(post.getPostId(), offerCreateDto, member2));
        // then
        assertThat(exception.getErrorCode().getErrorMessage()).isEqualTo("센터만 견적을 제시할 수 있습니다.");
    }

    @Test
    @DisplayName("이미 견적을 작성한 이력이 있는 센터가 댓글 작성 요청 시 예외가 발생한다.")
    void createOffer_withAlreadyWroteOffer() {
        // given
        Member member = saveClient("user1", "user1");
        Post post = savePost(member);
        Member center = saveCenter("center1", "center1");
        saveOffer(10, "create offer1", post, center);
        OfferCreateDto offerCreateDto2 = new OfferCreateDto(9, "create offer2");
        // when
        BusinessException exception = assertThrows(BusinessException.class,
                () -> offerService.createOffer(post.getPostId(), offerCreateDto2, center));
        // then
        assertThat(exception.getErrorCode().getErrorMessage()).isEqualTo("이미 견적을 작성하였습니다.");
    }

    @Test
    @DisplayName("성공적으로 댓글을 수정할 수 있다.")
    void updateOffer() {
        // given
        Member member = saveClient("user1", "user1");
        Post post = savePost(member);
        Member center = saveCenter("center1", "center1");
        Offer offer = saveOffer(10, "offer1", post, center);
        OfferCreateDto offerCreateDto = new OfferCreateDto(10, "update offer");
        // when
        offerService.updateOffer(post.getPostId(), offer.getOfferId(), offerCreateDto, center);
        // then
        Optional<Offer> actual = offerRepository.findByPost_PostIdAndOfferId(post.getPostId(), offer.getOfferId());
        assertThat(actual).isPresent().get().extracting("price", "contents").containsExactly(10, "update offer");
    }

    @Test
    @DisplayName("존재하지 않는 댓글에 대해 수정 요청 시 예외가 발생한다.")
    void updateOffer_withNoOffer() {
        // given
        Member member = saveClient("user1", "user1");
        Post post = savePost(member);
        // when
        BusinessException exception = assertThrows(BusinessException.class,
                () -> offerService.updateOffer(post.getPostId(), 999L, null, null));
        // then
        assertThat(exception.getErrorCode().getErrorMessage()).isEqualTo("존재하지 않는 견적");
    }

    @Test
    @DisplayName("댓글 작성자가 아닌 유저가 댓글 수정 요청 시 예외가 발생한다.")
    void updateOffer_withNotWriter() {
        // given
        Member member = saveClient("user1", "user1");
        Post post = savePost(member);
        Member center = saveCenter("center1", "center1");
        Offer offer = saveOffer(10, "offer1", post, center);
        Member center2 = saveCenter("center2", "center2");
        // when
        BusinessException exception = assertThrows(BusinessException.class,
                () -> offerService.updateOffer(post.getPostId(), offer.getOfferId(), null, center2));
        // then
        assertThat(exception.getErrorCode().getErrorMessage()).isEqualTo("작성자만 가능합니다.");
    }

    @Test
    @DisplayName("기존 댓글의 제시 가격보다 높은 가격으로 수정 요청 시 예외가 발생한다.")
    void updateOffer_biggerPrice() {
        // given
        Member member = saveClient("user1", "user1");
        Post post = savePost(member);
        Member center = saveCenter("center1", "center1");
        Offer offer = saveOffer(10, "offer1", post, center);
        OfferCreateDto offerCreateDto = new OfferCreateDto(11, "update offer");
        // when
        BusinessException exception = assertThrows(BusinessException.class,
                () -> offerService.updateOffer(post.getPostId(), offer.getOfferId(), offerCreateDto, center));
        // then
        assertThat(exception.getErrorCode().getErrorMessage()).isEqualTo("기존 금액보다 낮은 금액으로만 수정 가능합니다.");
    }

    @Test
    @DisplayName("성공적으로 댓글을 삭제할 수 있다.")
    void deleteOffer() {
        // given
        Member member = saveClient("user1", "user1");
        Post post = savePost(member);
        Member center = saveCenter("center1", "center1");
        Offer offer = saveOffer(10, "offer1", post, center);
        // when
        offerService.deleteOffer(post.getPostId(), offer.getOfferId(), center);
        // then
        Optional<Offer> actual = offerRepository.findByPost_PostIdAndOfferId(post.getPostId(), offer.getOfferId());
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("댓글 작성자가 아닌 유저가 댓글 삭제 요청 시 예외가 발생한다.")
    void deleteOffer_withNotWriter() {
        // given
        Member member = saveClient("user1", "user1");
        Post post = savePost(member);
        Member center = saveCenter("center1", "center1");
        Offer offer = saveOffer(10, "offer1", post, center);
        Member center2 = saveCenter("center2", "center2");
        // when
        BusinessException exception = assertThrows(BusinessException.class,
                () -> offerService.deleteOffer(post.getPostId(), offer.getOfferId(), center2));
        // then
        assertThat(exception.getErrorCode().getErrorMessage()).isEqualTo("작성자만 가능합니다.");
    }

    @Test
    @DisplayName("성공적으로 견적 제시 댓글을 낙찰할 수 있다.")
    void selectOffer() {
        // given
        Member member = saveClient("user1", "user1");
        Post post = savePost(member);
        Member center = saveCenter("center1", "center1");
        Offer offer = saveOffer(10, "offer1", post, center);
        // when
        offerService.selectOffer(post.getPostId(), offer.getOfferId(), member);
        // then
        Post actualPost = postRepository.findById(post.getPostId()).get();
        Offer actualOffer = offerRepository.findByPost_PostIdAndOfferId(post.getPostId(), offer.getOfferId()).get();
        assertTrue(actualPost.isDone());
        assertTrue(actualOffer.isSelected());
    }

    @Test
    @DisplayName("게시글 작성자가 아닌 유저가 댓글 낙찰 요청 시 예외가 발생한다.")
    void selectOffer_withNotWriter() {
        // given
        Member member = saveClient("user1", "user1");
        Post post = savePost(member);
        Member center = saveCenter("center1", "center1");
        Offer offer = saveOffer(10, "offer1", post, center);
        Member member2 = saveClient("user2", "user2");
        // when
        BusinessException exception = assertThrows(BusinessException.class,
                () -> offerService.selectOffer(post.getPostId(), offer.getOfferId(), member2));
        // then
        assertThat(exception.getErrorCode().getErrorMessage()).isEqualTo("작성자만 가능합니다.");
    }

    @Test
    @DisplayName("존재하지 않는 댓글을 낙찰 요청 시 예외가 발생한다.")
    void selectOffer_withNoOffer() {
        // given
        Member member = saveClient("user1", "user1");
        Post post = savePost(member);
        // when
        BusinessException exception = assertThrows(BusinessException.class,
                () -> offerService.selectOffer(post.getPostId(), 999L, member));
        // then
        assertThat(exception.getErrorCode().getErrorMessage()).isEqualTo("존재하지 않는 견적");
    }

    private Post savePost(Member member) {
        PostCreateDto postCreateDto = new PostCreateDto("승용차", "제네시스", 0,
                "서울시 강남구", "기스, 깨짐", "오일 교체, 타이어 교체", new ArrayList<>(), "게시글 내용");
        return postRepository.save(Post.createPost(postCreateDto, null, member));
    }

    private Offer saveOffer(int price, String contents, Post post, Member center) {
        Offer offer = Offer.builder()
                .price(price)
                .contents(contents)
                .post(post)
                .center(center).build();
        return offerRepository.save(offer);
    }

    private Member saveCenter(String loginId, String password) {
        Image savedImage = imageRepository.save(Image.createImage("profile.png", "profile.png"));
        Member member = Member.createCenter(loginId, password, savedImage);
        return memberRepository.save(member);
    }

    private Member saveClient(String loginId, String password) {
        Image savedImage = imageRepository.save(Image.createImage("profile.png", "profile.png"));
        Member member = Member.createClient(loginId, password, savedImage);
        return memberRepository.save(member);
    }
}
