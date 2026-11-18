package com.rescuebite.provider.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI foodProviderServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("RescueBite - Food Provider Management Service API")
                        .description("Microservice managing food provider registrations, business profiles, addresses, and verification statuses.")
                        .version("1.0.0")
                        .contact(new Contact().name("RescueBite Team").email("team@rescuebite.com")));
    }
}

