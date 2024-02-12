package softeer.be33ma3.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.exception.JwtTokenException;
import softeer.be33ma3.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JwtService {
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    @Transactional
    public JwtToken getJwtToken(int memberType, Long memberId, String loginId){ //토큰 발급 -> 로그인 시
        //토큰 생성
        JwtToken jwtToken = jwtProvider.createJwtToken(memberType, memberId, loginId);

        return jwtToken;
    }

    @Transactional
    public String reissue(String refreshToken){   //리프레시 토큰으로 엑세스 토큰 재발급
        Member member = memberRepository.findMemberByRefreshToken(refreshToken).orElseThrow(() -> new JwtTokenException("올바르지 않은 리프레시 토큰"));

        //토큰 생성
        String accessToken = jwtProvider.createAccessToken(member.getMemberType(), member.getMemberId(), member.getPassword());

        return accessToken;
    }
}
