package softeer.be33ma3.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import softeer.be33ma3.jwt.JwtProvider;
import softeer.be33ma3.jwt.intercepter.JwtAuthenticationInterceptor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final JwtProvider jwtProvider;

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthenticationInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/home", "/member/login", "/member/signUp", "/post/image");
    }

    @Bean
    public JwtAuthenticationInterceptor jwtAuthenticationInterceptor(){
        return new JwtAuthenticationInterceptor(jwtProvider);
    }
}
