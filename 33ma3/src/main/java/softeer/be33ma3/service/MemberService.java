package softeer.be33ma3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.dto.request.MemberLoginDto;
import softeer.be33ma3.dto.request.MemberSignUpDto;
import softeer.be33ma3.jwt.JwtService;
import softeer.be33ma3.jwt.JwtToken;
import softeer.be33ma3.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtService jwtService;

    @Transactional
    public void memberSignUp(MemberSignUpDto memberSignUpDto) {
        //TODO: 중복회원 확인하기
        Member member = Member.createMember(memberSignUpDto);

        memberRepository.save(member);
    }

    @Transactional
    public JwtToken login(MemberLoginDto memberLoginDto) {
        Member member = memberRepository.findMemberByLoginId(memberLoginDto.getLoginId())
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않음"));

        if(!member.getPassword().equals(memberLoginDto.getPassword())){
            throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않음");
        }

        JwtToken jwtToken = jwtService.getJwtToken(member.getMemberType(), member.getMemberId(), memberLoginDto.getLoginId());
        member.setRefreshToken(jwtToken.getRefreshToken()); //리프레시 토큰 저장

        return jwtToken;
    }
}
