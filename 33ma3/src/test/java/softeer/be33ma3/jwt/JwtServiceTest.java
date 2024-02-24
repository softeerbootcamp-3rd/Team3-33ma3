package softeer.be33ma3.jwt;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.dto.request.LoginDto;
import softeer.be33ma3.dto.response.LoginSuccessDto;
import softeer.be33ma3.exception.BusinessException;
import softeer.be33ma3.exception.ErrorCode;
import softeer.be33ma3.repository.MemberRepository;
import softeer.be33ma3.service.MemberService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class JwtServiceTest {
    @Autowired private JwtService jwtService;
    @Autowired private MemberService memberService;
    @Autowired private MemberRepository memberRepository;

    @BeforeEach
    void setUp(){
        Member client1 = Member.createClient("client1", "1234", null);
        memberRepository.save(client1);
    }

    @AfterEach
    void tearDown(){
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("리프레시 토큰으로 엑세스 토큰을 재발급 받을 수 있다.")
    @Test
    void reissue(){
        //given
        LoginDto loginDto = new LoginDto("client1", "1234");
        LoginSuccessDto loginSuccessDto = memberService.login(loginDto);

        //when
        String reissue = jwtService.reissue(loginSuccessDto.getJwtToken().getRefreshToken());

        //then
        assertThat(reissue).contains("Bearer");  //액세스 토큰인지 확인
    }

    @DisplayName("리프레시 토큰으로 member를 찾을 수 없는 경우(올바르지 않은 리프레시 토큰) 예외가 발생한다.")
    @Test
    void reissueWithWrongRefreshToken(){
        //given
        LoginDto loginDto = new LoginDto("client1", "1234");
        memberService.login(loginDto);

        String refreshToken = "wrongwrong";

        //when //then
        assertThatThrownBy(() -> jwtService.reissue(refreshToken))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.REFRESH_TOKEN_NOT_VALID);
    }
}
