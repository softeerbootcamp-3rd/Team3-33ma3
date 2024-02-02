package softeer.be33ma3.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Center {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long centerId;
    private String centerName;
    private double latitude;
    private double longitude;
    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
