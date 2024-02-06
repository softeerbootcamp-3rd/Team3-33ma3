package softeer.be33ma3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softeer.be33ma3.domain.Center;
import softeer.be33ma3.domain.Offer;
import softeer.be33ma3.domain.Post;
import softeer.be33ma3.dto.request.OfferCreateDto;
import softeer.be33ma3.dto.response.OfferDetailDto;
import softeer.be33ma3.repository.CenterRepository;
import softeer.be33ma3.repository.OfferRepository;
import softeer.be33ma3.repository.PostRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OfferService {

    private final PostService postService;
    private final OfferRepository offerRepository;
    private final PostRepository postRepository;
    private final CenterRepository centerRepository;

    // 견적 제시 댓글 하나 반환
    public OfferDetailDto getOneOffer(Long postId, Long offerId) {
        // 1. 해당하는 게시글 가져와 존재하는지 판단하기
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글"));
        // 2. 해당하는 댓글 가져와 존재하는지 판단하기
        Offer offer = offerRepository.findById(offerId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 견적"));
        return OfferDetailDto.fromEntity(offer);
    }

    // 견적 제시 댓글 생성
    @Transactional
    public void createOffer(Long postId, OfferCreateDto offerCreateDto) {
        // 1. 해당 게시글 가져오기
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글"));
        // 2. 경매 완료된 게시글인지 검증
        if(post.isDone())
            throw new IllegalArgumentException("경매가 완료되었습니다.");
        // TODO: 3. 센터 정보 가져오고 작성 가능한지 검증
        Center center = null;
        // 3. 댓글 생성하여 저장하기
        Offer offer = offerCreateDto.toEntity(post, center);
        offerRepository.save(offer);
    }
}
