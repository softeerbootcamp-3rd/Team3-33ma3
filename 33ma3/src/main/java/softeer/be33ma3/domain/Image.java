package softeer.be33ma3.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long imageId;

    private String link;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
