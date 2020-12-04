package com.ddkolesnik.trading.vaadin.ui;

import com.ddkolesnik.trading.service.AppUserService;
import com.ddkolesnik.trading.vaadin.custom.CustomAppLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;

import static com.ddkolesnik.trading.configuration.support.Location.ADDRESS_PAGE;

/**
 * @author Alexandr Stegnin
 */

@Route(value = ADDRESS_PAGE)
@PageTitle(AddressView.PAGE_TITLE)
@Theme(value = Material.class, variant = Material.LIGHT)
public class AddressView extends CustomAppLayout {

    protected static final String PAGE_TITLE = "ДАННЫЕ ПО АДРЕСАМ";

    public AddressView(AppUserService userService) {
        super(userService);
    }
}
