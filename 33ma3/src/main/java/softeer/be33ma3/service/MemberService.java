package softeer.be33ma3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.dto.request.MemberSignUpDto;
import softeer.be33ma3.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public void memberSignUp(MemberSignUpDto memberSignUpDto) {
        Member member = Member.createMember(memberSignUpDto);

        memberRepository.save(member);
    }
}
