package softeer.be33ma3.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "모든 리뷰")
public class ShowAllReviewDto {
    @Schema(description = "센터 아이디", example = "1")
    private Long centerId;
    @Schema(description = "별점 평균", example = "4.3")
    private Double scoreAvg;   //별점 평균
    @Schema(description = "리뷰 개수", example = "30")
    private Long reviewCount;    //리뷰 개수
    @Schema(description = "센터 이름", example = "보경점")
    private String centerName;
    @Schema(description = "센터 프로필", example = "image.png")
    private String centerImage;
}
