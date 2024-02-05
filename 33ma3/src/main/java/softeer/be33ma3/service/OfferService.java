package softeer.be33ma3.service;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import softeer.be33ma3.domain.Center;
import softeer.be33ma3.domain.Offer;
import softeer.be33ma3.domain.Post;
import softeer.be33ma3.dto.response.OfferDetailDto;
import softeer.be33ma3.dto.response.PostDetailDto;
import softeer.be33ma3.repository.OfferRepository;
import softeer.be33ma3.repository.PostRepository;
import softeer.be33ma3.response.DataResponse;
import softeer.be33ma3.response.SingleResponse;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OfferService {

    private final OfferRepository offerRepository;
    private final PostRepository postRepository;

    // 견적 제시 댓글 하나 반환
    public OfferDetailDto getOneOffer(Long postId, Long offerId) {
        // 1. 해당하는 게시글 가져와 존재하는지 판단하기
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글"));
        // 2. 해당하는 댓글 가져와 존재하는지 판단하기
        Offer offer = offerRepository.findById(offerId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 견적"));
        return OfferDetailDto.fromEntity(offer);
    }
}
