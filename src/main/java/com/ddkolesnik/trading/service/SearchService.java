package com.ddkolesnik.trading.service;

import com.ddkolesnik.trading.configuration.security.SecurityUtils;
import com.ddkolesnik.trading.model.CadasterEntity;
import com.ddkolesnik.trading.model.EgrnResponse;
import com.ddkolesnik.trading.model.RosreestrRequest;
import com.ddkolesnik.trading.model.RosreestrResponse;
import com.ddkolesnik.trading.model.dto.EgrnDTO;
import com.ddkolesnik.trading.model.dto.EgrnDetailsDTO;
import com.ddkolesnik.trading.vaadin.support.VaadinViewUtils;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.server.Command;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */

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
    public void search(String address, UI ui, ListDataProvider<CadasterEntity> dataProvider) {
        List<CadasterEntity> entities = cadasterService.findByTag(address);
        if (!entities.isEmpty()) {
            return;
        }
        RosreestrRequest request = new RosreestrRequest("normal", address, 1);
        request.setQuery(address);
        Mono<RosreestrResponse> mono = webClient.post()
                .uri("/search")
                .body(Mono.just(request), RosreestrRequest.class)
                .retrieve()
                .bodyToMono(RosreestrResponse.class);
        String userName = SecurityUtils.getUsername();
        mono.subscribe(response -> saveCadasterAddresses(response, address, userName, ui, dataProvider));
    }

    /**
     * Метод сохранения полученных результатов в базу данных
     *
     * @param rosreestrResponse ответ от росреестра
     */
    private void saveCadasterAddresses(RosreestrResponse rosreestrResponse, String address, String userName,
                                       UI ui, ListDataProvider<CadasterEntity> dataProvider) {
        rosreestrResponse.getObject().getRooms().forEach(room -> {
            CadasterEntity entity = new CadasterEntity(room, address);
            entity.setModifiedBy(userName);
            cadasterService.create(entity);
        });
        ui.access((Command) () -> {
            VaadinViewUtils.showNotification("Данные успешно получены. Необходимо перезагрузить страницу.");
            dataProvider.refreshAll();
        });
    }

    /**
     * Получить детализированную информацию по кадастровому номеру
     *
     * @param cadaster сущность
     * @param ui ссылка на представление, чтобы можно было оповестить пользователя о завершении
     */
    public void getEgrnDetails(CadasterEntity cadaster, UI ui) {
        RosreestrRequest request = new RosreestrRequest(cadaster.getCadNumber());
        Mono<EgrnResponse> mono = webClient.post()
                .uri("/objectInfoFull")
                .body(Mono.just(request), RosreestrRequest.class)
                .retrieve()
                .bodyToMono(EgrnResponse.class);
        mono.subscribe(response -> updateCadaster(response, cadaster, ui));
    }

    /**
     * Обновить запись в базе данных
     *
     * @param response ответ с детальной информацией
     * @param entity сущность для обновления
     * @param ui ссылка на представление, чтобы можно было оповестить пользователя о завершении
     */
    private void updateCadaster(EgrnResponse response, CadasterEntity entity, UI ui) {
        if (response != null) {
            EgrnDTO egrnDTO = response.getEgrnDTO();
            if (egrnDTO != null) {
                EgrnDetailsDTO detailsDTO = egrnDTO.getDetails();
                entity.setType(detailsDTO.getOksType());
                entity.setFloor(detailsDTO.getFloor());
                cadasterService.update(entity);
            }

        }
        ui.access((Command) () -> VaadinViewUtils.showNotification("Данные успешно получены. Необходимо перезагрузить страницу."));
    }

}
