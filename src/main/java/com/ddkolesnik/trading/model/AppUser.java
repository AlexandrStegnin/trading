package com.ddkolesnik.trading.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author Alexandr Stegnin
 */

@Data
@Entity
@NoArgsConstructor
@Table(name = "app_user")
@EqualsAndHashCode(callSuper = true)
public class AppUser extends BaseEntity {

    @Size(max = 45, message = "Логин не должен содержать более {max} символов")
    @Column(name = "login", unique = true, updatable = false, nullable = false)
    private String login;

    @NotBlank(message = "Пароль должен быть задан")
    @Column(name = "password")
    private String password;

    @Email(message = "Нужно указать валидный email [example@example.ru]")
    @Column(name = "email")
    private String email;

    @OneToOne
    @JoinColumn(name = "role_id")
    private Role role;

    public AppUser(UserDetails principal) {
        this.login = principal.getUsername();
    }
}
