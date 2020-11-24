package com.ddkolesnik.trading.configuration.support;

import com.vaadin.flow.component.login.LoginI18n;

/**
 * Класс для добавления локализаций на разные языки
 *
 * @author Alexandr Stegnin
 */

public class Internationalization {

    public static LoginI18n russianI18n() {
        final LoginI18n i18n = LoginI18n.createDefault();

        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("");
//        i18n.getHeader().setDescription("Панель администратора ДДК");
        i18n.getForm().setUsername("ИМЯ ПОЛЬЗОВАТЕЛЯ");
        i18n.getForm().setTitle("");
        i18n.getForm().setSubmit("ВОЙТИ");
        i18n.getForm().setPassword("ПАРОЛЬ");
        i18n.getForm().setForgotPassword("ЗАБЫЛ ПАРОЛЬ?");
        i18n.getErrorMessage().setTitle("ИМЯ ПОЛЬЗОВАТЕЛЯ/ПАРОЛЬ НЕ ВЕРНЫ");
        i18n.getErrorMessage()
                .setMessage("ПРОВЕРЬТЕ ВВЕДЁННЫЕ ДАННЫЕ И ПОВТОРИТЕ ПОПЫТКУ.");
//        i18n.setAdditionalInformation("ДОПОЛНИТЕЛЬНАЯ ИНФОРМАЦИЯ.");
        return i18n;
    }

}
