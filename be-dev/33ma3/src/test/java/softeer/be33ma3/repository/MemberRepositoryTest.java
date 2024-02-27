package softeer.be33ma3.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.jwt.JwtProvider;
import softeer.be33ma3.jwt.JwtToken;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("test")
@SpringBootTest
@Transactional
class MemberRepositoryTest {
    @Autowired private MemberRepository memberRepository;
    @Autowired private JwtProvider jwtProvider;

    @AfterEach
    void tearDown(){
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("로그인 아이디로 멤버를 찾을 수 있다.")
    @Test
    void findMemberByLoginId(){
        //given
        Member member = Member.createClient("user1", "1234", null);
        Member savedMember = memberRepository.save(member);

        //when
        Member findMember = memberRepository.findMemberByLoginId(savedMember.getLoginId()).get();

        //then
        assertThat(findMember)
                .extracting("loginId", "password")
                .containsExactly("user1", "1234");
    }

    @DisplayName("로그인 아이디와 비밀번호로 멤버를 찾을 수 있다.")
    @Test
    void findByLoginIdAndPassword(){
        //given
        Member member = Member.createClient("user1", "1234", null);
        Member savedMember = memberRepository.save(member);

        //when
        Member findMember = memberRepository.findByLoginIdAndPassword(savedMember.getLoginId(), savedMember.getPassword()).get();

        //then
        assertThat(findMember)
                .extracting("loginId", "password")
                .containsExactly("user1", "1234");
    }

    @DisplayName("리프레시 토큰으로 회원을 찾을 수 있다.")
    @Test
    void findMemberByRefreshToken(){
        //given
        Member client = Member.createClient("client1", "1234", null);
        Member savedMember = memberRepository.save(client);
        JwtToken jwtToken = jwtProvider.createJwtToken(savedMember.getMemberType(), savedMember.getMemberId(), savedMember.getLoginId());
        String refreshToken = jwtToken.getRefreshToken();
        savedMember.setRefreshToken(refreshToken);

        //when
        Member findMember = memberRepository.findMemberByRefreshToken(refreshToken).get();

        //then
        assertThat(findMember.getLoginId()).isEqualTo(savedMember.getLoginId());
    }
}
