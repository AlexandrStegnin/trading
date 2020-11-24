package com.ddkolesnik.trading.vaadin.custom;

import com.ddkolesnik.trading.configuration.security.SecurityUtils;
import com.ddkolesnik.trading.model.AppUser;
import com.ddkolesnik.trading.repository.AuthRepository;
import com.ddkolesnik.trading.service.AppUserService;
import com.ddkolesnik.trading.vaadin.ui.LoginView;
import com.ddkolesnik.trading.vaadin.ui.TradingView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PageConfigurator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

import static com.ddkolesnik.trading.configuration.support.Location.LOGIN_PAGE;

public class CustomAppLayout extends AppLayout implements BeforeEnterObserver, PageConfigurator {

    @Autowired
    private AuthRepository auth;

    private final Tabs tabs = new Tabs();
    private final Map<Class<? extends Component>, Tab> navigationTargetToTab = new HashMap<>();

    private final AppUser currentDbUser;

    private void init() {
        tabs.setSizeFull();
//        Image img = VaadinViewUtils.getLogo(51);
//        addToNavbar(img);
        if (SecurityUtils.isUserLoggedIn()) {
//            addMenuTab("АДМИНКА", AdminView.class, VaadinIcon.COG.create());
//            addMenuTab("ПОЛЬЗОВАТЕЛИ", UserView.class, VaadinIcon.USER.create());
//            addMenuTab("РОЛИ", RoleView.class, VaadinIcon.SHIELD.create());
            addMenuTab("ДАННЫЕ ПО ТОРГАМ", TradingView.class, VaadinIcon.LOCK.create());
            addMenuTab("ВЫЙТИ", LoginView.class, VaadinIcon.SIGN_OUT.create());
            tabs.setOrientation(Tabs.Orientation.HORIZONTAL);
        } else {
            addMenuTab("ВОЙТИ", LoginView.class, VaadinIcon.SIGN_IN.create());
        }
        addToNavbar(tabs);
    }

    public CustomAppLayout(AppUserService userService) {
        this.currentDbUser = userService.findByLogin(SecurityUtils.getUsername());
        init();
    }

    private void logout() {
        Notification.show("ВЫ УСПЕШНО ВЫШЛИ ИЗ СИСТЕМЫ!", 3000, Notification.Position.TOP_END);
        auth.logout();
        this.getUI().ifPresent(ui -> ui.navigate(LOGIN_PAGE));
    }

    protected AppUser getCurrentDbUser() {
        return currentDbUser;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Tab selectedTab = navigationTargetToTab.get(event.getNavigationTarget());
        tabs.setSelectedTab(selectedTab);
        String tabName = selectedTab.getElement().getTextRecursively();
        if (tabName.equalsIgnoreCase("ВЫЙТИ")) {
            logout();
        }
    }

    private void addMenuTab(String label, Class<? extends Component> target, Icon icon) {
        RouterLink link = new RouterLink(label, target);
        icon.getStyle().set("margin-left", "5px");
        link.add(icon);
        link.getStyle().set("color", "#6200ee")
                        .set("text-decoration", "none");
        Tab tab = new Tab(link);
        navigationTargetToTab.put(target, tab);
        tab.getStyle()
                .set("font-size", "16px")
                .set("font-weight", "bold");
        tabs.add(tab);
    }

    @Override
    public void configurePage(InitialPageSettings settings) {
        settings.addMetaTag("viewport", "width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no");
        // Favicon
        settings.addLink("shortcut icon", "./icons/favicon.ico");
        settings.addFavIcon("icon", "./icons/icon-192.png", "192x192");
    }
}
