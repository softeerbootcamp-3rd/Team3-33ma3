package softeer.be33ma3.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JwtService {
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    @Transactional
    public JwtToken getJwtToken(int memberType, Long memberId, String loginId){
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));

        //토큰 생성
        JwtToken jwtToken = jwtProvider.createJwtToken(memberType, memberId, loginId);

        //refreshToken 저장
        member.setRefreshToken(jwtToken.getRefreshToken());

        return jwtToken;
    }
}
