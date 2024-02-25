package softeer.be33ma3.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.jwt.JwtToken;

@Data
@AllArgsConstructor
@Schema(description = "로그인 성공 응답 DTO")
public class LoginSuccessDto {
    @Schema(description = "멤버 아이디", example = "1")
    private Long memberId;
    @Schema(description = "유저 타입", example = "1")
    private int memberType;
    @Schema(description = "Jwt Token")
    private JwtToken jwtToken;

    public static LoginSuccessDto createLoginSuccessDto(Member member, JwtToken jwtToken) {
        return new LoginSuccessDto(member.getMemberId(), member.getMemberType(), jwtToken);
    }
}
