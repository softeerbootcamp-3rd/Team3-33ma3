package softeer.be33ma3.domain;

import jakarta.persistence.*;
import lombok.Getter;
import softeer.be33ma3.domain.calcuator.DistanceCalculator;

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

    //반경안에 있는 센터인지 확인하는 메소드
    public boolean isWithinRadius(DistanceCalculator distanceCalculator, double memberLatitude, double memberLongitude, double radius) {
        double distance = distanceCalculator.calculate(memberLatitude, memberLongitude, latitude, longitude);
        return distance <= radius;
    }
}
