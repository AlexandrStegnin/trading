package com.ddkolesnik.trading.repository;

import com.ddkolesnik.trading.model.TradingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Alexandr Stegnin
 */

@Repository
public interface TradingRepository extends JpaRepository<TradingEntity, Long> {
}
