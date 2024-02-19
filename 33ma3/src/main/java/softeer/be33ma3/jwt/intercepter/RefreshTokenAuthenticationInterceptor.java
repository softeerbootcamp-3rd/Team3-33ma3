package softeer.be33ma3.jwt.intercepter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.exception.BusinessException;
import softeer.be33ma3.jwt.JwtProvider;
import softeer.be33ma3.repository.MemberRepository;


import static softeer.be33ma3.exception.ErrorCode.JWT_NOT_VALID;
import static softeer.be33ma3.exception.ErrorCode.REFRESH_TOKEN_REQUIRED;
import static softeer.be33ma3.jwt.JwtProperties.REFRESH_HEADER_STRING;

@RequiredArgsConstructor
public class RefreshTokenAuthenticationInterceptor implements HandlerInterceptor {
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String refreshToken = getToken(request);

        if(jwtProvider.validationToken(refreshToken)){  //유효한 리프레시 토큰인 경우
            Claims claims = jwtProvider.getClaims(refreshToken);

            if (claims.get("memberId") == null) { //토큰에 memberId가 없는 경우
                throw new BusinessException(JWT_NOT_VALID);
            }

            Long memberId = Long.valueOf(claims.get("memberId").toString());
            Member member = memberRepository.findMemberByRefreshToken(refreshToken).get();

            return memberId.equals(member.getMemberId()); //리프레시 토큰에서 꺼낸 멤버 아이디와, 리프레시 토큰으로 찾은 멤버가 같은지 확인
        }

        return false;
    }

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(REFRESH_HEADER_STRING);
        if (!StringUtils.hasText(token)) {
            throw new BusinessException(REFRESH_TOKEN_REQUIRED);
        }

        return token;
    }
}
