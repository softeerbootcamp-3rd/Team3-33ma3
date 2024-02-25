package softeer.be33ma3.domain.calcuator;

import org.springframework.stereotype.Component;

@Component
public class HaversineFormula implements DistanceCalculator {
    private static final double EARTH_RADIUS = 6371;    //지구의 반경 (단위: km)

    @Override
    public double calculate(double memberLatitude, double memberLongitude, double centerLatitude, double centerLongitude) {
        //두 지점의 위도 경도 차이를 라이안으로 변환
        double dLat = Math.toRadians(centerLatitude - memberLatitude);
        double dLon = Math.toRadians(centerLongitude - memberLongitude);
        //두 지점 사이의 거리 구하기(Haversine 공식)
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(memberLatitude)) * Math.cos(Math.toRadians(centerLatitude)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return Math.round(EARTH_RADIUS * c * 10) / 10.0 ;   //소수점 첫째자리 까지만
    }
}
