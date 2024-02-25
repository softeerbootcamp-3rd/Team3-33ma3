package softeer.be33ma3.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import softeer.be33ma3.domain.calcuator.DistanceCalculator;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Center {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long centerId;

    private double latitude;

    private double longitude;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static Center createCenter(double latitude, double longitude, Member member){
        Center center = new Center();
        center.latitude = latitude;
        center.longitude = longitude;
        center.member = member;
        return center;
    }

    //반경안에 있는 센터인지 확인하는 메소드
    public boolean isWithinRadius(DistanceCalculator distanceCalculator, double memberLatitude, double memberLongitude, double radius) {
        double distance = distanceCalculator.calculate(memberLatitude, memberLongitude, latitude, longitude);
        return distance <= radius;
    }
}
