package softeer.be33ma3.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;
import softeer.be33ma3.domain.Post;
import softeer.be33ma3.repository.post.PostCustomRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository {
    List<Post> findByDoneFalse();
}
