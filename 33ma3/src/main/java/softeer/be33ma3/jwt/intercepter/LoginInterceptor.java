package softeer.be33ma3.jwt.intercepter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import softeer.be33ma3.dto.request.MemberLoginDto;
import softeer.be33ma3.jwt.AuthenticationMember;
import softeer.be33ma3.service.MemberService;

@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {
    public static final String AUTHENTICATE_MEMBER = "AuthenticateMember";

    @Autowired private ObjectMapper objectMapper;
    private final MemberService memberService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getMethod().equals("POST")){
            MemberLoginDto memberLoginDto = objectMapper.readValue(request.getReader(), MemberLoginDto.class);

            if(!memberService.verifyMember(memberLoginDto)){ //정상적인 사용자가 아닌 경우
                return false;
            }

            request.setAttribute(AUTHENTICATE_MEMBER, new AuthenticationMember(memberLoginDto.getLoginId(), memberLoginDto.getPassword()));
        }

        System.out.println("처음 인터셉터는 들어옴");
        return true; //false 진행 X
    }
}
