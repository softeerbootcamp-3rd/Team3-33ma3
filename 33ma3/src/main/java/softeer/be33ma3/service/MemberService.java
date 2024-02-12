package softeer.be33ma3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import softeer.be33ma3.domain.Center;
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.dto.request.CenterSignUpDto;
import softeer.be33ma3.dto.request.LoginDto;
import softeer.be33ma3.dto.request.ClientSignUpDto;
import softeer.be33ma3.jwt.JwtService;
import softeer.be33ma3.jwt.JwtToken;
import softeer.be33ma3.repository.CenterRepository;
import softeer.be33ma3.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private static final int CLIENT_TYPE = 1;
    private static final int CENTER_TYPE = 2;

    private final MemberRepository memberRepository;
    private final CenterRepository centerRepository;
    private final JwtService jwtService;

    @Transactional
    public void clientSignUp(ClientSignUpDto clientSignUpDto) {
        if(memberRepository.findMemberByLoginId(clientSignUpDto.getLoginId()).isPresent()){//아이디가 이미 존재하는 경우
            throw new IllegalArgumentException("이미 존재하는 아이디");
        }

        Member member = Member.createMember(CLIENT_TYPE, clientSignUpDto.getLoginId(), clientSignUpDto.getPassword());
        memberRepository.save(member);
    }

    @Transactional
    public void centerSignUp(CenterSignUpDto centerSignUpDto) {
        if(memberRepository.findMemberByLoginId(centerSignUpDto.getLoginId()).isPresent()){
            throw new IllegalArgumentException("이미 존재하는 아이디");
        }

        Member member = Member.createMember(CENTER_TYPE, centerSignUpDto.getLoginId(), centerSignUpDto.getPassword());
        member = memberRepository.save(member);

        Center center = Center.createCenter(centerSignUpDto.getCenterName(), centerSignUpDto.getLatitude(), centerSignUpDto.getLongitude(), member);
        centerRepository.save(center);
    }

    @Transactional
    public JwtToken login(LoginDto loginDto) {
        Member member = memberRepository.findByLoginIdAndPassword(loginDto.getLoginId(), loginDto.getPassword())
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않음"));

        JwtToken jwtToken = jwtService.getJwtToken(member.getMemberType(), member.getMemberId(), loginDto.getLoginId());
        member.setRefreshToken(jwtToken.getRefreshToken()); //리프레시 토큰 저장

        return jwtToken;
    }
}
