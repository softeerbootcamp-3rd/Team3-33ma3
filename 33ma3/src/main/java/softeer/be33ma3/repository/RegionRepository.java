package softeer.be33ma3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import softeer.be33ma3.domain.Region;
import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Long>{
    Optional<Region> findByRegionName(String regionName);
}
