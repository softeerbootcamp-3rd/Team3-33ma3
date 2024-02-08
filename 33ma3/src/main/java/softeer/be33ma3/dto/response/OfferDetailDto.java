package softeer.be33ma3.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import softeer.be33ma3.domain.Offer;

@Getter
@Schema(description = "견적 상세보기 응답 DTO")
public class OfferDetailDto {
    @Schema(description = "견적 아이디", example = "1")
    private Long offerId;

    @Schema(description = "센터 이름", example = "현대자동차 강남점")
    private String centerName;

    @Schema(description = "멤버 아이디", example = "1")
    private int price;

    @Schema(description = "견적 내용", example = "사진상으로는 10만원에 가능할듯 합니다.")
    private String contents;

    @Schema(description = "낙찰 여부", example = "false")
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