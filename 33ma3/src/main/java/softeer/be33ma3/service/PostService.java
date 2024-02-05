package softeer.be33ma3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import softeer.be33ma3.domain.Offer;
import softeer.be33ma3.repository.OfferRepository;
import softeer.be33ma3.repository.PostRepository;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final OfferRepository offerRepository;
    private final PostRepository postRepository;

    // 해당 게시글의 평균 견적 제시 가격 반환
    private double priceAvgOfPost(Long postId) {
        // 해당 게시글의 견적 모두 가져오기
        List<Offer> offerList = offerRepository.findAllByPostId(postId);

        // 제시 가격의 합계, 개수 구하기
        IntSummaryStatistics stats = offerList.stream()
                .collect(Collectors.summarizingInt(Offer::getPrice));

        return stats.getAverage();
    }
}
