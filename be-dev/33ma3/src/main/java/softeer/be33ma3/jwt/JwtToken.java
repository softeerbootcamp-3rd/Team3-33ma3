package softeer.be33ma3.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtToken {
    private String accessToken; //엑세스 토큰 헤더 키
    private String refreshToken; //리프레시 토큰 헤더 키
}
