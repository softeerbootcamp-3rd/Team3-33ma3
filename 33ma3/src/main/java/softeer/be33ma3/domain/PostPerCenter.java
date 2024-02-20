package softeer.be33ma3.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostPerCenter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postPerCenterId;

    @ManyToOne
    @JoinColumn(name = "center_id")
    private Member center;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public PostPerCenter(Member center, Post post) {
        this.center = center;
        this.post = post;
    }
}
