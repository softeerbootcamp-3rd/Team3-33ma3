package softeer.be33ma3.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "게시글과 평균가 DTO")
public class PostWithAvgPriceDto {
    @Schema(description = "게시글 세부사항")
    private PostDetailDto postDetail;
    @Schema(description = "평균 제시 가격", example = "10.5")
    private double avgPrice;
    @Schema(description = "작성한 댓글 세부사항")
    private OfferDetailDto offerDetail;      // 견적을 작성한 이력이 있는 서비스 센터의 경우 작성한 댓글 정보 보내주기

    public PostWithAvgPriceDto(PostDetailDto postDetailDto, double avgPrice) {
        this.postDetail = postDetailDto;
        this.avgPrice = avgPrice;
    }

    public void setOfferDetailDto(OfferDetailDto offerDetailDto) {
        this.offerDetail = offerDetailDto;
    }
}
