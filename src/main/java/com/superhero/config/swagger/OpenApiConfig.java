package com.superhero.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI usersMicroserviceOpenAPI() {
        Contact contact = createContact();
        return new OpenAPI()
            .info(new Info().title("Super Hero API")
                .description("This is a CRUD application secured with JWT token")
                .contact(contact)
                .version("1.0"))
            .components(new Components()
                .addSecuritySchemes("bearerAuth", new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .in(SecurityScheme.In.HEADER)
                    .name("Authorization")))
            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }


    private Contact createContact() {
        Contact contact = new Contact();
        contact.setName("Julian Parodi");
        contact.setEmail("julianparodi19@gmail.com");
        contact.setUrl("https://github.com/Juliparodi/");
        return contact;
    }




}






