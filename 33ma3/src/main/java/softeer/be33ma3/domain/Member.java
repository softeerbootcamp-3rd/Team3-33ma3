package softeer.be33ma3.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import softeer.be33ma3.dto.request.MemberSignUpDto;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long memberId;

    private int memberType;     // 클라이언트: 1, 서비스 센터: 2

    private String loginId;

    private String password;

    private String refreshToken;

    public Member(int memberType, String loginId, String password) {
        this.memberType = memberType;
        this.loginId = loginId;
        this.password = password;
    }

    public static Member createMember(MemberSignUpDto memberSignUpDto){
        return new Member(memberSignUpDto.getMemberType(), memberSignUpDto.getLoginId(), memberSignUpDto.getPassword());
    }
}
