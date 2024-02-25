package softeer.be33ma3.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import softeer.be33ma3.domain.Member;

import java.util.List;

@Data
@NoArgsConstructor
@Schema(description = "특정 센터에 대한 리뷰")
public class ShowCenterReviewsDto {
    @Schema(description = "센터 이름", example = "현대자동차 강남점")
    private String centerName;
    @Schema(description = "센터 프로필 이미지", example = "image.png")
    private String centerImage;
    @Schema(description = "별점 평균", example = "4.5")
    private Double scoreAvg;
    private List<OneReviewDto> reviews;

    public static ShowCenterReviewsDto create(Member center,  double scoreAvg, List<OneReviewDto> reviews) {
        ShowCenterReviewsDto showCenterReviewsDto = new ShowCenterReviewsDto();
        showCenterReviewsDto.centerName = center.getLoginId();
        showCenterReviewsDto.centerImage = center.getImage().getLink();
        showCenterReviewsDto.scoreAvg = scoreAvg;
        showCenterReviewsDto.reviews = reviews;

        return showCenterReviewsDto;
    }
}
