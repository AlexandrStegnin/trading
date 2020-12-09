package com.ddkolesnik.trading.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Alexandr Stegnin
 */

@Configuration
public class AppConfig {

    @Value("${app.api.token}")
    private String appApiToken;

    @Value("${app.api.base.url}")
    private String appApiBaseUrl;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(appApiBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("Token", appApiToken)
                .build();
    }

}
