package com.rescuebite.payment.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI paymentServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("RescueBite - Payment Management Service API")
                        .description("Microservice simulating transactions, managing payment audit records, and updating order status upon successful payment.")
                        .version("1.0.0")
                        .contact(new Contact().name("RescueBite Team").email("team@rescuebite.com")));
    }

    @Bean
    public RestClient restClient() {
        return RestClient.create();
    }
}

