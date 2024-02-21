package softeer.be33ma3.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    private String contents;

    private double score;

    @OneToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "writer_id")
    private Member writer;

    @ManyToOne
    @JoinColumn(name = "center_id")
    private Member center;

    @Builder
    private Review(String contents, double score, Post post, Member writer, Member center) {
        this.contents = contents;
        this.score = score;
        this.post = post;
        this.writer = writer;
        this.center = center;
    }
}
