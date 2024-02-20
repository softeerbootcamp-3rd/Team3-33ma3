package softeer.be33ma3.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShowReviewDto {
    private Long memberId;
    private double avg;   //별점
    private Long count;    //리뷰 개수
    private String loginId;
//    private String link;
}
