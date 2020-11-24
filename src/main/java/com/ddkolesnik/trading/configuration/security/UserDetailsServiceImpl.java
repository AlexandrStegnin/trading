package com.ddkolesnik.trading.configuration.security;

import com.ddkolesnik.trading.model.AppUser;
import com.ddkolesnik.trading.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * @author Alexandr Stegnin
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AppUserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final AppUser user = userRepository.findByLogin(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with login '%s'.", username));
        }
        return new org.springframework.security.core.userdetails.User(
                user.getLogin(), user.getPassword(), true, true,
                true, true, Collections.singleton(user.getRole()));
    }

}
