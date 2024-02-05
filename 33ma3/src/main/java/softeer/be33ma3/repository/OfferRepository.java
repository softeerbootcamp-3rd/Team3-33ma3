package softeer.be33ma3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import softeer.be33ma3.domain.Offer;

public interface OfferRepository extends JpaRepository<Offer, Long> {
}
