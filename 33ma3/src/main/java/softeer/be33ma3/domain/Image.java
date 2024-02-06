package softeer.be33ma3.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long imageId;

    private String link;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public void setPost(Post post){
        this.post = post;
        post.getImages().add(this);
    }

    public static Image createImage(String link){
        Image image = new Image();
        image.link = link;

        return image;
    }
}
