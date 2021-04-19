package com.ddkolesnik.trading.service;

import com.ddkolesnik.trading.api.CadasterEntity;
import com.ddkolesnik.trading.api.KadnetData;
import com.ddkolesnik.trading.api.KadnetResponse;
import com.ddkolesnik.trading.configuration.security.SecurityUtils;
import com.ddkolesnik.trading.service.client.ApiClient;
import com.ddkolesnik.trading.vaadin.support.VaadinViewUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
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
    public void getKadnetResponse(String address) {
        Mono<KadnetResponse> response = client.getKadnetResponse(address);
        log.info("Получаем данные из КАДНЕТ");
        String userName = SecurityUtils.getUsername();
        KadnetResponse kadnetResponse = response.block();
        if (kadnetResponse != null) {
            getRosreestrInfo(kadnetResponse, userName, address);
            log.info("Закончили");
        }
    }

    private void getRosreestrInfo(KadnetResponse response, String userName, String tag) {
        if (response.getResult() == null) {
            VaadinViewUtils.showNotification("Не удалось получить ответ от Каднет");
            return;
        }
        List<KadnetData> kadnetData = response.getData()
                .stream()
                .filter(Objects::nonNull)
                .filter(data -> data.getObjectType().equalsIgnoreCase("Помещение"))
                .collect(Collectors.toList());
        log.info("Начинаем разбор адресов [{} шт], полученных из КАДНЕТ", kadnetData.size());
        kadnetData.forEach(data -> {
            CadasterEntity entity = new CadasterEntity(data, tag, userName);
            daDataService.cleanData(entity);
            cadasterService.create(entity);
        });
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
