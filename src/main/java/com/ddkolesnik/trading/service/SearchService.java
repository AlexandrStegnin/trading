package com.ddkolesnik.trading.service;

import com.ddkolesnik.trading.configuration.security.SecurityUtils;
import com.ddkolesnik.trading.api.CadasterEntity;
import com.ddkolesnik.trading.api.EgrnResponse;
import com.ddkolesnik.trading.api.RosreestrRequest;
import com.ddkolesnik.trading.api.RosreestrResponse;
import com.ddkolesnik.trading.model.dto.CadasterDTO;
import com.ddkolesnik.trading.model.dto.EgrnDTO;
import com.ddkolesnik.trading.model.dto.EgrnDetailsDTO;
import com.ddkolesnik.trading.service.client.ApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Slf4j
@Service
public class SearchService {

    private final ApiClient client;

    private final WebClient webClient;

    private final CadasterService cadasterService;

    @Value("${app.api.token}")
    private String appApiToken;

    public SearchService(ApiClient client, CadasterService cadasterService) {
        this.client = client;
        this.cadasterService = cadasterService;
        webClient = WebClient.builder()
                .baseUrl("https://apirosreestr.ru/api/cadaster")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    /**
     * Метод поиска объекта по адресу
     *
     * @param address полный адрес
     */
    public boolean search(String address) {
        List<CadasterEntity> entities = cadasterService.findByTag(address);
        if (!entities.isEmpty()) {
            return false;
        }
        RosreestrRequest request = new RosreestrRequest("normal", address, 1);
        Mono<RosreestrResponse> mono = webClient
                .post()
                .uri("/search")
                .header("Token", appApiToken)
                .body(Mono.just(request), RosreestrRequest.class)
                .retrieve()
                .bodyToMono(RosreestrResponse.class);
        String userName = SecurityUtils.getUsername();
        RosreestrResponse response = mono.block();
        if (response != null) {
            saveCadasterAddresses(response, address, userName);
        }
        return true;
    }

    /**
     * Метод сохранения полученных результатов в базу данных
     *
     * @param rosreestrResponse ответ от росреестра
     */
    private void saveCadasterAddresses(RosreestrResponse rosreestrResponse, String address, String userName) {
        List<CadasterDTO> allObjects = rosreestrResponse.getObject().getAllObjects();
        allObjects.forEach(dto -> {
            CadasterEntity entity = new CadasterEntity(dto, address);
            entity.setModifiedBy(userName);
            cadasterService.create(entity);
        });
    }

    /**
     * Получить детализированную информацию по кадастровому номеру
     *
     * @param cadaster сущность
     */
    public void getEgrnDetails(CadasterEntity cadaster) {
        RosreestrRequest request = new RosreestrRequest(cadaster.getCadNumber());
        Mono<EgrnResponse> mono = webClient.post()
                .uri("/objectInfoFull")
                .header("Token", appApiToken)
                .body(Mono.just(request), RosreestrRequest.class)
                .retrieve()
                .bodyToMono(EgrnResponse.class);
        EgrnResponse egrnResponse = mono.block();
        updateCadaster(egrnResponse, cadaster);
    }

    public void updateEgrnDetails(String address) {
        List<CadasterEntity> entities = cadasterService.findByTag(address);
        entities.forEach(this::getEgrnDetails);
        cadasterService.update(entities);
    }

    /**
     * Обновить запись в базе данных
     *
     * @param response ответ с детальной информацией
     * @param entity   сущность для обновления
     */
    private void updateCadaster(EgrnResponse response, CadasterEntity entity) {
        if (response != null) {
            EgrnDTO egrnDTO = response.getEgrnDTO();
            if (egrnDTO != null) {
                EgrnDetailsDTO detailsDTO = egrnDTO.getDetails();
                if (detailsDTO.getOksType() != null) {
                    entity.setType(detailsDTO.getOksType());
                }
                if (detailsDTO.getFloor() != null) {
                    entity.setFloor(detailsDTO.getFloor());
                }
            }
        }
    }

}
