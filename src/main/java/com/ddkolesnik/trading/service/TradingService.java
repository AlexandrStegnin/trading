package com.ddkolesnik.trading.service;

import com.ddkolesnik.trading.configuration.support.State;
import com.ddkolesnik.trading.model.TradingEntity;
import com.ddkolesnik.trading.repository.TradingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return tradingRepository.findAll();
    }

    public TradingEntity create(TradingEntity tradingEntity) {
        return tradingRepository.save(tradingEntity);
    }

    public TradingEntity update(TradingEntity tradingEntity) {
        return tradingRepository.save(tradingEntity);
    }

    public void delete(TradingEntity tradingEntity) {
        tradingRepository.delete(tradingEntity);
    }

    @Transactional
    public void confirm(List<String> ids) {
        ids.forEach(id -> {
            TradingEntity entity = tradingRepository.getOne(Long.valueOf(id));
            entity.setConfirmed(true);
            tradingRepository.save(entity);
        });
    }

    public void delete(Collection<String> tradingIds) {
        tradingIds.forEach(id -> tradingRepository.delete(tradingRepository.getOne(Long.valueOf(id))));
    }

    public List<TradingEntity> findByIdIn(List<Long> ids) {
        return tradingRepository.findByIdIn(ids);
    }

}
