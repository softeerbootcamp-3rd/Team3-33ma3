//package softeer.be33ma3.service;
//
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//import softeer.be33ma3.domain.Center;
//import softeer.be33ma3.domain.Member;
//import softeer.be33ma3.dto.response.CenterListDto;
//import softeer.be33ma3.repository.CenterRepository;
//import softeer.be33ma3.repository.MemberRepository;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.tuple;
//
//@ActiveProfiles("test")
//@SpringBootTest
//@Transactional
//class LocationServiceTest {
//    @Autowired private LocationService locationService;
//    @Autowired private CenterRepository centerRepository;
//    @Autowired private MemberRepository memberRepository;
//
//    @DisplayName("반경안에 있는 센터들을 반환할 수 있다.")
//    @Test
//    void getCenters(){
//        //given
//        Member member1 = new Member(2, "center1", "1234");
//        Member member2 = new Member(2, "center2", "1234");
//
//        memberRepository.saveAll(List.of(member1, member2));
//
//        Center center1 = new Center("테스트 센터점1", 37.6, 127.0, member1);
//        Center center2 = new Center("테스트 센터점2", 37.509, 127.0, member2);
//
//        centerRepository.saveAll(List.of(center1, center2));
//
//        double memberLatitude = 37.5;
//        double memberLongitude = 127.0;
//        double radius = 1.0;
//
//        //when
//        List<CenterListDto> centers = locationService.getCentersInRadius(memberLatitude, memberLongitude, radius);
//
//        //then
//        assertThat(centers).hasSize(1)
//                .extracting("centerId", "centerName", "latitude", "longitude")
//                .containsExactlyInAnyOrder(tuple(2L, "테스트 센터점2", 37.509, 127.0));
//    }
//}
