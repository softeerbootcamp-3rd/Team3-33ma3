package softeer.be33ma3.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.exception.BusinessException;
import softeer.be33ma3.repository.MemberRepository;

import static softeer.be33ma3.exception.ErrorCode.NOT_FOUND_MEMBER;
import static softeer.be33ma3.jwt.JwtProperties.ACCESS_HEADER_STRING;

@Slf4j
@RequiredArgsConstructor
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("supportsParameter 실행");
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(CurrentUser.class);   //이 어노테이션이 붙어 있는지 확인
        boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType());    //타입이 Member 인지 확인

        return hasLoginAnnotation && hasMemberType; //true면 resolveArgument 실행
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        log.info("resolveArgument 실행");

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        String accessToken = request.getHeader(ACCESS_HEADER_STRING);   //헤더에서 엑세스 토큰 가져오기

        if (accessToken == null) {   //토큰이 없는 경우
            return null;
        }

        Claims claims = jwtProvider.getClaims(accessToken);
        Long memberId = Long.parseLong(claims.getSubject());

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new BusinessException(NOT_FOUND_MEMBER));

        return member;
    }
}
