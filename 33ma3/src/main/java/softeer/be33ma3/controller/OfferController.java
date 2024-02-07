package softeer.be33ma3.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import softeer.be33ma3.dto.request.OfferCreateDto;
import softeer.be33ma3.dto.response.OfferDetailDto;
import softeer.be33ma3.response.DataResponse;
import softeer.be33ma3.response.SingleResponse;
import softeer.be33ma3.service.OfferService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class OfferController {

    private final OfferService offerService;

    @GetMapping("/{post_id}/offer/{offer_id}")
    public ResponseEntity<?> getOneOffer(@PathVariable("post_id") Long postId,
                                         @PathVariable("offer_id") Long offerId) {
        OfferDetailDto offerDetailDto = offerService.getOffer(postId, offerId);
        return ResponseEntity.ok(DataResponse.success("견적 불러오기 성공", offerDetailDto));
    }

    @PostMapping("/{post_id}/offer")
    public ResponseEntity<?> createOffer(@PathVariable("post_id") Long postId,
                                         @RequestBody @Valid OfferCreateDto offerCreateDto) {
        offerService.createOffer(postId, offerCreateDto);
        // 글 작성자에게 업데이트 된 댓글 리스트 보내기
        offerService.sendOfferList2Writer(postId);
        return ResponseEntity.ok(SingleResponse.success("입찰 성공"));
    }
}
