package softeer.be33ma3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softeer.be33ma3.domain.Center;
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.domain.Post;
import softeer.be33ma3.domain.Review;
import softeer.be33ma3.dto.request.ReviewCreateDto;
import softeer.be33ma3.exception.BusinessException;
import softeer.be33ma3.repository.OfferRepository;
import softeer.be33ma3.repository.PostRepository;
import softeer.be33ma3.repository.ReviewRepository;

import static softeer.be33ma3.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final PostRepository postRepository;
    private final OfferRepository offerRepository;

    // 리뷰 생성하기
    @Transactional
    public Long createReview(Long postId, ReviewCreateDto reviewCreateDto, Member member) {
        // 1. 해당하는 게시글 가져오기
        Post post = postRepository.findById(postId).orElseThrow(() -> new BusinessException(NOT_FOUND_POST));
        if(!post.isDone()) {        // 경매 중일 경우 리뷰 작성 불가
            throw new BusinessException(NOT_DONE_POST);
        }
        // 2. 글 작성자인지 검증
        if(!post.getMember().equals(member)) {
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
        if(!review.getWriter().equals(member)) {
            throw new BusinessException(AUTHOR_ONLY_ACCESS);
        }
        reviewRepository.delete(review);
    }
}
