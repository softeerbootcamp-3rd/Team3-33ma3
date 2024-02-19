package softeer.be33ma3.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import softeer.be33ma3.exception.BusinessException;

import java.security.Key;
import java.util.Date;

import static softeer.be33ma3.exception.ErrorCode.EXPIRED_TOKEN;
import static softeer.be33ma3.exception.ErrorCode.JWT_NOT_VALID;
import static softeer.be33ma3.jwt.JwtProperties.*;
@Slf4j
@Component //빈으로 등록
public class JwtProvider {  //jwt 토큰을 만들고 검증하는 역할
    private final Key key;

    public JwtProvider(@Value("${jwt.secret}") String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public JwtToken createJwtToken(int memberType, Long memberId, String longinId){
        //엑세스 토큰
        String accessToken = createAccessToken(memberType, memberId, longinId);

        //리프레시 토큰
        String refreshToken = createRefreshToken(memberId);

        return new JwtToken(accessToken, refreshToken);
    }

    public String createAccessToken(int memberType, Long memberId, String longinId) { //액세스 토큰 생성
        String accessToken = ACCESS_PREFIX_STRING + Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .claim("memberType", memberType)
                .claim("memberId", memberId)
                .claim("loginId", longinId)
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_TIME)) //만료시간 설정
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();

        return accessToken;
    }

    //토큰 유효성 검사
    public Boolean validationToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key)
                    .build().parseClaimsJws(token).getBody();
        }catch (SignatureException e ){ //signature 검증에 실패한 경우
            throw new BusinessException(JWT_NOT_VALID);
        }catch (ExpiredJwtException e) { //토큰이 만료된 경우
            throw new BusinessException(EXPIRED_TOKEN);
        } catch (MalformedJwtException e) { //jwt 형식에 맞지 않는 경우
            throw new BusinessException(JWT_NOT_VALID);
        }

        return true;
    }


    //JWT 토큰의 Claims 을 가져오는 메서드
    public Claims getClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token.replace(ACCESS_PREFIX_STRING, "")) // ACCESS_PREFIX_STRING 제거
                .getBody();
    }

    private String createRefreshToken(Long memberId) {  //리프레시 토큰 생성
        String refreshToken = Jwts.builder()
                .setSubject(String.valueOf(memberId) + "_refresh")
                .claim("memberId", memberId)
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_TIME))   //만료시간 설정
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();

        return refreshToken;
    }
}
