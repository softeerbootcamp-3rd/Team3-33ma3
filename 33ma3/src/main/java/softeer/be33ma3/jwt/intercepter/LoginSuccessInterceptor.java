package softeer.be33ma3.jwt.intercepter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import softeer.be33ma3.jwt.AuthenticationMember;
import softeer.be33ma3.jwt.JwtService;
import softeer.be33ma3.jwt.JwtToken;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessInterceptor implements HandlerInterceptor {
    public static final String AUTHENTICATE_MEMBER = "AuthenticateMember";

    @Autowired private ObjectMapper objectMapper;
    private final JwtService jwtService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("loginSuccessInterceptor 들어옴");
        //1. 로그인 한 사용자 가져요기
        Object authenticateMember = request.getAttribute(AUTHENTICATE_MEMBER);

        if(authenticateMember instanceof AuthenticationMember authenticationMember){
            log.info("토큰 발급 받으러 들어옴");
            //2. 토큰 발급
            JwtToken jwtToken = jwtService.login(((AuthenticationMember) authenticateMember).getLoginId());

            //3. 응답 보내기
            String json = objectMapper.writeValueAsString(jwtToken.getAuthorization());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getOutputStream().write(json.getBytes());
        }

        return true;
    }
}
