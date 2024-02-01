package softeer.be33ma3.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class CenterService {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long centerServiceId;
    private String centerServiceName;
    private String centerServiceType;
}
