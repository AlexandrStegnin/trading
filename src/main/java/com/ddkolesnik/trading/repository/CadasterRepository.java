package com.ddkolesnik.trading.repository;

import com.ddkolesnik.trading.model.CadasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Repository
public interface CadasterRepository extends JpaRepository<CadasterEntity, Long> {

    List<CadasterEntity> findByTag(String tag);

}
