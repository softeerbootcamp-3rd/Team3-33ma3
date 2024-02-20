package softeer.be33ma3.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShowReviewDto {
    private Long centerId;
    private double scoreAvg;   //별점 평균
    private Long reviewCount;    //리뷰 개수
    private String centerName;
    private String link;
}
