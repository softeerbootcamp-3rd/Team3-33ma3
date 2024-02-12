package softeer.be33ma3.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.dto.request.CenterSignUpDto;
import softeer.be33ma3.dto.request.ClientSignUpDto;
import softeer.be33ma3.dto.request.LoginDto;
import softeer.be33ma3.repository.CenterRepository;
import softeer.be33ma3.repository.MemberRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class MemberServiceTest {
    @Autowired private MemberRepository memberRepository;
    @Autowired private MemberService memberService;
    @Autowired private CenterRepository centerRepository;

    @AfterEach
    void tearDown(){
        centerRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("일반 사용자 회원가입")
    @Test
    void clientSignUp(){
        //given
        ClientSignUpDto client = new ClientSignUpDto("client1", "1234");

        // when
        memberService.clientSignUp(client);

        //then
        Member signUpClient = memberRepository.findByLoginIdAndPassword("client1", "1234").get();
        assertThat(signUpClient)
                .extracting("loginId", "password")
                .containsExactly("client1", "1234");
    }

    @DisplayName("동일한 아이디로 회원가입 하는 경우 예외가 발생한다.")
    @Test
    void clientSignUpWithSameId(){
        //given
        Member client1 = Member.createMember(1, "client1", "1234");
        memberRepository.save(client1);

        ClientSignUpDto client2 = new ClientSignUpDto("client1", "1234");

        //when //then
        assertThatThrownBy(() -> memberService.clientSignUp(client2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 아이디");
    }

    @DisplayName("센터 회원가입")
    @Test
    void centerSignUp(){
        //given
        CenterSignUpDto center = new CenterSignUpDto("center", "1234", "테스트 센터 강남점" ,37.1234, 127.1234);

        //when
        memberService.centerSignUp(center);

        //then
        Member signUpCenter = memberRepository.findByLoginIdAndPassword("center", "1234").get();
        assertThat(signUpCenter)
                .extracting("loginId", "password")
                .containsExactly("center", "1234");
    }

    @DisplayName("센터와 일반 사용자 로그인 기능")
    @Test
    void login(){
        //given
        Member client1 = Member.createMember(1, "client1", "1234");
        memberRepository.save(client1);

        LoginDto loginDto = new LoginDto("client1", "1234");

        //when
        memberService.login(loginDto);

        //then
        Member member = memberRepository.findByLoginIdAndPassword("client1", "1234").get();
        assertThat(member.getRefreshToken()).isNotNull();
    }

    @DisplayName("아이디 또는 비밀번호가 다르면 예외가 발생한다.")
    @Test
    void loginWithWrongId(){
        //given
        Member client1 = Member.createMember(1, "client1", "1234");
        memberRepository.save(client1);

        LoginDto loginDto = new LoginDto("client2", "1234");

        //when //then
        assertThatThrownBy(() -> memberService.login(loginDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("아이디 또는 비밀번호가 일치하지 않음");
    }
}
