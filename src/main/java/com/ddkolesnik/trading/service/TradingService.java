package com.ddkolesnik.trading.service;

import com.ddkolesnik.trading.configuration.support.State;
import com.ddkolesnik.trading.model.TradingEntity;
import com.ddkolesnik.trading.repository.TradingRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alexandr Stegnin
 */

@Service
public class TradingService {

    private final TradingRepository tradingRepository;

    public TradingService(TradingRepository tradingRepository) {
        this.tradingRepository = tradingRepository;
    }

    public List<TradingEntity> findAll() {
        return tradingRepository.findByState(State.ACTIVE.getId());
    }

    public TradingEntity create(TradingEntity tradingEntity) {
        return tradingRepository.save(tradingEntity);
    }

    public TradingEntity update(TradingEntity tradingEntity) {
        return tradingRepository.save(tradingEntity);
    }

    public void delete(TradingEntity tradingEntity) {
        tradingEntity.setState(State.ARCHIVE.getId());
        tradingRepository.save(tradingEntity);
    }

    public void delete(Collection<String> tradingIds) {
        List<Long> ids = tradingIds.stream()
                .map(Long::valueOf)
                .collect(Collectors.toList());
        List<TradingEntity> entities = tradingRepository.findByIdIn(ids);
        entities.forEach(entity -> {
            entity.setState(State.ARCHIVE.getId());
            tradingRepository.save(entity);
        });
    }

}
