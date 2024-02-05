package softeer.be33ma3.controller;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import softeer.be33ma3.dto.response.OfferDetailDto;
import softeer.be33ma3.response.DataResponse;
import softeer.be33ma3.service.OfferService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class OfferController {

    private final OfferService offerService;

    @GetMapping("/{post_id}/offer/{offer_id}")
    public ResponseEntity<?> getOneOffer(@PathVariable("post_id") Long postId,
                                         @PathVariable("offer_id") Long offerId) {

        OfferDetailDto offerDetailDto = offerService.getOneOffer(postId, offerId);
        return ResponseEntity.ok(DataResponse.success("견적 불러오기 성공", offerDetailDto));
    }
}
