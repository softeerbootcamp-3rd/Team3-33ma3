package softeer.be33ma3.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.domain.Post;
import softeer.be33ma3.domain.Review;
import softeer.be33ma3.dto.request.PostCreateDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@ActiveProfiles("test")
@SpringBootTest
class ReviewRepositoryTest {

    @Autowired private ReviewRepository reviewRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private PostRepository postRepository;

    @AfterEach
    void tearDown() {
        reviewRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("해당 게시글을 통해 달린 리뷰글 하나를 반환한다.")
    void findByPost_PostId() {
        // given
        Member member = saveClient("user1", "user1");
        Post post = savePost(member);
        reviewRepository.save(Review.builder().writer(member).post(post).build());
        // when
        Optional<Review> actual = reviewRepository.findByPost_PostId(post.getPostId());
        // then
        assertThat(actual).isPresent();
    }

    @Test
    @DisplayName("존재하지 않는 리뷰글을 찾고자 할 경우 Optional.empty()를 반환한다.")
    void findByPost_PostId_withNoReview() {
        // given
        // when
        Optional<Review> actual = reviewRepository.findByPost_PostId(999L);
        // then
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("해당 센터의 평균 별점을 계산하여 반환한다.")
    void findAvgScoreByCenterId() {
        // given
        Member member = saveClient("user1", "user1");
        Member center = saveCenter("center1", "center1");
        Post post1 = savePost(member);
        Post post2 = savePost(member);
        Post post3 = savePost(member);
        Review review1 = Review.builder().score(1).writer(member).post(post1).center(center).build();
        Review review2 = Review.builder().score(3).writer(member).post(post2).center(center).build();
        Review review3 = Review.builder().score(0.5).writer(member).post(post3).center(center).build();
        reviewRepository.saveAll(List.of(review1, review2, review3));
        // when
        Optional<Double> avgScore = reviewRepository.findAvgScoreByCenterId(center.getMemberId());
        // then
        assertThat(avgScore).isPresent().get().isEqualTo(1.5);
    }

    @Test
    @DisplayName("센터에 대한 리뷰가 하나도 없을 경우 평균 별점은 Optional.empty()가 반환된다.")
    void findAvgScoreByCenterId_withNoReview() {
        // given
        Member center = saveCenter("center1", "center1");
        // when
        Optional<Double> avgScore = reviewRepository.findAvgScoreByCenterId(center.getMemberId());
        // then
        assertThat(avgScore).isEmpty();
    }

    @Test
    @DisplayName("센터에 해당하는 모든 리뷰 목록을 별점 높은 순으로 반환한다.")
    void findReviewsByCenterIdOrderByScore() {
        // given
        Member member = saveClient("user1", "user1");
        Member center1 = saveCenter("center1", "center1");
        Member center2 = saveCenter("center2", "center2");
        Post post1 = savePost(member);
        Post post2 = savePost(member);
        Post post3 = savePost(member);
        Post post4 = savePost(member);
        Post post5 = savePost(member);
        Review review1 = Review.builder().score(1).writer(member).post(post1).center(center1).build();
        Review review2 = Review.builder().score(3).writer(member).post(post2).center(center1).build();
        Review review3 = Review.builder().score(0.5).writer(member).post(post3).center(center1).build();
        Review review4 = Review.builder().score(2.5).writer(member).post(post4).center(center1).build();
        Review review5 = Review.builder().score(5).writer(member).post(post5).center(center2).build();
        reviewRepository.saveAll(List.of(review1, review2, review3, review4, review5));
        // when
        List<Review> actual = reviewRepository.findReviewsByCenterIdOrderByScore(center1.getMemberId());
        // then
        assertThat(actual).hasSize(4).extracting("score", "post.postId")
                .containsExactly(
                        tuple(3.0, post2.getPostId()),
                        tuple(2.5, post4.getPostId()),
                        tuple(1.0, post1.getPostId()),
                        tuple(0.5, post3.getPostId())
                );
    }

    private Member saveClient(String loginId, String password) {
        Member member = Member.createClient(loginId, password, null);
        return memberRepository.save(member);
    }

    private Member saveCenter(String loginId, String password) {
        Member member = Member.createCenter(loginId, password, null);
        return memberRepository.save(member);
    }

    private Post savePost(Member member) {
        PostCreateDto postCreateDto = new PostCreateDto("승용차", "제네시스", 0,
                "서울시 강남구", "기스, 깨짐", "오일 교체, 타이어 교체", new ArrayList<>(), "게시글 내용");
        return postRepository.save(Post.createPost(postCreateDto, null, member));
    }
}
