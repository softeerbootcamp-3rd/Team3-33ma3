package softeer.be33ma3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import softeer.be33ma3.domain.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewCustomRepository {
    Optional<Review> findByPost_PostId(Long postId);

    @Query("SELECT AVG(r.score) FROM Review r WHERE r.center.memberId = :memberId")
    Optional<Double> findAvgScoreByCenterId(@Param("memberId") Long memberId);
    @Query("SELECT r FROM Review r WHERE r.center.memberId = :memberId ORDER BY r.score DESC, r.createTime DESC")
    List<Review> findReviewsByCenterIdOrderByScore(@Param("memberId") Long memberId);   //별점높은 순으로 정렬, 별점이 같으면 최신순으로 정렬
}
