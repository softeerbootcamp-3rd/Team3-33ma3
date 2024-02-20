package softeer.be33ma3.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import softeer.be33ma3.domain.Member;

import java.util.List;

@Data
@NoArgsConstructor
public class ShowCenterReviewsDto {
    private String centerName;
    private String link;
    private double scoreAvg;
    private List<OneReviewDto> reviews;

    public static ShowCenterReviewsDto create(Member center,  double scoreAvg, List<OneReviewDto> reviews) {
        ShowCenterReviewsDto showCenterReviewsDto = new ShowCenterReviewsDto();
        showCenterReviewsDto.centerName = center.getLoginId();
        showCenterReviewsDto.link = center.getImage().getLink();
        showCenterReviewsDto.scoreAvg = scoreAvg;
        showCenterReviewsDto.reviews = reviews;

        return showCenterReviewsDto;
    }
}
