package softeer.be33ma3.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import softeer.be33ma3.domain.Offer;

@Getter
@Schema(description = "견적 상세보기 응답 DTO")
public class OfferDetailDto {
    private Long offerId;
    private String centerName;
    private int price;
    private String contents;
    private boolean selected;

    @Builder
    private OfferDetailDto(Long offerId, String centerName, int price, String contents, boolean selected) {
        this.offerId = offerId;
        this.centerName = centerName;
        this.price = price;
        this.contents = contents;
        this.selected = selected;
    }

    // Offer Entity -> OfferDetailDto 변환
    public static OfferDetailDto fromEntity(Offer offer) {
        return OfferDetailDto.builder()
                .offerId(offer.getOfferId())
                .centerName(offer.getCenter().getCenterName())
                .price(offer.getPrice())
                .contents(offer.getContents())
                .selected(offer.isSelected()).build();
    }
}
