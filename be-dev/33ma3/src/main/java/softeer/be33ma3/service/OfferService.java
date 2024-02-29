package softeer.be33ma3.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softeer.be33ma3.dto.response.AvgPriceDto;
import softeer.be33ma3.exception.BusinessException;
import softeer.be33ma3.repository.ReviewRepository;
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.domain.Offer;
import softeer.be33ma3.domain.Post;
import softeer.be33ma3.dto.request.OfferCreateDto;
import softeer.be33ma3.dto.response.OfferDetailDto;
import softeer.be33ma3.repository.OfferRepository;
import softeer.be33ma3.repository.PostRepository;
import softeer.be33ma3.response.DataResponse;
import softeer.be33ma3.websocket.WebSocketService;

import java.util.List;
import java.util.Set;

import static softeer.be33ma3.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class OfferService {

    private final OfferRepository offerRepository;
    private final PostRepository postRepository;
    private final ReviewRepository reviewRepository;
    private final WebSocketService webSocketService;

    private static final String OFFER_CREATE = "CREATE";
    private static final String OFFER_UPDATE = "UPDATE";
    private static final String OFFER_DELETE = "DELETE";
    private static final String SELECT_SUCCESS = "SELECT SUCCESS";
    private static final String SELECT_FAIL = "SELECT FAIL";
    private static final String SELECT_END = "SELECT END";

    // 견적 제시 댓글 하나 반환
    public OfferDetailDto showOffer(Long postId, Long offerId) {
        // 1. 해당 게시글 가져오기
        postRepository.findById(postId).orElseThrow(() -> new BusinessException(NOT_FOUND_POST));
        // 2. 해당 댓글 가져오기
        Offer offer = offerRepository.findByPost_PostIdAndOfferId(postId, offerId).orElseThrow(() -> new BusinessException(NOT_FOUND_OFFER));
        Double score = reviewRepository.findAvgScoreByCenterId(offer.getCenter().getMemberId()).orElse(0.0);
        return OfferDetailDto.fromEntity(offer, score);
    }

    // 견적 제시 댓글 생성
    @Transactional
    public Long createOffer(Long postId, OfferCreateDto offerCreateDto, Member member) {
        // 1. 해당 게시글이 마감 전인지 확인
        Post post = checkNotDonePost(postId);
        if(!member.isCenter()) {
            throw new BusinessException(NOT_CENTER);
        }
        // 2. 이미 견적을 작성한 센터인지 검증
        offerRepository.findByPost_PostIdAndCenter_MemberId(postId, member.getMemberId()).ifPresent(offer -> {throw new BusinessException(ALREADY_SUBMITTED);});
        // 3. 댓글 생성하여 저장하기
        Offer offer = offerCreateDto.toEntity(post, member);
        Offer savedOffer = offerRepository.save(offer);
        // 4. 업데이트된 사항 실시간 전송
        sendAboutOfferUpdate(post, OFFER_CREATE, savedOffer);
        offer.setPost(post);
        return savedOffer.getOfferId();
    }

    // 견적 제시 댓글 수정
    @Transactional
    public void updateOffer(Long postId, Long offerId, OfferCreateDto offerCreateDto, Member member) {
        // 1. 해당 게시글이 마감 전인지 확인
        Post post = checkNotDonePost(postId);
        // 2. 기존 댓글 가져오기
        Offer offer = offerRepository.findByPost_PostIdAndOfferId(postId, offerId).orElseThrow(() -> new BusinessException(NOT_FOUND_OFFER));
        // 3. 수정 가능한지 검증
        if(!offer.getCenter().getMemberId().equals(member.getMemberId()))
            throw new BusinessException(AUTHOR_ONLY_ACCESS);
        if(offerCreateDto.getPrice() > offer.getPrice())
            throw new BusinessException(ONLY_LOWER_AMOUNT_ALLOWED);
        // 4. 댓글 수정하기
        offer.setPrice(offerCreateDto.getPrice());
        offer.setContents(offerCreateDto.getContents());
        offerRepository.save(offer);
        // 5. 업데이트된 사항 실시간 전송
        sendAboutOfferUpdate(post, OFFER_UPDATE, offer);
    }

    // 견적 제시 댓글 삭제
    @Transactional
    public void deleteOffer(Long postId, Long offerId, Member member) {
        // 1. 해당 게시글이 마감 전인지 확인
        Post post = checkNotDonePost(postId);
        // 2. 기존 댓글 가져오기
        Offer offer = offerRepository.findByPost_PostIdAndOfferId(postId, offerId).orElseThrow(() -> new BusinessException(NOT_FOUND_OFFER));
        // 3. 삭제 가능한지 검증
        if(!offer.getCenter().getMemberId().equals(member.getMemberId()))
            throw new BusinessException(AUTHOR_ONLY_ACCESS);
        // 4. 댓글 삭제
        offerRepository.delete(offer);
        // 5. 업데이트된 사항 실시간 전송
        sendAboutOfferUpdate(post, OFFER_DELETE, offer);
    }

    // 견적 제시 댓글 낙찰
    @Transactional
    public void selectOffer(Long postId, Long offerId, Member member) {
        // 1. 해당 게시글이 마감 전인지 확인
        Post post = checkNotDonePost(postId);
        // 3. 게시글 작성자의 접근인지 검증
        if(!member.getMemberId().equals(post.getMember().getMemberId()))
            throw new BusinessException(AUTHOR_ONLY_ACCESS);
        // 4. 낙찰을 희망하는 댓글 가져오기
        Offer offer = offerRepository.findByPost_PostIdAndOfferId(postId, offerId).orElseThrow(() -> new BusinessException(NOT_FOUND_OFFER));
        // 5. 댓글 낙찰, 게시글 마감 처리
        offer.setSelected();
        post.setDone();
        // 6. 서비스 센터들에게 낙찰 또는 경매 마감 메세지 보내기
        sendMessageAfterSelection(postId, post.getMember().getMemberId(), offer.getCenter().getMemberId());
        webSocketService.deletePostRoom(postId);
    }

    // 해당 게시글을 가져오고, 마감 전인지 판단
    private Post checkNotDonePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new BusinessException(NOT_FOUND_POST));
        if(post.isDone())
            throw new BusinessException(CLOSED_POST);
        return post;
    }

    public void sendAboutOfferUpdate(Post post, String requestType, Offer offer) {
        Object data = offer.getOfferId();
        if(!requestType.equals(OFFER_DELETE)) {
            Double score = reviewRepository.findAvgScoreByCenterId(offer.getCenter().getMemberId()).orElse(0.0);
            data = OfferDetailDto.fromEntity(offer, score);
        }
        // 게시글 작성자에게 데이터 보내기
        sendData2Writer(post.getPostId(), post.getMember().getMemberId(), requestType, data);
        // 그 외 화면을 보고 있는 유저들에게 평균 제시 가격 보내기
        sendAvgPrice2others(post.getPostId(), post.getMember().getMemberId());
    }

    public void sendData2Writer(Long postId, Long memberId, String requestType, Object data) {
        DataResponse<?> response = DataResponse.success(requestType, data);
        if(webSocketService.isInPostRoom(postId, memberId)) {
            webSocketService.sendData2Client(memberId, response);
        }
    }

    public void sendAvgPrice2others(Long postId, Long writerId) {
        // 평균 견적 가격 계산하기
        Double avgPrice = offerRepository.findAvgPriceByPostId(postId).orElse(0.0);
        AvgPriceDto avgPriceDto = new AvgPriceDto(Math.round( avgPrice * 10 ) / 10.0);
        // 해당 화면을 보고 있는 유저 명단 가져오기
        Set<Long> memberList = webSocketService.findAllMemberInPost(postId);
        if(memberList == null)
            return;
        memberList.forEach(memberId -> {
            if(!memberId.equals(writerId)) {
                webSocketService.sendData2Client(memberId, avgPriceDto);
            }
        });
    }

    // 낙찰 처리 후 서비스 센터들에게 낙찰 메세지, 경매 마감 메세지 전송
    private void sendMessageAfterSelection(Long postId, Long writerId, Long selectedMemberId) {
        DataResponse<Boolean> selectSuccess = DataResponse.success(SELECT_SUCCESS, true);
        DataResponse<Boolean> selectFail = DataResponse.success(SELECT_FAIL, false);
        DataResponse<Boolean> selectEnd = DataResponse.success(SELECT_END, false);
        // 낙찰된 센터에게 메세지 보내기
        webSocketService.sendData2Client(selectedMemberId, selectSuccess);
        // 현재 관전자들
        Set<Long> memberIdsInPost = webSocketService.findAllMemberInPost(postId);
        if(memberIdsInPost == null)
            return;
        memberIdsInPost.remove(writerId);
        memberIdsInPost.remove(selectedMemberId);
        // 경매에 참여한 서비스 센터들
        List<Long> participants = offerRepository.findCenterMemberIdsByPost_PostId(postId);
        memberIdsInPost.forEach(memberId -> {
            if(participants.contains(memberId)) {
                webSocketService.sendData2Client(memberId, selectFail);
            }
            else {
                webSocketService.sendData2Client(memberId, selectEnd);
            }
        });
    }
}
