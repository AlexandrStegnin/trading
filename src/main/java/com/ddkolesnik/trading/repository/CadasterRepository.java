package com.ddkolesnik.trading.repository;

import com.ddkolesnik.trading.api.CadasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Repository
public interface CadasterRepository extends JpaRepository<CadasterEntity, Long> {

    @Query("SELECT DISTINCT ce.tag FROM CadasterEntity ce")
    List<String> getTags();

    @Query("SELECT ce FROM CadasterEntity ce WHERE LOWER(ce.tag) LIKE LOWER(:tag)")
    List<CadasterEntity> findByTagLike(@Param("tag") String tag);

}
