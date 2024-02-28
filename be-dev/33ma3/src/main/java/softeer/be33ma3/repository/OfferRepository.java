package softeer.be33ma3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.domain.Offer;

import java.util.List;
import java.util.Optional;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    List<Offer> findByPost_PostId(Long postId);
    Optional<Offer> findByPost_PostIdAndCenter_MemberId(Long postId, Long centerId);
    Optional<Offer> findByPost_PostIdAndOfferId(Long postId, Long offerId);
    @Query("SELECT o.center FROM Offer o WHERE o.post.postId = :postId AND o.selected = true")
    Optional<Member> findSelectedCenterByPostId(@Param("postId") Long postId);
    @Query("SELECT AVG(o.price) FROM Offer o WHERE o.post.postId = :postId")
    Optional<Double> findAvgPriceByPostId(@Param("postId") Long postId);
    @Query("SELECT o.center.memberId FROM Offer o WHERE o.post.postId = :postId")
    List<Long> findCenterMemberIdsByPost_PostId(@Param("postId") Long postId);
}
