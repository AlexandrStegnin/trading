package com.ddkolesnik.trading.vaadin.ui;

import com.ddkolesnik.trading.model.CadasterEntity;
import com.ddkolesnik.trading.service.AppUserService;
import com.ddkolesnik.trading.service.CadasterService;
import com.ddkolesnik.trading.service.SearchService;
import com.ddkolesnik.trading.vaadin.custom.CustomAppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.communication.PushMode;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.ddkolesnik.trading.configuration.support.Location.ADDRESS_PAGE;

/**
 * @author Alexandr Stegnin
 */

@Slf4j
@Push(PushMode.AUTOMATIC)
@Route(value = ADDRESS_PAGE)
@PageTitle(AddressView.PAGE_TITLE)
@Theme(value = Material.class, variant = Material.LIGHT)
public class AddressView extends CustomAppLayout {

    protected static final String PAGE_TITLE = "ДАННЫЕ ПО АДРЕСАМ";

    private final CadasterService cadasterService;
    private final Grid<CadasterEntity> grid;
    private final ListDataProvider<CadasterEntity> dataProvider;
    private final TextField search;
    private final Button searchBtn;
    private final SearchService searchService;

    public AddressView(AppUserService userService, CadasterService cadasterService, SearchService searchService) {
        super(userService);
        this.cadasterService = cadasterService;
        this.grid = new Grid<>();
        this.dataProvider = new ListDataProvider<>(getAll());
        this.search = new TextField("ЗАПРОСИТЬ ИНФОРМАЦИЮ ПО АДРЕСУ");
        this.searchBtn = new Button("ЗАПРОСИТЬ", VaadinIcon.SEARCH.create(), e -> search(search.getValue()));
        this.searchService = searchService;
        init();
    }

    private List<CadasterEntity> getAll() {
        return cadasterService.findAll();
    }

    private void init() {
        grid.setDataProvider(dataProvider);
        grid.setMultiSort(true);

        grid.addColumn(CadasterEntity::getCadNumber)
                .setHeader("КАДАСТРОВЫЙ НОМЕР")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(1);

        grid.addColumn(CadasterEntity::getAddress, "АДРЕС")
                .setHeader("АДРЕС")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(1);

        grid.addColumn(CadasterEntity::getType, "ТИП")
                .setHeader("ТИП")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(1);

        grid.addColumn(CadasterEntity::getArea)
                .setHeader("ПЛОЩАДЬ")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(1);

        grid.addColumn(CadasterEntity::getFloor)
                .setHeader("ЭТАЖ")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(1);

        search.setWidth("90%");
        HorizontalLayout searchLayout = new HorizontalLayout();
        searchLayout.setWidthFull();
        searchLayout.add(search, searchBtn);
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(searchLayout, grid);
        verticalLayout.setAlignItems(FlexComponent.Alignment.END);
        verticalLayout.setHeightFull();
        setContent(verticalLayout);
    }

    private void search(String address) {
        Notification notification = new Notification("Запрос отправлен!", 2_000);
        notification.open();
        searchService.search(address);
        searchService.updateEgrnDetails(address);
        notification = new Notification("Данные обновлены!", 2_000);
        notification.open();
    }

}
