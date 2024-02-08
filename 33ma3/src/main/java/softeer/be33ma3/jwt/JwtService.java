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

        //기존 사용자인지 신규 사용자인지 확인 - 리프레시 토큰으로 확인

        //신규 사용자인 경우 - 새로 만들기
        member.setRefreshToken(jwtToken.getRefreshToken());

        return jwtToken;
    }
}
