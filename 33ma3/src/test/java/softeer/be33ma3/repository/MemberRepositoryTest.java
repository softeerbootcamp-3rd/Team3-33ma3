//package softeer.be33ma3.repository;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import softeer.be33ma3.domain.Member;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//
//@ActiveProfiles("test")
//@SpringBootTest
//class MemberRepositoryTest {
//    @Autowired private MemberRepository memberRepository;
//
//    @AfterEach
//    void tearDown(){
//        memberRepository.deleteAllInBatch();
//    }
//
//    @DisplayName("로그인 아이디로 멤버를 찾을 수 있다.")
//    @Test
//    void findMemberByLoginId(){
//        //given
//        Member member = Member.createMember(1, "user1", "1234");
//        member = memberRepository.save(member);
//
//        //when
//        Member findMember = memberRepository.findMemberByLoginId(member.getLoginId()).get();
//
//        //then
//        assertThat(findMember)
//                .extracting("loginId", "password")
//                .containsExactly("user1", "1234");
//    }
//
//    @DisplayName("로그인 아이디와 비밀번호로 멤버를 찾을 수 있다.")
//    @Test
//    void findByLoginIdAndPassword(){
//        //given
//        Member member = Member.createMember(1, "user1", "1234");
//        member = memberRepository.save(member);
//
//        //when
//        Member findMember = memberRepository.findByLoginIdAndPassword("user1", "1234").get();
//
//        //then
//        assertThat(findMember)
//                .extracting("loginId", "password")
//                .containsExactly("user1", "1234");
//    }
//}
