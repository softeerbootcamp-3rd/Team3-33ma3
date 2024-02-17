package softeer.be33ma3.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softeer.be33ma3.dto.response.AvgPriceDto;
import softeer.be33ma3.websocket.WebSocketHandler;
import softeer.be33ma3.domain.Center;
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.domain.Offer;
import softeer.be33ma3.domain.Post;
import softeer.be33ma3.dto.request.OfferCreateDto;
import softeer.be33ma3.dto.response.OfferDetailDto;
import softeer.be33ma3.exception.UnauthorizedException;
import softeer.be33ma3.repository.CenterRepository;
import softeer.be33ma3.repository.OfferRepository;
import softeer.be33ma3.repository.PostRepository;
import softeer.be33ma3.response.DataResponse;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class OfferService {

    private final OfferRepository offerRepository;
    private final PostRepository postRepository;
    private final CenterRepository centerRepository;
    private final WebSocketHandler webSocketHandler;

    // 견적 제시 댓글 하나 반환
    public OfferDetailDto showOffer(Long postId, Long offerId) {
        // 1. 해당 게시글 가져오기
        postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글"));
        // 2. 해당 댓글 가져오기
        Offer offer = offerRepository.findById(offerId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 견적"));
        return OfferDetailDto.fromEntity(offer);
    }

    // 견적 제시 댓글 생성
    @Transactional
    public void createOffer(Long postId, OfferCreateDto offerCreateDto, Member member) {
        // 1. 해당 게시글이 마감 전인지 확인
        Post post = checkNotDonePost(postId);
        // 2. 센터 정보 가져오기
        Center center = centerRepository.findByMember_MemberId(member.getMemberId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 센터"));
        // 3. 이미 견적을 작성한 센터인지 검증
        offerRepository.findByPost_PostIdAndCenter_CenterId(postId, center.getCenterId())
                .ifPresent(offer -> {throw new UnauthorizedException("이미 견적을 작성하였습니다.");});
        // 4. 댓글 생성하여 저장하기
        Offer offer = offerCreateDto.toEntity(post, center);
        offerRepository.save(offer);
    }

    // 견적 제시 댓글 수정
    @Transactional
    public void updateOffer(Long postId, Long offerId, OfferCreateDto offerCreateDto, Member member) {
        // 1. 해당 게시글이 마감 전인지 확인
        checkNotDonePost(postId);
        // 2. 센터 정보 가져오기
        Center center = centerRepository.findByMember_MemberId(member.getMemberId()).orElseThrow(() ->  new IllegalArgumentException("존재하지 않는 센터"));
        // 3. 기존 댓글 가져오기
        Offer offer = offerRepository.findById(offerId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 견적"));
        // 4. 수정 가능한지 검증
        if(center.getCenterId() != offer.getCenter().getCenterId())
            throw new UnauthorizedException("작성자만 수정 가능합니다.");
        if(offerCreateDto.getPrice() > offer.getPrice())
            throw new IllegalArgumentException("기존 금액보다 낮은 금액으로만 수정 가능합니다.");
        // 5. 댓글 수정하기
        offer.setPrice(offerCreateDto.getPrice());
        offer.setContents(offerCreateDto.getContents());
        offerRepository.save(offer);
    }

    // 견적 제시 댓글 삭제
    @Transactional
    public void deleteOffer(Long postId, Long offerId, Member member) {
        // 1. 해당 게시글이 마감 전인지 확인
        checkNotDonePost(postId);
        // 2. 센터 정보 가져오기
        Center center = centerRepository.findByMember_MemberId(member.getMemberId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 센터"));
        // 3. 기존 댓글 가져오기
        Offer offer = offerRepository.findById(offerId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 견적"));
        // 4. 댓글 작성자인지 검증
        if(!offer.getCenter().equals(center))
            throw new UnauthorizedException("작성자만 삭제 가능합니다.");
        // 5. 댓글 삭제
        offerRepository.delete(offer);
    }

    // 견적 제시 댓글 낙찰
    @Transactional
    public void selectOffer(Long postId, Long offerId, Member member) {
        // 1. 해당 게시글이 마감 전인지 확인
        Post post = checkNotDonePost(postId);
        // 3. 게시글 작성자의 접근인지 검증
        if(member.getMemberId() != post.getMember().getMemberId())
            throw new UnauthorizedException("작성자만 낙찰 가능합니다.");
        // 4. 낙찰을 희망하는 댓글 가져오기
        Offer offer = offerRepository.findById(offerId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 견적"));
        // 5. 댓글 낙찰, 게시글 마감 처리
        offer.setSelected();
        post.setDone();
        // TODO: 6. 서비스 센터들에게 낙찰 또는 경매 마감 메세지 보내기
        Long selectedMemberId = offer.getCenter().getMember().getMemberId();
        sendMessageAfterSelection(postId, selectedMemberId);
        webSocketHandler.deletePostRoom(postId);
    }

    // 해당 게시글을 가져오고, 마감 전인지 판단
    private Post checkNotDonePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글"));
        if(post.isDone())
            throw new IllegalArgumentException("완료된 게시글");
        return post;
    }

    // 해당 견적을 작성한 서비스 센터들의 member id 목록 반환
    private List<Long> findMemberIdsWithOfferList(List<Offer> offerList) {
        return offerList.stream()
                .map(offer -> offer.getCenter().getMember().getMemberId())
                .toList();
    }

    // 견적 제시 댓글 목록의 평균 제시 가격 계산하여 반환하기
    public static double calculateAvgPrice(List<Offer> offerList) {
        // 제시 가격의 합계, 개수 구하기
        IntSummaryStatistics stats = offerList.stream()
                .collect(Collectors.summarizingInt(Offer::getPrice));

        if(stats.getSum() == 0)
            return 0;
        return Math.round(stats.getAverage() * 10) / 10.0;      // 소수점 첫째 자리까지 반올림
    }

    public void sendAboutOfferUpdate(Long postId) {
        // 1. 해당 게시글 가져오기
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글"));
        // 2. 글 작성자 아이디 가져오기
        Long writerId = post.getMember().getMemberId();
        // 3. 글 작성자에게 업데이트된 댓글 목록 실시간 전송
        sendOfferList2Writer(postId, writerId);
        // 4. 그 외 접속한 유저에게 업데이트된 평균 제시 가격 실시간 전송
        sendAvgPrice2Others(postId, writerId);
    }

    // 게시글에 해당하는 견적 제시 댓글 리스트 실시간 전송
    public void sendOfferList2Writer(Long postId, Long memberId) {
        // 1. 게시글에 속해있는 댓글 목록 가져오기
        List<Offer> offerList = offerRepository.findByPost_PostId(postId);
        List<OfferDetailDto> offerDetailList = OfferDetailDto.fromEntityList(offerList);
        // 2. 해당 게시글 화면에 진입해 있을 경우 전송하기
        if(webSocketHandler.isInPostRoom(postId, memberId)) {
            log.info("게시글 작성자에게 실시간 댓글 전송 진행");
            webSocketHandler.sendData2Client(memberId, offerDetailList);
        }
    }

    // 해당 게시글 화면을 보고 있는 모든 유저들에게 평균 견적 제시 가격 실시간 전송
    public void sendAvgPrice2Others(Long postId, Long writerId) {
        // 1. 해당 게시글에 달린 모든 견적 가져오기
        List<Offer> offerList = offerRepository.findByPost_PostId(postId);
        // 2. 해당 게시글을 보고 있는 모든 유저들 가져오기 (글 작성자도 포함되어있을 경우 제외시키기)
        Set<Long> memberList = webSocketHandler.findAllMemberInPost(postId);
        if(memberList == null) {
            log.info("해당 게시글을 보고 있는 유저가 없습니다.");
            return;
        }
        memberList = memberList.stream()
                .filter(memberId -> !memberId.equals(writerId))
                .collect(Collectors.toSet());
        // 3. 평균 견적 가격 계산하기
        AvgPriceDto avgPriceDto = new AvgPriceDto(calculateAvgPrice(offerList));
        // 4. 전송하기
        memberList.forEach(memberId -> webSocketHandler.sendData2Client(memberId, avgPriceDto));
    }
  
    // 낙찰 처리 후 서비스 센터들에게 낙찰 메세지, 경매 마감 메세지 전송
    private void sendMessageAfterSelection(Long postId, Long selectedMemberId) {
        // 낙찰 메세지
        DataResponse<Long> selectAlert = DataResponse.success("제시한 견적이 낙찰되었습니다.", postId);
        webSocketHandler.sendData2Client(selectedMemberId, selectAlert);
        // 경매 마감 메세지
        DataResponse<Long> endAlert = DataResponse.success("견적 미선정으로 경매가 마감되었습니다. 다음 기회를 노려보세요!", postId);
        List<Offer> offerList = offerRepository.findByPost_PostId(postId);
        List<Long> memberIdsInPost = findMemberIdsWithOfferList(offerList);
        memberIdsInPost.stream()
                .filter(memberId -> !memberId.equals(selectedMemberId))
                .forEach(memberId -> webSocketHandler.sendData2Client(memberId, endAlert));
    }
}
