package com.rescuebite.notification.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI notificationServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("RescueBite - Notification Service API")
                        .description("Microservice managing user notifications, event alerts for orders, payments, donations, and inbox status.")
                        .version("1.0.0")
                        .contact(new Contact().name("RescueBite Team").email("team@rescuebite.com")));
    }
}

