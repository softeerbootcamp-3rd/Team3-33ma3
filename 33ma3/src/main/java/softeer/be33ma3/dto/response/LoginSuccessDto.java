package softeer.be33ma3.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.jwt.JwtToken;

@Data
@AllArgsConstructor
public class LoginSuccessDto {
    private Long memberId;
    private int memberType;
    private JwtToken jwtToken;

    public static LoginSuccessDto createLoginSuccessDto(Member member, JwtToken jwtToken) {
        return new LoginSuccessDto(member.getMemberId(), member.getMemberType(), jwtToken);
    }
}
