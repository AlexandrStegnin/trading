package com.ddkolesnik.trading.service;

import com.ddkolesnik.trading.model.TradingEntity;
import com.ddkolesnik.trading.repository.TradingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
