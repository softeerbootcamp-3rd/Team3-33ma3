package softeer.be33ma3.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi three3ma3Api(){
        return GroupedOpenApi.builder()
                .group("33MA3-API")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenAPI three3ma3OpenAPI() {
        return new OpenAPI()
                .info(new Info().title("THREE_THREE_MA_THREE API")
                        .description("수리수리마수리 API 명세서 입니다."));
    }
}
