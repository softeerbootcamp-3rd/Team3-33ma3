package softeer.be33ma3.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import softeer.be33ma3.jwt.JwtToken;

@Data
@AllArgsConstructor
public class LoginSuccessDto {
    private Long memberId;
    private JwtToken jwtToken;
}
