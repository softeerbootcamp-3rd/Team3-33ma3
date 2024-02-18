package softeer.be33ma3.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import softeer.be33ma3.domain.*;
import softeer.be33ma3.dto.request.OfferCreateDto;
import softeer.be33ma3.dto.request.PostCreateDto;
import softeer.be33ma3.dto.response.OfferDetailDto;
import softeer.be33ma3.exception.UnauthorizedException;
import softeer.be33ma3.repository.CenterRepository;
import softeer.be33ma3.repository.MemberRepository;
import softeer.be33ma3.repository.OfferRepository;
import softeer.be33ma3.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
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
        Post savedPost = createAndSavePost("승용차", "제네시스", 3, "서울시 강남구",
                "기스, 깨짐", "오일 교체", new ArrayList<>(),"게시글 내용", null, null);
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
            offerService.showOffer(999L, 999L);
        });
        // then
        assertThat(exception.getMessage()).isEqualTo("존재하지 않는 게시글");
    }

    @DisplayName("존재하지 않는 댓글에 대한 조회 요청일 경우 예외가 발생한다.")
    @Test
    void showOfferWithNoOffer() {
        // given
        Post savedPost = createAndSavePost("승용차", "제네시스", 3, "서울시 강남구",
                "기스, 깨짐", "오일 교체", new ArrayList<>(),"게시글 내용", null, null);
        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            offerService.showOffer(savedPost.getPostId(), 999L);
        });
        // then
        assertThat(exception.getMessage()).isEqualTo("존재하지 않는 견적");
    }

    @DisplayName("성공적으로 견적 댓글을 생성할 수 있다.")
    @Test
    void createOffer() {
        // given
        // post 저장
        Post savedPost = createAndSavePost("승용차", "제네시스", 3, "서울시 강남구",
                "기스, 깨짐", "오일 교체", new ArrayList<>(),"게시글 내용", null, null);
        // center 저장
        Member member = Member.createMember(2, "center1Id", "center1Pw");
        Member savedMember = memberRepository.save(member);
        Center center = Center.createCenter("center1", 0.0, 0.0, savedMember);
        centerRepository.save(center);
        // OfferCreateDto 생성
        OfferCreateDto offerCreateDto = new OfferCreateDto(10, "offer1");
        // when
        Long offerId = offerService.createOffer(savedPost.getPostId(), offerCreateDto, savedMember);
        // then
        Optional<Offer> actual = offerRepository.findById(offerId);
        assertThat(actual).isPresent().get().extracting("price", "contents")
                .containsExactly(10, "offer1");
    }

    @DisplayName("존재하지 않는 게시글에 대해 댓글 생성 요청시 예외가 발생한다.")
    @Test
    void createOfferWithNoPost() {
        // given
        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            offerService.createOffer(1L, null, null);
        });
        // then
        assertThat(exception.getMessage()).isEqualTo("존재하지 않는 게시글");
    }

    @DisplayName("경매 완료된 게시글에 대해 댓글 생성 요청시 예외가 발생한다.")
    @Test
    void createOfferWithAlreadyDonePost() {
        // given
        // post 저장
        Post savedPost = createAndSavePost("승용차", "제네시스", 3, "서울시 강남구",
                "기스, 깨짐", "오일 교체", new ArrayList<>(),"게시글 내용", null, null);
        savedPost.setDone();
        postRepository.save(savedPost);
        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            offerService.createOffer(savedPost.getPostId(), null, null);
        });
        // then
        assertThat(exception.getMessage()).isEqualTo("완료된 게시글");
    }

    @DisplayName("존재하지 않는 센터로 댓글 생성 요청시 예외가 발생한다.")
    @Test
    void createOfferWithNoCenter() {
        // given
        Post savedPost = createAndSavePost("승용차", "제네시스", 3, "서울시 강남구",
                "기스, 깨짐", "오일 교체", new ArrayList<>(),"게시글 내용", null, null);
        Member member = Member.createMember(2, "center1Id", "center1Pw");
        Member savedMember = memberRepository.save(member);
        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            offerService.createOffer(savedPost.getPostId(), null, savedMember);
        });
        // then
        assertThat(exception.getMessage()).isEqualTo("존재하지 않는 센터");
    }

    @DisplayName("이미 견적을 제시한 서비스 센터가 댓글 생성 요청시 예외가 발생한다.")
    @Test
    void createOfferWithAlreadyWroteCenter() {
        // given
        Post savedPost = createAndSavePost("승용차", "제네시스", 3, "서울시 강남구",
                "기스, 깨짐", "오일 교체", new ArrayList<>(),"게시글 내용", null, null);
        Member member = Member.createMember(2, "center1Id", "center1Pw");
        Member savedMember = memberRepository.save(member);
        Center center = Center.createCenter("center1", 0.0, 0.0, savedMember);
        centerRepository.save(center);
        offerService.createOffer(savedPost.getPostId(), new OfferCreateDto(10, "offer1"), savedMember);
        // 새로운 견적 제시 dto 생성하기
        OfferCreateDto offerCreateDto = new OfferCreateDto(1, "offer2");
        // when
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            offerService.createOffer(savedPost.getPostId(), offerCreateDto, savedMember);
        });
        // then
        assertThat(exception.getMessage()).isEqualTo("이미 견적을 작성하였습니다.");
    }

    @DisplayName("성공적으로 댓글을 수정할 수 있다.")
    @Test
    void updateOffer() {
        // given
        // post 저장
        Post savedPost = createAndSavePost("승용차", "제네시스", 3, "서울시 강남구",
                "기스, 깨짐", "오일 교체", new ArrayList<>(),"게시글 내용", null, null);
        // center 저장
        Member member = Member.createMember(2, "center1Id", "center1Pw");
        Member savedMember = memberRepository.save(member);
        Center center = Center.createCenter("center1", 0.0, 0.0, savedMember);
        centerRepository.save(center);
        // offer 저장
        OfferCreateDto offerCreateDto = new OfferCreateDto(10, "offer1");
        Long offerId = offerService.createOffer(savedPost.getPostId(), offerCreateDto, savedMember);
        offerCreateDto = new OfferCreateDto(10, "new offer1");
        // when
        offerService.updateOffer(savedPost.getPostId(), offerId, offerCreateDto, savedMember);
        // then
        Optional<Offer> actual = offerRepository.findById(offerId);
        assertThat(actual).isPresent().get().extracting("price", "contents")
                .containsExactly(10, "new offer1");
    }

    @DisplayName("존재하지 않는 댓글에 대해 수정 요청시 예외가 발생한다.")
    @Test
    void updateOfferWithNoOffer() {
        // given
        // post 저장
        Post savedPost = createAndSavePost("승용차", "제네시스", 3, "서울시 강남구",
                "기스, 깨짐", "오일 교체", new ArrayList<>(),"게시글 내용", null, null);
        // center 저장
        Member member = Member.createMember(2, "center1Id", "center1Pw");
        Member savedMember = memberRepository.save(member);
        Center center = Center.createCenter("center1", 0.0, 0.0, savedMember);
        centerRepository.save(center);
        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            offerService.updateOffer(savedPost.getPostId(), 999L, null, savedMember);
        });
        // then
        assertThat(exception.getMessage()).isEqualTo("존재하지 않는 견적");
    }

    @DisplayName("댓글 작성자가 아닌 다른 유저가 댓글 수정 요청시 예외가 발생한다.")
    @Test
    void updateOfferWithNotWriter() {
        // given
        // post 저장
        Post savedPost = createAndSavePost("승용차", "제네시스", 3, "서울시 강남구",
                "기스, 깨짐", "오일 교체", new ArrayList<>(),"게시글 내용", null, null);
        // center 저장
        Member member = Member.createMember(2, "center1Id", "center1Pw");
        Member savedMember = memberRepository.save(member);
        Center center = Center.createCenter("center1", 0.0, 0.0, savedMember);
        centerRepository.save(center);
        // offer 저장
        OfferCreateDto offerCreateDto = new OfferCreateDto(10, "offer1");
        Long offerId = offerService.createOffer(savedPost.getPostId(), offerCreateDto, savedMember);
        // 새로운 center 생성
        Member member2 = Member.createMember(2, "center2Id", "center2Pw");
        Member savedMember2 = memberRepository.save(member2);
        Center center2 = Center.createCenter("center2", 0.0, 0.0, savedMember2);
        centerRepository.save(center2);
        // when
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            offerService.updateOffer(savedPost.getPostId(), offerId, null, savedMember2);
        });
        // then
        assertThat(exception.getMessage()).isEqualTo("작성자만 수정 가능합니다.");
    }

    @DisplayName("기존보다 더 높은 가격으로 수정 요청시 예외가 발생한다.")
    @Test
    void updateOfferWithHighPrice() {
        // given
        // post 저장
        Post savedPost = createAndSavePost("승용차", "제네시스", 3, "서울시 강남구",
                "기스, 깨짐", "오일 교체", new ArrayList<>(),"게시글 내용", null, null);
        // center 저장
        Member member = Member.createMember(2, "center1Id", "center1Pw");
        Member savedMember = memberRepository.save(member);
        Center center = Center.createCenter("center1", 0.0, 0.0, savedMember);
        centerRepository.save(center);
        // offer 저장
        OfferCreateDto offerCreateDto = new OfferCreateDto(10, "offer1");
        Long offerId = offerService.createOffer(savedPost.getPostId(), offerCreateDto, savedMember);
        OfferCreateDto offerCreateDto2 = new OfferCreateDto(11, "new offer1");
        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            offerService.updateOffer(savedPost.getPostId(), offerId, offerCreateDto2, savedMember);
        });
        // then
        assertThat(exception.getMessage()).isEqualTo("기존 금액보다 낮은 금액으로만 수정 가능합니다.");
    }

    @DisplayName("성공적으로 댓글을 삭제할 수 있다.")
    @Test
    void deleteOffer() {
        // given
        // post 저장
        Post savedPost = createAndSavePost("승용차", "제네시스", 3, "서울시 강남구",
                "기스, 깨짐", "오일 교체", new ArrayList<>(),"게시글 내용", null, null);
        // center 저장
        Member member = Member.createMember(2, "center1Id", "center1Pw");
        Member savedMember = memberRepository.save(member);
        Center center = Center.createCenter("center1", 0.0, 0.0, savedMember);
        centerRepository.save(center);
        // offer 저장
        OfferCreateDto offerCreateDto = new OfferCreateDto(10, "offer1");
        Long offerId = offerService.createOffer(savedPost.getPostId(), offerCreateDto, savedMember);
        // when
        offerService.deleteOffer(savedPost.getPostId(), offerId, savedMember);
        // then
        Optional<Offer> actual = offerRepository.findById(offerId);
        assertThat(actual).isEmpty();
    }

    @DisplayName("댓글 작성자가 아닌 다른 유저의 댓글 삭제 요청시 예외가 발생한다.")
    @Test
    void deleteOfferWithNotWriter() {
        // given
        // post 저장
        Post savedPost = createAndSavePost("승용차", "제네시스", 3, "서울시 강남구",
                "기스, 깨짐", "오일 교체", new ArrayList<>(),"게시글 내용", null, null);
        // center 저장
        Member member = Member.createMember(2, "center1Id", "center1Pw");
        Member savedMember = memberRepository.save(member);
        Center center = Center.createCenter("center1", 0.0, 0.0, savedMember);
        centerRepository.save(center);
        // offer 저장
        OfferCreateDto offerCreateDto = new OfferCreateDto(10, "offer1");
        Long offerId = offerService.createOffer(savedPost.getPostId(), offerCreateDto, savedMember);
        // 새로운 center 생성
        Member member2 = Member.createMember(2, "center2Id", "center2Pw");
        Member savedMember2 = memberRepository.save(member2);
        Center center2 = Center.createCenter("center2", 0.0, 0.0, savedMember2);
        centerRepository.save(center2);
        // when
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            offerService.deleteOffer(savedPost.getPostId(), offerId, savedMember2);
        });
        // then
        assertThat(exception.getMessage()).isEqualTo("작성자만 삭제 가능합니다.");
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

    private Post createAndSavePost(String carType, String modelName, int deadLine, String location, String repairService, String tuneUpService,
                                   List<Long> centers, String contents, Region region, Member member) {
        PostCreateDto postCreateDto = new PostCreateDto(carType, modelName, deadLine, location,
                repairService, tuneUpService, centers, contents);
        Post post = Post.createPost(postCreateDto, region, member);
        return postRepository.save(post);
    }
}
