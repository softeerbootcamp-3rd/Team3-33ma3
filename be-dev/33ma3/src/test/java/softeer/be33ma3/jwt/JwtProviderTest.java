package softeer.be33ma3.jwt;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class JwtProviderTest {
    @Autowired private JwtProvider jwtProvider;

    @DisplayName("member 정보로 jwt token을 생성한다.")
    @Test
    void createJwtToken(){
        //given
        int memberType = 1;
        Long memberId = 1L;
        String loginId = "member1";

        //when //then
        assertThat(jwtProvider.createJwtToken(memberType, memberId, loginId)).isInstanceOf(JwtToken.class);
    }

    @DisplayName("jwt의 claims를 가져올 수 있다.")
    @Test
    void getClaims(){
        //given
        String accessToken = jwtProvider.createAccessToken(1, 1L, "1234");

        //when
        Claims claims = jwtProvider.getClaims(accessToken);

        //then
        assertThat(Long.valueOf(claims.get("memberId").toString())).isEqualTo(1L);
    }
}
