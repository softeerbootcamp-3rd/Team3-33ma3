package softeer.be33ma3.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long offerId;

    private int price;

    private String contents;

    private boolean selected;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id")
    private Member center;

    @Builder
    private Offer(int price, String contents, Post post, Member center) {
        this.price = price;
        this.contents = contents;
        this.post = post;
        this.center = center;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    public void setContents(String contents) {
        this.contents = contents;
    }
    public void setSelected() {
        selected = true;
    }

    public void setPost(Post post){
        this.post = post;
        post.getOffers().add(this);
    }
}
