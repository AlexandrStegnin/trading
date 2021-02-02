package com.ddkolesnik.trading.service;

import com.ddkolesnik.trading.api.CadasterEntity;
import org.springframework.stereotype.Service;
import ru.redcom.lib.integration.api.client.dadata.DaDataClient;
import ru.redcom.lib.integration.api.client.dadata.dto.Address;

/**
 * @author Alexandr Stegnin
 */

@Service
public class DaDataService {

    private final DaDataClient daDataClient;

    public DaDataService(DaDataClient daDataClient) {
        this.daDataClient = daDataClient;
    }

    public void cleanData(CadasterEntity entity) {
        Address address = daDataClient.cleanAddress(entity.getAddress());
        entity.setAddress(prepareAddress(address));
    }

    public String getCleanAddress(String address) {
        Address cleanAddress = daDataClient.cleanAddress(address);
        return prepareAddress(cleanAddress);
    }

    private String getIfNull(String data) {
        if (data == null) {
            return "";
        }
        return data;
    }

    private String prepareAddress(Address address) {
        String city = getIfNull(address.getCityWithType());
        String street = getIfNull(address.getStreetWithType());
        String house = getIfNull(address.getHouse());
        String blockType = getIfNull(address.getBlockType());
        String block = getIfNull(address.getBlock());
        return String.format("%s %s %s %s %s", city, street, house, blockType, block)
                .replaceAll("\\s{2,}", "")
                .trim();
    }

}
