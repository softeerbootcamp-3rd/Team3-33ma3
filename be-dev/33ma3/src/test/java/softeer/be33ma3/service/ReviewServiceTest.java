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
import softeer.be33ma3.dto.response.ShowAllReviewDto;
import softeer.be33ma3.dto.response.ShowCenterReviewsDto;
import softeer.be33ma3.exception.BusinessException;
import softeer.be33ma3.exception.ErrorCode;
import softeer.be33ma3.repository.ImageRepository;
import softeer.be33ma3.repository.MemberRepository;
import softeer.be33ma3.repository.RegionRepository;
import softeer.be33ma3.repository.post.PostRepository;
import softeer.be33ma3.repository.review.ReviewRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class ReviewServiceTest {
    @Autowired private MemberRepository memberRepository;
    @Autowired private ReviewRepository reviewRepository;
    @Autowired private ReviewService reviewService;
    @Autowired private PostRepository postRepository;
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

        Review review1 = createReview(savedPost1, writer1, center, "리뷰 작성 테스트1");
        Review review2 = createReview(savedPost2, writer1, center, "리뷰 작성 테스트2");

        reviewRepository.saveAll(List.of(review1, review2));

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

        Review review1 = createReview(savedPost1, writer1, center, "리뷰 작성 테스트1");
        Review review2 = createReview(savedPost2, writer2, center, "리뷰 작성 테스트2");

        reviewRepository.saveAll(List.of(review1, review2));

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

    private Post createPost(Region region, Member writer1) {
        PostCreateDto postCreateDto = new PostCreateDto("승용차", "제네시스", 3, "서울시 강남구", "기스, 깨짐", "오일 교체", new ArrayList<>(),"게시글 생성");
        Post post = Post.createPost(postCreateDto, region, writer1);
        Post savedPost = postRepository.save(post);
        return savedPost;
    }

    private Review createReview(Post savedPost, Member writer, Member center, String contents) {
        return Review.builder()
                .contents(contents)
                .score(4.5)
                .post(savedPost)
                .writer(writer)
                .center(center).build();
    }
}
