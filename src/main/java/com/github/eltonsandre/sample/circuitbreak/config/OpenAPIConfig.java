package com.github.eltonsandre.sample.circuitbreak.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author eltonsandre
 * date 29/03/2020 17:07
 */
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OpenAPIConfig {

    BuildProperties buildProperties;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("basicScheme",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("basic")))
                .info(this.info());
    }

    private Info info() {
        return new Info()
                .title(buildProperties.getName())
                .description("Simple para teste de circuit break utilizando Resilience4J")
                .contact(new Contact().name("eltonsandre"))
                .version(buildProperties.getVersion())
                .license(new License()
                        .name("Apache 2.0")
                        .url("http://.org"));
    }

}
