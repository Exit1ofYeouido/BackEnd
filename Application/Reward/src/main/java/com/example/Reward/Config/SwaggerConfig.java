package com.example.Reward.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@SecurityScheme(
//        name = "bearerAuth",
//        type = SecuritySchemeType.HTTP,
//        bearerFormat = "JWT",
//        scheme = "bearer"
//)
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI(@Value("${service-url}") String url) {


        return new OpenAPI()
                .addServersItem(new Server().url(url))
                .info(new Info()
                        .title("Reward API")
                        .version("1.0")
                        .description("광고 리워드,영수증 리워드,출석체크 리워드 API입니다."));

    }

}
