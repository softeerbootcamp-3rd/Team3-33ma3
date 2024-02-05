package softeer.be33ma3.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reviewId;

    private String contents;

    private double score;

    @OneToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
