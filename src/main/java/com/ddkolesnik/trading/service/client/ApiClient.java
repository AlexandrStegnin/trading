package com.ddkolesnik.trading.service.client;

import com.ddkolesnik.trading.api.EgrnResponse;
import com.ddkolesnik.trading.api.RosreestrRequest;
import com.ddkolesnik.trading.api.RosreestrResponse;
import com.ddkolesnik.trading.model.dto.CadasterDTO;
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

    public ApiClient(WebClient webClient) {
        this.webClient = webClient;
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

    public List<CadasterDTO> convert(RosreestrResponse response) {
        return response.getObject().getAllObjects();
    }

}
