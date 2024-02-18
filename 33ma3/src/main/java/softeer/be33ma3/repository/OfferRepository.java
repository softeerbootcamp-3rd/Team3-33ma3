package softeer.be33ma3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import softeer.be33ma3.domain.Offer;

import java.util.List;
import java.util.Optional;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    List<Offer> findByPost_PostId(Long postId);
    Optional<Offer> findByPost_PostIdAndCenter_CenterId(Long postId, long centerId);
    Optional<Offer> findByPost_PostIdAndOfferId(Long postId, Long offerId);
}
