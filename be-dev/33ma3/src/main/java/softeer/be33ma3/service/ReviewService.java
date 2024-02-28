package softeer.be33ma3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.domain.Post;
import softeer.be33ma3.domain.Review;
import softeer.be33ma3.dto.request.ReviewCreateDto;
import softeer.be33ma3.dto.response.OneReviewDto;
import softeer.be33ma3.dto.response.ShowCenterReviewsDto;
import softeer.be33ma3.dto.response.ShowAllReviewDto;
import softeer.be33ma3.exception.BusinessException;
import softeer.be33ma3.repository.MemberRepository;
import softeer.be33ma3.repository.OfferRepository;
import softeer.be33ma3.repository.PostRepository;
import softeer.be33ma3.repository.ReviewRepository;

import java.util.ArrayList;
import java.util.List;

import static softeer.be33ma3.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final PostRepository postRepository;
    private final OfferRepository offerRepository;
    private final MemberRepository memberRepository;

    // 리뷰 생성하기
    @Transactional
    public Long createReview(Long postId, ReviewCreateDto reviewCreateDto, Member member) {
        // 1. 해당하는 게시글 가져오기
        Post post = postRepository.findById(postId).orElseThrow(() -> new BusinessException(NOT_FOUND_POST));
        if(!post.isDone()) {        // 경매 중일 경우 리뷰 작성 불가
            throw new BusinessException(NOT_DONE_POST);
        }
        // 2. 글 작성자인지 검증
        if(!post.getMember().getMemberId().equals(member.getMemberId())) {
            throw new BusinessException(AUTHOR_ONLY_ACCESS);
        }
        // 3. 이미 리뷰를 작성헀는지 검증
        if(reviewRepository.findByPost_PostId(postId).isPresent()) {
            throw new BusinessException(ALREADY_WROTE_REVIEW);
        }
        // 4. 센터 가져오기
        Member center = offerRepository.findSelectedCenterByPostId(postId).orElseThrow(() -> new BusinessException(NO_SELECTED_CENTER));
        Review review = reviewCreateDto.toEntity(post, member, center);
        Review savedReview = reviewRepository.save(review);
        return savedReview.getReviewId();
    }

    // 리뷰 삭제하기
    @Transactional
    public void deleteReview(Long reviewId, Member member) {
        // 1. 기존 리뷰 가져오기
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new BusinessException(NOT_FOUND_REVIEW));
        // 2. 리뷰 작성자인지 검증
        if(!review.getWriter().getMemberId().equals(member.getMemberId())) {
            throw new BusinessException(AUTHOR_ONLY_ACCESS);
        }
        reviewRepository.delete(review);
    }

    public List<ShowAllReviewDto> showAllReview() {    //전체 리뷰 조회
        return reviewRepository.findReviewGroupByCenter();
    }

    public ShowCenterReviewsDto showOneCenterReviews(Long centerId) {   //특정 센터 리뷰 조회
        Member center = memberRepository.findById(centerId).orElseThrow(() -> new BusinessException(NOT_FOUND_CENTER));
        List<OneReviewDto> oneReviewDtos = new ArrayList<>();
        double totalScore = 0.0;
        List<Review> reviews = reviewRepository.findReviewsByCenterIdOrderByScore(centerId);

        for (Review review : reviews) {
            totalScore += review.getScore();
            oneReviewDtos.add(OneReviewDto.create(review));
        }

        double scoreAvg = Math.round(totalScore/reviews.size() * 10 ) / 10.0;   //별점 평균
        return ShowCenterReviewsDto.create(center, scoreAvg, oneReviewDtos);
    }
}
