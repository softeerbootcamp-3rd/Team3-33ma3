package softeer.be33ma3.domain.calcuator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class DistanceCalculatorTest {
    @Autowired private DistanceCalculator distanceCalculator;

    @DisplayName("주어진 두 지점의 위도 경도로 거리를 계산할 수 있다.")
    @Test
    void calculate(){
        //given
        double toLatitude = 37.5;
        double toLongitude = 127.0;
        double fromLatitude = 37.509;
        double fromLongitude = 127.0;

        //when
        double distance = distanceCalculator.calculate(toLatitude, toLongitude, fromLatitude, fromLongitude);

        //then
        assertThat(distance).isEqualTo(1.0);
    }
}
