package com.example.Mypage.Config;

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

//        List<Tag> tagList = List.of(
//                createTag("기본 API", "기본임 ㅇㅇ 여기에다가 태그 여러개 붙여놓으셈"),
//                createTag("기본 2","기본이 두개 ~")
//                );
        return new OpenAPI()
                .addServersItem(new Server().url(url))
                .info(new Info()
                        .title("기본 API")
                        .version("1.0")
                        .description("api 명세서 한번 보쉴?"));
//                        ).tags(tagList);
    }

    private Tag createTag(String name, String description) {
        Tag tag = new Tag();
        tag.setName(name);
        tag.setDescription(description);
        return tag;
    }
}
