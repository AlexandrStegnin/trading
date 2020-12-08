package com.ddkolesnik.trading.service;

import com.ddkolesnik.trading.model.CadasterEntity;
import com.ddkolesnik.trading.repository.CadasterRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Service
public class CadasterService {

    private final CadasterRepository cadasterRepository;

    public CadasterService(CadasterRepository cadasterRepository) {
        this.cadasterRepository = cadasterRepository;
    }

    public List<CadasterEntity> findAll() {
        return cadasterRepository.findAll();
    }

    public void create(CadasterEntity entity) {
        cadasterRepository.save(entity);
    }

    public List<CadasterEntity> findByTag(String address) {
        return cadasterRepository.findByTag(address);
    }

    public void update(CadasterEntity entity) {
        cadasterRepository.save(entity);
    }

    public void update(Collection<CadasterEntity> cadasterEntities) {
        cadasterRepository.saveAll(cadasterEntities);
    }

    public List<String> getTags() {
        return cadasterRepository.getTags();
    }

}
