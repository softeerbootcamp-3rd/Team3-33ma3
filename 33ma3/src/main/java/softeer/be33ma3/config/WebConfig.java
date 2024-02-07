package softeer.be33ma3.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import softeer.be33ma3.jwt.JwtService;
import softeer.be33ma3.jwt.intercepter.LoginInterceptor;
import softeer.be33ma3.jwt.intercepter.LoginSuccessInterceptor;
import softeer.be33ma3.service.MemberService;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final MemberService memberService;
    private final JwtService jwtService;

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/member/signUp");

        registry.addInterceptor(loginSuccessInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/member/signUp");
    }

    @Bean
    public LoginInterceptor loginInterceptor(){
        return new LoginInterceptor(memberService);
    }

    @Bean
    public LoginSuccessInterceptor loginSuccessInterceptor(){
        return new LoginSuccessInterceptor(jwtService);
    }
}
