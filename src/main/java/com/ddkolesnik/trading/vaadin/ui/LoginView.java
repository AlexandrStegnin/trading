package com.ddkolesnik.trading.vaadin.ui;

import com.ddkolesnik.trading.configuration.support.Internationalization;
import com.ddkolesnik.trading.repository.AuthRepository;
import com.ddkolesnik.trading.vaadin.support.VaadinViewUtils;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.login.AbstractLogin;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;

import static com.ddkolesnik.trading.configuration.support.Location.LOGIN_PAGE;
import static com.ddkolesnik.trading.configuration.support.Location.TRADING_PAGE;

/**
 * @author Alexandr Stegnin
 */

@Route(value = LOGIN_PAGE)
@PageTitle(LoginView.PAGE_TITLE)
@Theme(value = Material.class, variant = Material.LIGHT)
@CssImport(value = "./styles/shared-styles.css", themeFor = "vaadin-login-overlay-wrapper")
public class LoginView extends HorizontalLayout implements HasUrlParameter<String> {

    protected static final String PAGE_TITLE = "АВТОРИЗАЦИЯ";

    private final AuthRepository authRepository;

    public LoginView(AuthRepository authRepository) {
        this.authRepository = authRepository;
        init();
    }

    private void init() {
        LoginOverlay loginForm = new LoginOverlay();
        Div title = new Div();
        title.add(VaadinViewUtils.getLogo(350));
        loginForm.setTitle(title);
        loginForm.setDescription("");
        loginForm.setOpened(true);
        loginForm.setI18n(Internationalization.russianI18n());
        loginForm.setForgotPasswordButtonVisible(false);
        loginForm.addLoginListener(e -> {
            boolean authenticated = authenticate(e);
            if (authenticated) {
                loginForm.close();
                UI.getCurrent().getPage().setLocation(TRADING_PAGE);
            } else {
                loginForm.setError(true);
            }
        });
        setAlignItems(Alignment.CENTER);
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        add(loginForm);
    }

    private boolean authenticate(AbstractLogin.LoginEvent e) {
        return authRepository.authenticate(e.getUsername(), e.getPassword()).isAuthenticated();
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        if (parameter != null && parameter.equalsIgnoreCase("logout")) {
            logout();
        }
    }

    private void logout() {
        Notification.show("ВЫ УСПЕШНО ВЫШЛИ ИЗ СИСТЕМЫ!", 3000, Notification.Position.TOP_END);
        authRepository.logout();
        this.getUI().ifPresent(ui -> ui.navigate(LOGIN_PAGE));
    }

}
