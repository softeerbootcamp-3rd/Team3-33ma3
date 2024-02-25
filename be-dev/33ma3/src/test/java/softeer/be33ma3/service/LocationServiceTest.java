package softeer.be33ma3.service;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import softeer.be33ma3.domain.Center;
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.dto.response.CenterListDto;
import softeer.be33ma3.repository.CenterRepository;
import softeer.be33ma3.repository.MemberRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@ActiveProfiles("test")
@SpringBootTest
class LocationServiceTest {
    @Autowired private LocationService locationService;
    @Autowired private CenterRepository centerRepository;
    @Autowired private MemberRepository memberRepository;

    @BeforeEach
    void setUp(){
        Member member1 = new Member(2, "center1", "1234", null);
        Member member2 = new Member(2, "center2", "1234", null);

        memberRepository.saveAll(List.of(member1, member2));

        Center center1 = Center.createCenter(37.6, 127.0, member1);
        Center center2 = Center.createCenter(37.509, 127.0, member2);

        centerRepository.saveAll(List.of(center1, center2));
    }

    @AfterEach
    void tearDown(){
        centerRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("반경안에 있는 센터들을 반환할 수 있다.")
    @Test
    void getCentersInRadius(){
        //given
        double memberLatitude = 37.5;
        double memberLongitude = 127.0;
        double radius = 1.0;

        //when
        List<CenterListDto> centers = locationService.getCentersInRadius(memberLatitude, memberLongitude, radius);

        //then
        assertThat(centers).hasSize(1)
                .extracting("latitude", "longitude")
                .containsExactlyInAnyOrder(tuple(37.509, 127.0));
    }

    @DisplayName("저장된 모든 센터들을 반환한다.")
    @Test
    void getAllCenters(){
        //given //when
        List<CenterListDto> allCenters = locationService.getAllCenters();

        //then
        assertThat(allCenters).hasSize(2)
                .extracting("latitude", "longitude")
                .containsExactly(tuple(37.6, 127.0),
                        tuple(37.509, 127.0));
    }
}
