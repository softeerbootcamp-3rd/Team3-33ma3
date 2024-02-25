package softeer.be33ma3.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import softeer.be33ma3.domain.calcuator.DistanceCalculator;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class CenterTest {
    @Autowired
    DistanceCalculator distanceCalculator;

    @DisplayName("반경 안 거리이면 true를 반환한다.")
    @Test
    void isWithinRadius(){
        //given
        Center center = Center.createCenter(37.5, 127.0, null);
        double memberLatitude = 37.509; //0.9차이가 1km 차이
        double memberLongitude = 127.0;
        double radius = 1.0;

        //when
        boolean withinRadius = center.isWithinRadius(distanceCalculator, memberLatitude, memberLongitude, radius);

        //then
        assertThat(withinRadius).isTrue();
    }
}
