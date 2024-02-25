package softeer.be33ma3.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import softeer.be33ma3.domain.Center;
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.domain.Post;
import softeer.be33ma3.domain.Review;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "리뷰 작성 요청 DTO")
public class ReviewCreateDto {
    @Schema(description = "별점", example = "4.5")
    @Min(value = 0, message = "별점은 0점 이상이어야 합니다.")
    @Max(value = 5, message = "별점은 5점 이하여야 합니다.")
    private double score;
    @Schema(description = "센터 리뷰글", example = "센터 리뷰글입니다.")
    @NotBlank
    private String contents;

    public Review toEntity(Post post, Member writer, Member center) {
        return Review.builder()
                .contents(contents)
                .score(score)
                .post(post)
                .writer(writer)
                .center(center).build();
    }
}
