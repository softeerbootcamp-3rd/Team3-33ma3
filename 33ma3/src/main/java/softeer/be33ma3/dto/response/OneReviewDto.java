package softeer.be33ma3.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import softeer.be33ma3.domain.Review;

import java.util.List;
import static softeer.be33ma3.utils.StringParser.stringCommaParsing;

@Data
@NoArgsConstructor
public class OneReviewDto {
    private String writerName;
    private String writerImage;
    private String contents;    //리뷰 내용
    private double score;   //별점
    private List<String> repairList;
    private List<String> tuneUpList;

    public static OneReviewDto create(Review review) {
        OneReviewDto oneReviewDto = new OneReviewDto();
        oneReviewDto.writerName = review.getWriter().getLoginId();
        oneReviewDto.writerImage = review.getWriter().getImage().getLink();
        oneReviewDto.contents = review.getContents();
        oneReviewDto.score = review.getScore();
        oneReviewDto.repairList = stringCommaParsing(review.getPost().getRepairService());
        oneReviewDto.tuneUpList = stringCommaParsing(review.getPost().getTuneUpService());

        return oneReviewDto;
    }
}
