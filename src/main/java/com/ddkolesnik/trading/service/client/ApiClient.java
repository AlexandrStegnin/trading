package com.ddkolesnik.trading.service.client;

import com.ddkolesnik.trading.api.EgrnResponse;
import com.ddkolesnik.trading.api.RosreestrRequest;
import com.ddkolesnik.trading.api.RosreestrResponse;
import com.ddkolesnik.trading.model.dto.CadasterDTO;
import com.ddkolesnik.trading.api.FiasResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Component
public class ApiClient {

    private final WebClient webClient;

    @Qualifier("fiasWebClient")
    private final WebClient fiasWebClient;

    public ApiClient(WebClient webClient, WebClient fiasWebClient) {
        this.webClient = webClient;
        this.fiasWebClient = fiasWebClient;
    }

    public Mono<RosreestrResponse> getRosreestrResponse(RosreestrRequest request) {
        return webClient.post()
                .uri("/search")
                .body(Mono.just(request), RosreestrRequest.class)
                .retrieve()
                .bodyToMono(RosreestrResponse.class);
    }

    public Mono<EgrnResponse> getCadasterDetails(RosreestrRequest request) {
        return webClient.post()
                .uri("/objectInfoFull")
                .body(Mono.just(request), RosreestrRequest.class)
                .retrieve()
                .bodyToMono(EgrnResponse.class);
    }

    public Mono<FiasResponse> getFiasResponse(String query) {
        String oneString = "1";
        String withParent = "0";
        String limit = "100";
        return fiasWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("query", query)
                        .queryParam("oneString", oneString)
                        .queryParam("withParent", withParent)
                        .queryParam("limit", limit)
                        .build())
                .retrieve()
                .bodyToMono(FiasResponse.class);
    }

    public List<CadasterDTO> convert(RosreestrResponse response) {
        return response.getObject().getAllObjects();
    }

}
