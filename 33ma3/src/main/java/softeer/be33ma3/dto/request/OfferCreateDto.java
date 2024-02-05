package softeer.be33ma3.dto.request;

import lombok.Getter;
import softeer.be33ma3.domain.Center;
import softeer.be33ma3.domain.Offer;
import softeer.be33ma3.domain.Post;

@Getter
public class OfferCreateDto {
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
