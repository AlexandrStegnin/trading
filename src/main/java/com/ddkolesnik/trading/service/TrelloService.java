package com.ddkolesnik.trading.service;

import com.ddkolesnik.trading.model.TradingEntity;
import com.ddkolesnik.trading.model.dto.TrelloDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

/**
 * @author Alexandr Stegnin
 */

@Service
public class TrelloService {

    @Value("${yandex.map.url.template}")
    private String yandexMapUrl;

    @Qualifier("trelloWebClient")
    private final WebClient trelloWebClient;

    public TrelloService(WebClient trelloWebClient) {
        this.trelloWebClient = trelloWebClient;
    }

    public void createCard(TradingEntity tradingEntity) {
        TrelloDTO dto = new TrelloDTO();
        dto.setName(prepareCardName(tradingEntity));
        dto.setDescription(prepareDescription(tradingEntity));
        trelloWebClient.post()
                .body(BodyInserters.fromValue(dto))
                .exchange().block();
    }

    private String prepareCardName(TradingEntity entity) {
        return prepare(entity.getAddress())
                .concat(" - ")
                .concat(prepare(entity.getArea()))
                .concat(" - ")
                .concat(prepare(String.valueOf(entity.getPrice())));
    }

    private String prepareDescription(TradingEntity entity) {
        String description = "";
        if (entity.getUrl() != null) {
            description = "ссылка на объявление - " + entity.getUrl() + "\n";
        }
        description += prepareMapUrl(entity);
        return description;
    }

    private String prepareMapUrl(TradingEntity entity) {
        Double lon = entity.getLongitude();
        Double lat = entity.getLatitude();
        if (Objects.isNull(lon) || Objects.isNull(lat)) {
            return "";
        }
        String longitude = String.valueOf(lon).replace(",", ".");
        String latitude = String.valueOf(lat).replace(",", ".");
        return "объект на карте - " + String.format(yandexMapUrl, longitude ,latitude);
    }

    private String prepare(String field) {
        if (Objects.isNull(field)) {
            return "";
        }
        return field;
    }

}
