package softeer.be33ma3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softeer.be33ma3.controller.WebSocketHandler;
import softeer.be33ma3.domain.Center;
import softeer.be33ma3.domain.Offer;
import softeer.be33ma3.domain.Post;
import softeer.be33ma3.dto.request.OfferCreateDto;
import softeer.be33ma3.dto.response.OfferDetailDto;
import softeer.be33ma3.exception.UnauthorizedException;
import softeer.be33ma3.repository.OfferRepository;
import softeer.be33ma3.repository.PostRepository;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OfferService {

    private final OfferRepository offerRepository;
    private final PostRepository postRepository;
    private final WebSocketHandler webSocketHandler;

    // 견적 제시 댓글 하나 반환
    public OfferDetailDto showOffer(Long postId, Long offerId) {
        // 1. 해당 게시글 가져오기
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글"));
        // 2. 해당 댓글 가져오기
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
            throw new IllegalArgumentException("완료된 게시글");
        // TODO: 3. 센터 정보 가져오고 작성 가능한지 검증
        Center center = null;
        // 4. 댓글 생성하여 저장하기
        Offer offer = offerCreateDto.toEntity(post, center);
        offerRepository.save(offer);
    }

    // 견적 제시 댓글 수정
    @Transactional
    public void updateOffer(Long postId, Long offerId, OfferCreateDto offerCreateDto) {
        // 1. 해당 게시글 가져오기
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글"));
        // 2. 경매 완료된 게시글인지 검증
        if(post.isDone())
            throw new IllegalArgumentException("완료된 게시글");
        // TODO: 3. 센터 정보 가져오기
        Center center = null;
        // 4. 기존 댓글 가져오기
        Offer offer = offerRepository.findById(offerId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 견적"));
        // 5. 수정 가능한지 검증
        if(center.getCenterId() != offer.getCenter().getCenterId())
            throw new UnauthorizedException("작성자만 수정 가능합니다.");
        if(offerCreateDto.getPrice() > offer.getPrice())
            throw new IllegalArgumentException("기존 금액보다 낮은 금액으로만 수정 가능합니다.");
        // 6. 댓글 수정하기
        offer.setPrice(offerCreateDto.getPrice());
        offer.setContents(offerCreateDto.getContents());
        offerRepository.save(offer);
    }

    // 견적 제시 댓글 목록의 평균 제시 가격 계산하여 반환하기
    public static double calculateAvgPrice(List<Offer> offerList) {
        // 제시 가격의 합계, 개수 구하기
        IntSummaryStatistics stats = offerList.stream()
                .collect(Collectors.summarizingInt(Offer::getPrice));

        if(stats.getSum() == 0)
            return 0;
        return stats.getAverage();
    }

    // 게시글에 해당하는 견적 제시 댓글 리스트 실시간 전송
    public void sendOfferList2Writer(Long postId) {
        // 1. 해당 게시글 가져오기
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글"));
        // 2. 글 작성자 아이디 가져오기
        Long memberId = post.getMember().getMemberId();
        // 3. 게시글에 속해있는 댓글 목록 가져오기
        List<Offer> offerList = offerRepository.findByPost_PostId(postId);
        List<OfferDetailDto> offerDetailList = OfferDetailDto.fromEntityList(offerList);
        // 4. 전송하기
        webSocketHandler.sendData2Client(memberId, offerDetailList);
    }

    // 게시글에 견적을 작성한 모든 서비스 센터들에게 평균 견적 제시 가격 실시간 전송
    public void sendAvgPrice2Centers(Long postId) {
        // 1. 해당 게시글에 달린 모든 견적 가져오기
        List<Offer> offerList = offerRepository.findByPost_PostId(postId);
        // 2. 견적 작성자의 member id 가져오기
        List<Long> memberList = offerList.stream()
                .map(offer -> offer.getCenter().getMember().getMemberId())
                .toList();
        // 3. 평균 견적 가격 계산하기
        double avgPrice = calculateAvgPrice(offerList);
        // 4. 전송하기
        memberList.forEach(memberId -> webSocketHandler.sendData2Client(memberId, avgPrice));
    }
}