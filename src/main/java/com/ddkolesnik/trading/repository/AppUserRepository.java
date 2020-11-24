package com.ddkolesnik.trading.repository;

import com.ddkolesnik.trading.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Alexandr Stegnin
 */

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    AppUser findByLogin(String login);

}
