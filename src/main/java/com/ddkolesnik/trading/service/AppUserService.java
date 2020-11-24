package com.ddkolesnik.trading.service;

import com.ddkolesnik.trading.model.AppUser;
import com.ddkolesnik.trading.repository.AppUserRepository;
import org.springframework.stereotype.Service;

/**
 * @author Alexandr Stegnin
 */

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;

    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public AppUser create(AppUser user) {
        return appUserRepository.save(user);
    }

    public AppUser findByLogin(String login) {
        return appUserRepository.findByLogin(login);
    }

}
