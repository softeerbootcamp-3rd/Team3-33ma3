package softeer.be33ma3.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class CarCategory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long carCategoryId;
    private String carCategoryName;
}
