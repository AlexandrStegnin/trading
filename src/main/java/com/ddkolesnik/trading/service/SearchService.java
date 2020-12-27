package com.ddkolesnik.trading.service;

import com.ddkolesnik.trading.api.CadasterEntity;
import com.ddkolesnik.trading.api.EgrnResponse;
import com.ddkolesnik.trading.api.RosreestrRequest;
import com.ddkolesnik.trading.api.RosreestrResponse;
import com.ddkolesnik.trading.configuration.security.SecurityUtils;
import com.ddkolesnik.trading.model.dto.CadasterDTO;
import com.ddkolesnik.trading.model.dto.EgrnDTO;
import com.ddkolesnik.trading.model.dto.EgrnDetailsDTO;
import com.ddkolesnik.trading.service.client.ApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Locale;

/**
 * @author Alexandr Stegnin
 */

@Slf4j
@Service
public class SearchService {

    private final ApiClient client;

    private final CadasterService cadasterService;

    public SearchService(ApiClient client, CadasterService cadasterService) {
        this.client = client;
        this.cadasterService = cadasterService;
    }

    /**
     * Метод поиска объекта по адресу
     *
     * @param tag тэг для хранения информации
     */
    public boolean search(String tag) {
        String address = prepareAddress(tag);
        List<CadasterEntity> entities = cadasterService.findByTagLike(address);
        if (!entities.isEmpty()) {
            return false;
        }
        RosreestrRequest request = new RosreestrRequest("normal", address, 1);
        Mono<RosreestrResponse> mono = client.getRosreestrResponse(request);
        String userName = SecurityUtils.getUsername();
        RosreestrResponse response = mono.block();
        if (response != null) {
            saveCadasterAddresses(response, userName, tag);
        }
        return true;
    }

    /**
     * Метод сохранения полученных результатов в базу данных
     *
     * @param rosreestrResponse ответ от росреестра
     * @param tag тэг, под которым хранить инфо
     */
    private void saveCadasterAddresses(RosreestrResponse rosreestrResponse, String userName, String tag) {
        List<CadasterDTO> allObjects = rosreestrResponse.getObject().getAllObjects();
        allObjects.forEach(dto -> {
            CadasterEntity entity = new CadasterEntity(dto, tag);
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
        Mono<EgrnResponse> mono = client.getCadasterDetails(request);
        EgrnResponse egrnResponse = mono.block();
        updateCadaster(egrnResponse, cadaster);
    }

    public void updateEgrnDetails(String tag) {
        String address = prepareAddress(tag);
        List<CadasterEntity> entities = cadasterService.findByTagLike(address);
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

    /**
     * Подготовить адрес к поиску
     *
     * @param address адрес
     * @return подготовленная строка для поиска
     */
    private String prepareAddress(String address) {
        String[] parts = address.trim().toLowerCase(Locale.ROOT).split("\\s");
        StringBuilder result = new StringBuilder();
        for (String part : parts) {
            result.append("%").append(part);
        }
        result.append("%");
        return result.toString();
    }

}
