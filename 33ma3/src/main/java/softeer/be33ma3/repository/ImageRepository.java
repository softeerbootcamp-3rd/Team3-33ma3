package softeer.be33ma3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import softeer.be33ma3.domain.Image;
public interface ImageRepository extends JpaRepository<Image, Long> {
}
