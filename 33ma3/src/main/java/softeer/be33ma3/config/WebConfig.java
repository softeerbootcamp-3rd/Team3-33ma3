package softeer.be33ma3.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import softeer.be33ma3.jwt.CurrentUserArgumentResolver;
import softeer.be33ma3.jwt.JwtProvider;
import softeer.be33ma3.jwt.intercepter.JwtAuthenticationInterceptor;
import softeer.be33ma3.jwt.intercepter.RefreshTokenAuthenticationInterceptor;
import softeer.be33ma3.repository.MemberRepository;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(refreshTokenAuthenticationInterceptor())
                .order(1)
                .addPathPatterns("/reissueToken");

        registry.addInterceptor(jwtAuthenticationInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/client/signUp", "/center/signUp", "/center/all", "/login", "/location", "/center/all", "/reissue", "/post", "/post/one/*", "/review", "/review/**")
                .excludePathPatterns("/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new CurrentUserArgumentResolver(jwtProvider, memberRepository));
    }

    @Bean
    public RefreshTokenAuthenticationInterceptor refreshTokenAuthenticationInterceptor(){
        return new RefreshTokenAuthenticationInterceptor(jwtProvider, memberRepository);
    }

    @Bean
    public JwtAuthenticationInterceptor jwtAuthenticationInterceptor(){
        return new JwtAuthenticationInterceptor(jwtProvider);
    }
}
