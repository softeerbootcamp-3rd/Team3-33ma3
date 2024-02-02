package softeer.be33ma3.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class PostPerCenter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long postPerCenterId;
    @ManyToOne
    @JoinColumn(name = "center_id")
    private Center center;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
