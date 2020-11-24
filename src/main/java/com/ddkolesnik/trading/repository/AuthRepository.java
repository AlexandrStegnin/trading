package com.ddkolesnik.trading.repository;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * @author Alexandr Stegnin
 */

@Component
public interface AuthRepository {

    Authentication authenticate(String login, String password);

    void logout();

}
