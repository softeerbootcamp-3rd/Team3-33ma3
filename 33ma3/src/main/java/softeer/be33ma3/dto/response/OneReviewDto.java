package softeer.be33ma3.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import softeer.be33ma3.domain.Review;

import java.util.List;
import static softeer.be33ma3.utils.StringParser.stringCommaParsing;

@Data
@NoArgsConstructor
@Schema(description = "리뷰")
public class OneReviewDto {
    @Schema(description = "리뷰 아이디", example = "1")
    private Long reviewId;
    @Schema(description = "작성자 아이디", example = "1")
    private Long writerId;
    @Schema(description = "작성자 이름", example = "client1")
    private String writerName;
    @Schema(description = "작성자 프로필", example = "image.png")
    private String writerImage;
    @Schema(description = "리뷰 내용", example = "좋아요")
    private String contents;    //리뷰 내용
    @Schema(description = "별점", example = "4.5")
    private double score;   //별점
    @Schema(description = "수리 서비스 리스트", example = "[깨짐, 기스]")
    private List<String> repairList;
    @Schema(description = "정비 서비스 리스트", example = "[타이어 교체, 오일 교체]")
    private List<String> tuneUpList;

    public static OneReviewDto create(Review review) {
        OneReviewDto oneReviewDto = new OneReviewDto();
        oneReviewDto.reviewId = review.getReviewId();
        oneReviewDto.writerId = review.getWriter().getMemberId();
        oneReviewDto.writerName = review.getWriter().getLoginId();
        oneReviewDto.writerImage = review.getWriter().getImage().getLink();
        oneReviewDto.contents = review.getContents();
        oneReviewDto.score = review.getScore();
        oneReviewDto.repairList = stringCommaParsing(review.getPost().getRepairService());
        oneReviewDto.tuneUpList = stringCommaParsing(review.getPost().getTuneUpService());

        return oneReviewDto;
    }
}
