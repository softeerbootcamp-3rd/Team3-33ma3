package softeer.be33ma3.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softeer.be33ma3.dto.response.AvgPriceDto;
import softeer.be33ma3.exception.BusinessException;
import softeer.be33ma3.repository.review.ReviewRepository;
import softeer.be33ma3.websocket.WebSocketHandler;
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.domain.Offer;
import softeer.be33ma3.domain.Post;
import softeer.be33ma3.dto.request.OfferCreateDto;
import softeer.be33ma3.dto.response.OfferDetailDto;
import softeer.be33ma3.repository.OfferRepository;
import softeer.be33ma3.repository.post.PostRepository;
import softeer.be33ma3.response.DataResponse;

import java.util.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static softeer.be33ma3.exception.ErrorCode.*;
import static softeer.be33ma3.service.MemberService.CENTER_TYPE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class OfferService {

    private final OfferRepository offerRepository;
    private final PostRepository postRepository;
    private final ReviewRepository reviewRepository;
    private final WebSocketHandler webSocketHandler;

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
        if(member.getMemberType() != CENTER_TYPE) {
            throw new BusinessException(NOT_CENTER);
        }
        // 2. 이미 견적을 작성한 센터인지 검증
        offerRepository.findByPost_PostIdAndCenter_MemberId(postId, member.getMemberId()).ifPresent(offer -> {throw new BusinessException(ALREADY_SUBMITTED);});
        // 3. 댓글 생성하여 저장하기
        Offer offer = offerCreateDto.toEntity(post, member);
        Offer savedOffer = offerRepository.save(offer);
        return savedOffer.getOfferId();
    }

    // 견적 제시 댓글 수정
    @Transactional
    public void updateOffer(Long postId, Long offerId, OfferCreateDto offerCreateDto, Member member) {
        // 1. 해당 게시글이 마감 전인지 확인
        checkNotDonePost(postId);
        // 2. 기존 댓글 가져오기
        Offer offer = offerRepository.findByPost_PostIdAndOfferId(postId, offerId).orElseThrow(() -> new BusinessException(NOT_FOUND_OFFER));
        // 3. 수정 가능한지 검증
        if(!offer.getCenter().equals(member))
            throw new BusinessException(AUTHOR_ONLY_ACCESS);
        if(offerCreateDto.getPrice() > offer.getPrice())
            throw new BusinessException(ONLY_LOWER_AMOUNT_ALLOWED);
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
        // 2. 기존 댓글 가져오기
        Offer offer = offerRepository.findByPost_PostIdAndOfferId(postId, offerId).orElseThrow(() -> new BusinessException(NOT_FOUND_OFFER));
        // 3. 삭제 가능한지 검증
        if(!offer.getCenter().equals(member))
            throw new BusinessException(AUTHOR_ONLY_ACCESS);
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
            throw new BusinessException(AUTHOR_ONLY_ACCESS);
        // 4. 낙찰을 희망하는 댓글 가져오기
        Offer offer = offerRepository.findByPost_PostIdAndOfferId(postId, offerId).orElseThrow(() -> new BusinessException(NOT_FOUND_OFFER));
        // 5. 댓글 낙찰, 게시글 마감 처리
        offer.setSelected();
        post.setDone();
        // 6. 서비스 센터들에게 낙찰 또는 경매 마감 메세지 보내기
        Long selectedMemberId = offer.getCenter().getMemberId();
        sendMessageAfterSelection(postId, selectedMemberId);
        webSocketHandler.deletePostRoom(postId);
    }

    // 해당 게시글을 가져오고, 마감 전인지 판단
    private Post checkNotDonePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new BusinessException(NOT_FOUND_POST));
        if(post.isDone())
            throw new BusinessException(CLOSED_POST);
        return post;
    }

    // 해당 견적을 작성한 서비스 센터들의 member id 목록 반환
    private List<Long> findMemberIdsWithOfferList(List<Offer> offerList) {
        return offerList.stream()
                .map(offer -> offer.getCenter().getMemberId())
                .toList();
    }

    public void sendAboutOfferUpdate(Long postId) {
        // 1. 해당 게시글 가져오기
        Post post = postRepository.findById(postId).orElseThrow(() -> new BusinessException(NOT_FOUND_POST));
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
        List<OfferDetailDto> offerDetailDtos = new ArrayList<>(
                offerList.stream().map(offer -> {
                    Double score = reviewRepository.findAvgScoreByCenterId(offer.getCenter().getMemberId()).orElse(0.0);
                    return OfferDetailDto.fromEntity(offer, score);
                }).toList());
        Collections.sort(offerDetailDtos);
        // 2. 해당 게시글 화면에 진입해 있을 경우 전송하기
        if(webSocketHandler.isInPostRoom(postId, memberId)) {
            log.info("게시글 작성자에게 실시간 댓글 전송 진행");
            webSocketHandler.sendData2Client(memberId, offerDetailDtos);
        }
    }

    // 해당 게시글 화면을 보고 있는 모든 유저들에게 평균 견적 제시 가격 실시간 전송
    public void sendAvgPrice2Others(Long postId, Long writerId) {
        // 1. 해당 게시글을 보고 있는 모든 유저들 가져오기 (글 작성자도 포함되어있을 경우 제외시키기)
        Set<Long> memberList = webSocketHandler.findAllMemberInPost(postId);
        if(memberList == null) {
            log.info("해당 게시글을 보고 있는 유저가 없습니다.");
            return;
        }
        memberList = memberList.stream()
                .filter(memberId -> !memberId.equals(writerId))
                .collect(Collectors.toSet());
        // 2. 평균 견적 가격 계산하기
        Double avgPrice = offerRepository.findAvgPriceByPostId(postId).orElse(0.0);
        AvgPriceDto avgPriceDto = new AvgPriceDto(Math.round( avgPrice * 10 ) / 10.0);
        // 3. 전송하기
        memberList.forEach(memberId -> webSocketHandler.sendData2Client(memberId, avgPriceDto));
    }

    // 낙찰 처리 후 서비스 센터들에게 낙찰 메세지, 경매 마감 메세지 전송
    private void sendMessageAfterSelection(Long postId, Long selectedMemberId) {
        // 낙찰 메세지
        DataResponse<Boolean> selectAlert = DataResponse.success("제시한 견적이 낙찰되었습니다.", true);
        webSocketHandler.sendData2Client(selectedMemberId, selectAlert);
        // 경매 마감 메세지
        DataResponse<Boolean> endAlert = DataResponse.success("견적 미선정으로 경매가 마감되었습니다. 다음 기회를 노려보세요!", false);
        List<Long> memberIdsInPost = offerRepository.findCenterMemberIdsByPost_PostId(postId);
        memberIdsInPost.stream()
                .filter(memberId -> !memberId.equals(selectedMemberId))
                .forEach(memberId -> webSocketHandler.sendData2Client(memberId, endAlert));
    }
}
