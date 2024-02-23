package softeer.be33ma3.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import softeer.be33ma3.domain.Offer;

@Getter
@Builder
@Schema(description = "견적 상세보기 응답 DTO")
public class OfferDetailDto implements Comparable<OfferDetailDto> {
    @Schema(description = "견적 아이디", example = "1")
    private Long offerId;

    @Schema(description = "견적을 작성한 센터의 멤버 아이디", example = "1")
    private Long memberId;

    @Schema(description = "센터 이름", example = "현대자동차 강남점")
    private String centerName;

    @Schema(description = "견적 제시 가격", example = "1")
    private int price;

    @Schema(description = "견적 내용", example = "사진상으로는 10만원에 가능할듯 합니다.")
    private String contents;

    @Schema(description = "낙찰 여부", example = "false")
    private boolean selected;

    @Schema(description = "해당 센터의 별점", example = "4.5")
    private Double score;

    @Schema(description = "해당 센터의 프로필 사진 링크", example = "profile.png")
    private String profile;

    // Offer Entity -> OfferDetailDto 변환
    public static OfferDetailDto fromEntity(Offer offer, Double score) {
        return OfferDetailDto.builder()
                .offerId(offer.getOfferId())
                .memberId(offer.getCenter().getMemberId())
                .centerName(offer.getCenter().getLoginId())
                .price(offer.getPrice())
                .contents(offer.getContents())
                .selected(offer.isSelected())
                .score(score)
                .profile(offer.getCenter().getImage().getLink()).build();
    }

    // 제시 가격 저렴한 순 -> 별점 높은 순 정렬
    @Override
    public int compareTo(OfferDetailDto other) {
        if(price != other.price)
            return price - other.price;
        if(score > other.getScore())
            return -1;
        else if(score < other.getScore())
            return 1;
        return 0;
    }
}
