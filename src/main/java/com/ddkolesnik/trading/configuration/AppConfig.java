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

    @Value("${app.api.fias.base.url}")
    private String fiasApiBaseUrl;

    @Value("${app.api.fias.token}")
    private String appFiasApiToken;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(appApiBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("Token", appApiToken)
                .build();
    }

    @Bean
    public WebClient fiasWebClient() {
        return WebClient.builder()
                .baseUrl(fiasApiBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("Token", appFiasApiToken)
                .build();
    }

}
