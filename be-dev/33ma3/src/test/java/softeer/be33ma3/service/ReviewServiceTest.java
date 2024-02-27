package softeer.be33ma3.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import softeer.be33ma3.domain.*;
import softeer.be33ma3.dto.request.PostCreateDto;
import softeer.be33ma3.dto.request.ReviewCreateDto;
import softeer.be33ma3.dto.response.ShowAllReviewDto;
import softeer.be33ma3.dto.response.ShowCenterReviewsDto;
import softeer.be33ma3.exception.BusinessException;
import softeer.be33ma3.exception.ErrorCode;
import softeer.be33ma3.repository.ImageRepository;
import softeer.be33ma3.repository.MemberRepository;
import softeer.be33ma3.repository.OfferRepository;
import softeer.be33ma3.repository.RegionRepository;
import softeer.be33ma3.repository.PostRepository;
import softeer.be33ma3.repository.ReviewRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
class ReviewServiceTest {
    @Autowired private MemberRepository memberRepository;
    @Autowired private ReviewRepository reviewRepository;
    @Autowired private ReviewService reviewService;
    @Autowired private PostRepository postRepository;
    @Autowired private OfferRepository offerRepository;
    @Autowired private RegionRepository regionRepository;
    @Autowired private ImageRepository imageRepository;

    @BeforeEach
    void setUp(){
        //회원 저장
        Image image1 = Image.createImage("test", "test.png");
        Image image2 = Image.createImage("test", "test.png");
        Image image3 = Image.createImage("test", "test.png");
        Image savedImage1 = imageRepository.save(image1);
        Image savedImage2 = imageRepository.save(image2);
        Image savedImage3 = imageRepository.save(image3);

        Member writer1 = Member.createClient( "client1", "1234", savedImage1);
        Member writer2 = Member.createClient( "client2", "1234", savedImage2);
        Member center = Member.createCenter( "center1", "1234", savedImage3);
        memberRepository.saveAll(List.of(writer1, writer2,center));
        regionRepository.save(new Region(1L, "강남구"));
    }

    @AfterEach
    void tearDown(){
        reviewRepository.deleteAllInBatch();
        offerRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
        regionRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
        imageRepository.deleteAllInBatch();
    }

    @DisplayName("센터별 모든 리뷰를 별점순으로 정렬하여 확인할 수 있다.")
    @Test
    void showAllReview(){
        //given
        Member writer1 = memberRepository.findMemberByLoginId("client1").get();
        Member center = memberRepository.findMemberByLoginId("center1").get();
        Region region = regionRepository.findByRegionName("강남구").get();

        Post savedPost1 = createPost(region, writer1);
        Post savedPost2 = createPost(region, writer1);

        createReview(savedPost1, writer1, center, 4.5, "리뷰 작성 테스트1");
        createReview(savedPost2, writer1, center, 4.5, "리뷰 작성 테스트2");

        //when
        List<ShowAllReviewDto> showAllReviewDtos = reviewService.showAllReview();

        //then
        assertThat(showAllReviewDtos).hasSize(1)
                .extracting("scoreAvg", "reviewCount","centerName")
                .containsExactly(tuple(4.5, 2L, "center1"));
    }

    @DisplayName("특정 센터에 대한 리뷰들을 별점순으로 정렬하여 반환한다.")
    @Test
    void showOneCenterReviews(){
        //given
        Member writer1 = memberRepository.findMemberByLoginId("client1").get();
        Member writer2 = memberRepository.findMemberByLoginId("client2").get();
        Member center = memberRepository.findMemberByLoginId("center1").get();
        Region region = regionRepository.findByRegionName("강남구").get();

        Post savedPost1 = createPost(region, writer1);
        Post savedPost2 = createPost(region, writer2);

        createReview(savedPost1, writer1, center, 4.5, "리뷰 작성 테스트1");
        createReview(savedPost2, writer2, center, 4.5, "리뷰 작성 테스트2");

        //when
        ShowCenterReviewsDto showCenterReviewsDto = reviewService.showOneCenterReviews(center.getMemberId());

        //then
        assertThat(showCenterReviewsDto.getReviews()).hasSize(2)
                .extracting("writerName", "contents", "score")
                .containsExactlyInAnyOrder(tuple("client1", "리뷰 작성 테스트1", 4.5),
                        tuple("client2", "리뷰 작성 테스트2", 4.5));
    }

    @DisplayName("센터가 존재하지 않는 경우 예외가 발생한다.")
    @Test
    void showOneCenterReviewsWithUnknownCenter(){
        //given
        Long unknownCenterId = 0L;

        //when //then
        assertThatThrownBy(() -> reviewService.showOneCenterReviews(unknownCenterId))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.NOT_FOUND_CENTER);
    }

    @Test
    @DisplayName("성공적으로 리뷰를 작성할 수 있다.")
    void createReview() {
        // given
        Member writer1 = memberRepository.findMemberByLoginId("client1").get();
        Member center = memberRepository.findMemberByLoginId("center1").get();
        Post post = createPost(null, writer1);
        post.setDone();
        Post savedPost = postRepository.save(post);
        Offer offer = createOffer(10, "offer1", savedPost, center);
        offer.setSelected();
        offerRepository.save(offer);
        ReviewCreateDto reviewCreateDto = new ReviewCreateDto(4.5, "review");
        // when
        reviewService.createReview(savedPost.getPostId(), reviewCreateDto, writer1);
        // then
        Optional<Review> actual = reviewRepository.findByPost_PostId(savedPost.getPostId());
        assertThat(actual).isPresent().get().extracting("score", "contents")
                .containsExactly(4.5, "review");
    }

    @Test
    @DisplayName("존재하지 않는 게시글에 대해 리뷰 작성 요청 시 예외가 발생한다.")
    void createReview_withNoPost() {
        // given
        Member writer1 = memberRepository.findMemberByLoginId("client1").get();
        ReviewCreateDto reviewCreateDto = new ReviewCreateDto(4.5, "review");
        // when
        BusinessException exception = assertThrows(BusinessException.class,
                () -> reviewService.createReview(999L, reviewCreateDto, writer1));
        // then
        assertThat(exception.getErrorCode().getErrorMessage()).isEqualTo("존재하지 않는 게시글");
    }

    @Test
    @DisplayName("경매 중인 게시글에 대해 리뷰 작성 요청 시 예외가 발생한다.")
    void createReview_withNotDonePost() {
        // given
        Member writer1 = memberRepository.findMemberByLoginId("client1").get();
        Post post = createPost(null, writer1);
        ReviewCreateDto reviewCreateDto = new ReviewCreateDto(4.5, "review");
        // when
        BusinessException exception = assertThrows(BusinessException.class,
                () -> reviewService.createReview(post.getPostId(), reviewCreateDto, writer1));
        // then
        assertThat(exception.getErrorCode().getErrorMessage()).isEqualTo("경매가 진행 중입니다.");
    }

    @Test
    @DisplayName("게시글 작성자가 아닌 유저가 리뷰 작성 요청 시 예외가 발생한다.")
    void createReview_withNotWriter() {
        // given
        Member writer1 = memberRepository.findMemberByLoginId("client1").get();
        Post post = createPost(null, writer1);
        post.setDone();
        Post savedPost = postRepository.save(post);
        ReviewCreateDto reviewCreateDto = new ReviewCreateDto(4.5, "review");
        Member writer2 = memberRepository.findMemberByLoginId("client2").get();
        // when
        BusinessException exception = assertThrows(BusinessException.class,
                () -> reviewService.createReview(savedPost.getPostId(), reviewCreateDto, writer2));
        // then
        assertThat(exception.getErrorCode().getErrorMessage()).isEqualTo("작성자만 가능합니다.");
    }

    @Test
    @DisplayName("해당 게시글에서 이미 리뷰를 작성한 유저가 다시 리뷰 작성 요청 시 예외가 발생한다.")
    void createReview_withAlreadyWroteReview() {
        // given
        Member writer1 = memberRepository.findMemberByLoginId("client1").get();
        Member center = memberRepository.findMemberByLoginId("center1").get();
        Post post = createPost(null, writer1);
        post.setDone();
        Post savedPost = postRepository.save(post);
        Offer offer = createOffer(10, "offer1", savedPost, center);
        offer.setSelected();
        offerRepository.save(offer);
        createReview(savedPost, writer1, center, 5, "review");
        ReviewCreateDto reviewCreateDto = new ReviewCreateDto(4.5, "review");
        // when
        BusinessException exception = assertThrows(BusinessException.class,
                () -> reviewService.createReview(savedPost.getPostId(), reviewCreateDto, writer1));
        // then
        assertThat(exception.getErrorCode().getErrorMessage()).isEqualTo("이미 리뷰를 작성하였습니다.");
    }

    @Test
    @DisplayName("해당 게시글에서 닉찰 처리된 센터가 없는데 리뷰 작성 요청 시 예외가 발생한다.")
    void createReview_withNoSelectedCenter() {
        // given
        Member writer1 = memberRepository.findMemberByLoginId("client1").get();
        Member center = memberRepository.findMemberByLoginId("center1").get();
        Post post = createPost(null, writer1);
        post.setDone();
        Post savedPost = postRepository.save(post);
        Offer offer = createOffer(10, "offer1", savedPost, center);
        ReviewCreateDto reviewCreateDto = new ReviewCreateDto(4.5, "review");
        // when
        BusinessException exception = assertThrows(BusinessException.class,
                () -> reviewService.createReview(savedPost.getPostId(), reviewCreateDto, writer1));
        // then
        assertThat(exception.getErrorCode().getErrorMessage()).isEqualTo("낙찰 처리된 센터가 없습니다.");
    }

    @Test
    @DisplayName("성공적으로 리뷰를 삭제할 수 있다.")
    void deleteReview() {
        // given
        Member writer1 = memberRepository.findMemberByLoginId("client1").get();
        Member center = memberRepository.findMemberByLoginId("center1").get();
        Post post = createPost(null, writer1);
        post.setDone();
        Post savedPost = postRepository.save(post);
        Offer offer = createOffer(10, "offer1", savedPost, center);
        offer.setSelected();
        offerRepository.save(offer);
        Review review = createReview(savedPost, writer1, center, 4.5, "review1");
        // when
        reviewService.deleteReview(review.getReviewId(), writer1);
        // then
        Optional<Review> actual = reviewRepository.findById(review.getReviewId());
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("작성자가 아닌 유저가 리뷰 삭제 요청 시 예외가 발생한다.")
    void deleteReview_withNotWriter() {
        // given
        Member writer1 = memberRepository.findMemberByLoginId("client1").get();
        Member center = memberRepository.findMemberByLoginId("center1").get();
        Post post = createPost(null, writer1);
        post.setDone();
        Post savedPost = postRepository.save(post);
        Offer offer = createOffer(10, "offer1", savedPost, center);
        offer.setSelected();
        offerRepository.save(offer);
        Review review = createReview(savedPost, writer1, center, 4.5, "review1");
        Member writer2 = memberRepository.findMemberByLoginId("client2").get();
        // when
        BusinessException exception = assertThrows(BusinessException.class,
                () -> reviewService.deleteReview(review.getReviewId(), writer2));
        // then
        assertThat(exception.getErrorCode().getErrorMessage()).isEqualTo("작성자만 가능합니다.");
    }

    @Test
    @DisplayName("존재하지 않는 리뷰에 대해 삭제 요청 시 예외가 발생한다.")
    void deleteReview_withNoReview() {
        // given
        Member writer1 = memberRepository.findMemberByLoginId("client1").get();
        // when
        BusinessException exception = assertThrows(BusinessException.class,
                () -> reviewService.deleteReview(999L, writer1));
        // then
        assertThat(exception.getErrorCode().getErrorMessage()).isEqualTo("존재하지 않는 리뷰");
    }

    private Post createPost(Region region, Member writer1) {
        PostCreateDto postCreateDto = new PostCreateDto("승용차", "제네시스", 3, "서울시 강남구", "기스, 깨짐", "오일 교체", new ArrayList<>(),"게시글 생성");
        Post post = Post.createPost(postCreateDto, region, writer1);
        Post savedPost = postRepository.save(post);
        return savedPost;
    }

    private Review createReview(Post savedPost, Member writer, Member center, double score, String contents) {
        Review review = Review.builder()
                .contents(contents)
                .score(score)
                .post(savedPost)
                .writer(writer)
                .center(center).build();
        return reviewRepository.save(review);
    }

    private Offer createOffer(int price, String contents, Post post, Member center) {
        Offer offer = Offer.builder()
                .price(price)
                .contents(contents)
                .post(post)
                .center(center).build();
        return offerRepository.save(offer);
    }
}
