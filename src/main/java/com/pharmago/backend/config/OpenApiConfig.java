package com.pharmago.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("PharmaGo API")
                        .description("API backend pour la localisation " +
                                "des pharmacies de garde à Lomé, Togo")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Edgard")
                                .email("contact@pharmago.tg")))

                // Déclaration du schéma de sécurité API Key
                .components(new Components()
                        .addSecuritySchemes("ApiKey",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .name("X-API-Key")))

                // Applique la sécurité globalement
                .addSecurityItem(
                        new SecurityRequirement().addList("ApiKey"));
    }
}