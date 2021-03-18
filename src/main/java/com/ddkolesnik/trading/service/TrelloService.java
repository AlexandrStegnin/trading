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

    @Value("${trello.yuliu.member.id}")
    private String yuliuMemberId;

    @Qualifier("trelloWebClient")
    private final WebClient trelloWebClient;

    public TrelloService(WebClient trelloWebClient) {
        this.trelloWebClient = trelloWebClient;
    }

    public void createCard(TradingEntity tradingEntity) {
        TrelloDTO dto = new TrelloDTO();
        dto.setName(prepareCardName(tradingEntity));
        dto.setIdMembers(new String[] {yuliuMemberId});
        dto.setDescription("ссылка - " + tradingEntity.getUrl());
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

    private String prepare(String field) {
        if (Objects.isNull(field)) {
            return "";
        }
        return field;
    }

}
