package softeer.be33ma3.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long offerId;
  
    private int price;

    private String contents;

    private boolean selected;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "center_id")
    private Center center;

    public Offer(int price, String contents, Post post, Center center) {
        this.price = price;
        this.contents = contents;
        this.post = post;
        this.center = center;
    }
}
