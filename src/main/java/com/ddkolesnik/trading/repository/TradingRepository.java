package com.ddkolesnik.trading.repository;

import com.ddkolesnik.trading.model.TradingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Repository
public interface TradingRepository extends JpaRepository<TradingEntity, Long> {

    List<TradingEntity> findByIdIn(Collection<Long> ids);

    List<TradingEntity> findByState(int state);

}
