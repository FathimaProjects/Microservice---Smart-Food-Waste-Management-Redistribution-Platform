package com.rescuebite.order.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI orderServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("RescueBite - Order Management Service API")
                        .description("Microservice managing food reservation orders, collection codes, order statuses, and integration with Food Listing and Notification services.")
                        .version("1.0.0")
                        .contact(new Contact().name("RescueBite Team").email("team@rescuebite.com")));
    }

    @Bean
    public RestClient restClient() {
        return RestClient.create();
    }
}

