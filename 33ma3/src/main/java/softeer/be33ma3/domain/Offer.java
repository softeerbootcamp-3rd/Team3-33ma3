package softeer.be33ma3.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long offerId;
    private int price;
    private String content;
    private boolean selected;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
