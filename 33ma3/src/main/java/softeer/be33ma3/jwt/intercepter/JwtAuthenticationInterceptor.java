package softeer.be33ma3.jwt.intercepter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import softeer.be33ma3.exception.JwtTokenException;
import softeer.be33ma3.jwt.JwtProvider;

import static softeer.be33ma3.jwt.JwtProperties.ACCESS_HEADER_STRING;
import static softeer.be33ma3.jwt.JwtProperties.ACCESS_PREFIX_STRING;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationInterceptor implements HandlerInterceptor {
    private final JwtProvider jwtProvider;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessToken = getToken(request);

        if(accessToken == null){    //헤더에 토큰이 없는 경우
            throw new IllegalArgumentException("JWT 토큰 필요");
        }

        if(StringUtils.hasText(accessToken)){   //토큰이 있는 경우
            return jwtProvider.validationToken(accessToken);    //토큰 검증(마감기한, signature, jwt 형식인지)
        }

        return true;
    }

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(ACCESS_HEADER_STRING);
        if(!StringUtils.hasText(token)){
            throw new JwtTokenException("ACCESS TOKEN 필요");
        }

        if(token.startsWith(ACCESS_PREFIX_STRING)){
            return token.substring(7);
        }

        return null;
    }
}
