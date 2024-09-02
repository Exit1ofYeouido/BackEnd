package com.example.Home.Config;



import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI(@Value("${service-url}") String url) {

        return new OpenAPI()
                .addServersItem(new Server().url(url))
                .info(new Info()
                        .title("홈 API")
                        .version("1.0")
                        .description("api 명세서 한번 보쉴?"));
    }

}
