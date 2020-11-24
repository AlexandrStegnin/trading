package com.ddkolesnik.trading.service;

import com.ddkolesnik.trading.repository.AuthRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * @author Alexandr Stegnin
 */

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthService implements AuthRepository {

    DaoAuthenticationProvider daoAuthenticationProvider;

    @Override
    public Authentication authenticate(String login, String password) {
        Authentication auth = new UsernamePasswordAuthenticationToken(login, password);
        Authentication authentication;
        try {
            authentication = daoAuthenticationProvider.authenticate(auth);
        } catch (AuthenticationException e) {
            return auth;
        }
        if (authentication.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            SecurityContextHolder.clearContext();
        }
        return authentication;
    }

    @Override
    public void logout() {
        SecurityContextHolder.clearContext();
    }

}
