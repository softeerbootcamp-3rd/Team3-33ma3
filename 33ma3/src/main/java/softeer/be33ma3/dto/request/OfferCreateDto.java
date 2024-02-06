package softeer.be33ma3.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import softeer.be33ma3.domain.Center;
import softeer.be33ma3.domain.Offer;
import softeer.be33ma3.domain.Post;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "견적 작성 요청 DTO")
public class OfferCreateDto {
    @Min(value = 1, message = "제시 금액은 1만원 이상이어야 합니다.")
    @Max(value = 1000, message = "제시 금액은 1000만원 이하여야 합니다.")
    private int price;
    private String contents;

    public Offer toEntity(Post post, Center center) {
        return Offer.builder()
                .price(price)
                .contents(contents)
                .post(post)
                .center(center).build();
    }
}

