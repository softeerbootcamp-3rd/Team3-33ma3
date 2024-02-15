package softeer.be33ma3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import softeer.be33ma3.domain.Alert;

public interface AlertRepository extends JpaRepository<Alert, Long> {
}
