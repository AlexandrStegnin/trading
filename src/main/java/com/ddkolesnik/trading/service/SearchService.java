package com.ddkolesnik.trading.service;

import com.ddkolesnik.trading.api.*;
import com.ddkolesnik.trading.configuration.security.SecurityUtils;
import com.ddkolesnik.trading.model.dto.CadasterDTO;
import com.ddkolesnik.trading.model.dto.EgrnDTO;
import com.ddkolesnik.trading.model.dto.EgrnDetailsDTO;
import com.ddkolesnik.trading.model.dto.FiasResponseDTO;
import com.ddkolesnik.trading.service.client.ApiClient;
import com.ddkolesnik.trading.vaadin.support.VaadinViewUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @author Alexandr Stegnin
 */

@Slf4j
@Service
public class SearchService {

    private final ApiClient client;

    private final CadasterService cadasterService;

    private final DaDataService daDataService;

    public SearchService(ApiClient client, CadasterService cadasterService,
                         DaDataService daDataService) {
        this.client = client;
        this.cadasterService = cadasterService;
        this.daDataService = daDataService;
    }

    /**
     * Метод проверки наличия записей в базе по тэгу
     *
     * @param tag тэг
     * @return результат проверки
     */
    public boolean existByTag(String tag) {
        if (tag == null) {
            return false;
        }
        String address = prepareAddress(tag);
        return cadasterService.existByTag(address);
    }

    /**
     * Получить список доступных похожих адресов
     *
     * @param address адрес для поиска в ФИАС
     */
    public void getFiasResponse(String address) {
        Mono<FiasResponse> response = client.getFiasResponse(address);
        log.info("Получаем данные из ФИАС");
        String userName = SecurityUtils.getUsername();
        FiasResponse fiasResponse = response.block();
        if (fiasResponse != null) {
            getRosreestrInfo(fiasResponse, userName, address);
            log.info("Закончили");
        }
    }

    private void getRosreestrInfo(FiasResponse response, String userName, String tag) {
        if (response.getResult() == null) {
            VaadinViewUtils.showNotification("Не удалось получить ответ от ФИАС");
            return;
        }
        List<String> addresses = response.getResult()
                .stream()
                .filter(fiasResponseDTO -> fiasResponseDTO.getContentType().equalsIgnoreCase("building"))
                .map(FiasResponseDTO::getFullName)
                .collect(Collectors.toList());
        log.info("Начинаем разбор адресов [{} шт], полученных из ФИАС", addresses.size());
        addresses.forEach(address -> getRosreestrInfo(address, userName, tag));
    }

    /**
     * Получить ответ от Росреестра по адресу
     *  @param address адрес
     * @param userName имя текущего пользователя
     * @param tag тэг под которым надо сохранить инфо
     */
    private void getRosreestrInfo(String address, String userName, String tag) {
        RosreestrRequest request = new RosreestrRequest("normal", address, 0);
        Mono<RosreestrResponse> mono = client.getRosreestrResponse(request);
        RosreestrResponse rosreestrResponse = mono.block();
        if (rosreestrResponse != null) {
            log.info("Обновляем данные, полученные из Росреестра. Добавляем тип и этаж.");
            getEgrnDetails(rosreestrResponse, userName, tag);
        }
    }

    /**
     * Получить детализированную информацию по кадастровому номеру
     *
     * @param rosreestrResponse ответ Росреестра
     */
    private void getEgrnDetails(RosreestrResponse rosreestrResponse, String userName, String tag) {
        List<CadasterDTO> allObjects = rosreestrResponse.getObject().getAllObjects();
        log.info("Получаем детали по [{} шт] объектам", allObjects.size());
        allObjects.forEach(dto -> {
            CadasterEntity entity = new CadasterEntity(dto, tag, userName);
            RosreestrRequest request = new RosreestrRequest(entity.getCadNumber());
            Mono<EgrnResponse> mono = client.getCadasterDetails(request);
            EgrnResponse egrnResponse = mono.block();
            updateCadaster(egrnResponse, entity);
        });
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
                daDataService.cleanData(entity);
                cadasterService.create(entity);
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
