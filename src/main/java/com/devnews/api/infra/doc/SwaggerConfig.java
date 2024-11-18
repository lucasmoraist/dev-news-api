package com.devnews.api.infra.doc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI documentation() {
        return new OpenAPI()
                .info(this.info())
                .servers(List.of(this.server()))
                .components(components())
                .addSecurityItem(new SecurityRequirement().addList("bearer"));
    }

    private Info info() {
        return new Info()
                .title("devnews")
                .description("""
                        Este é um projeto de uma API de blog para desenvolvedores. Criado com Spring Boot,
                        Flyway Migration, Spring Data JPA, MySQL, Swagger, Spring Security e JWT.
                        """)
                .summary("API de blog para desenvolvedores")
                .version("1.0")
                .contact(this.contact());
    }

    private Contact contact() {
        return new Contact()
                .name("Lucas de Morais Nascimento Taguchi")
                .email("luksmnt1101@gmail.com")
                .url("https://www.linkedin.com/in/lucas-morais-152672219/");
    }

    private Server server() {
        return new Server()
                .url("http://localhost:8080")
                .description("Servidor local");
    }

    private Components components(){
        return new Components()
                .addSecuritySchemes("bearerAuth",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                );
    }
}
