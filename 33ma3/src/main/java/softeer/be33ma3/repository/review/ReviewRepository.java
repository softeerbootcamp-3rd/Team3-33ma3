package softeer.be33ma3.repository.review;

import org.springframework.data.jpa.repository.JpaRepository;
import softeer.be33ma3.domain.Review;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByPost_PostId(Long postId);
}
