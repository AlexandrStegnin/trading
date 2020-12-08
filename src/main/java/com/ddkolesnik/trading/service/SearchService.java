package com.ddkolesnik.trading.service;

import com.ddkolesnik.trading.configuration.security.SecurityUtils;
import com.ddkolesnik.trading.model.CadasterEntity;
import com.ddkolesnik.trading.model.EgrnResponse;
import com.ddkolesnik.trading.model.RosreestrRequest;
import com.ddkolesnik.trading.model.RosreestrResponse;
import com.ddkolesnik.trading.model.dto.EgrnDTO;
import com.ddkolesnik.trading.model.dto.EgrnDetailsDTO;
import lombok.extern.slf4j.Slf4j;
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

    private final WebClient webClient;

    private final CadasterService cadasterService;

    public SearchService(CadasterService cadasterService) {
        this.cadasterService = cadasterService;
        webClient = WebClient.builder()
                .baseUrl("https://apirosreestr.ru/api/cadaster")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("Token", "BBKY-DZTN-KMFV-DHPT")
                .build();
    }

    /**
     * Метод поиска объекта по адресу
     *
     * @param address полный адрес
     */
    public void search(String address) {
        List<CadasterEntity> entities = cadasterService.findByTag(address);
        if (!entities.isEmpty()) {
            return;
        }
        RosreestrRequest request = new RosreestrRequest("normal", address, 1);
        Mono<RosreestrResponse> mono = webClient.post()
                .uri("/search")
                .body(Mono.just(request), RosreestrRequest.class)
                .retrieve()
                .bodyToMono(RosreestrResponse.class);
        String userName = SecurityUtils.getUsername();
        RosreestrResponse response = mono.block();
        if (response != null) {
            saveCadasterAddresses(response, address, userName);
        }
    }

    /**
     * Метод сохранения полученных результатов в базу данных
     *
     * @param rosreestrResponse ответ от росреестра
     */
    private void saveCadasterAddresses(RosreestrResponse rosreestrResponse, String address, String userName) {
        rosreestrResponse.getObject().getRooms().forEach(room -> {
            CadasterEntity entity = new CadasterEntity(room, address);
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

    //Тюмень Николая Ростовцева д 25 к 1

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
