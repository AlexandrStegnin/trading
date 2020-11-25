package com.ddkolesnik.trading.vaadin.ui;

import com.ddkolesnik.trading.model.TradingEntity;
import com.ddkolesnik.trading.service.AppUserService;
import com.ddkolesnik.trading.service.TradingService;
import com.ddkolesnik.trading.vaadin.custom.CustomAppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;

import java.util.*;

import static com.ddkolesnik.trading.configuration.support.Location.TRADING_PAGE;

/**
 * @author Alexandr Stegnin
 */

@Route(value = TRADING_PAGE)
@PageTitle(TradingView.PAGE_TITLE)
@Theme(value = Material.class, variant = Material.LIGHT)
public class TradingView extends CustomAppLayout {

    protected static final String PAGE_TITLE = "ДАННЫЕ ПО ТОРГАМ";

    private final TradingService tradingService;
    private final Grid<TradingEntity> grid;
    private final ListDataProvider<TradingEntity> dataProvider;
    private final CheckboxGroup<Checkbox> checkboxGroup;
    private final Set<Checkbox> items;
    private final Button confirmBtn;

    public TradingView(TradingService tradingService, AppUserService userService) {
        super(userService);
        this.tradingService = tradingService;
        this.grid = new Grid<>();
        this.dataProvider = new ListDataProvider<>(getAll());
        this.checkboxGroup = new CheckboxGroup<>();
        this.items = new LinkedHashSet<>();
        this.confirmBtn = new Button("ПОДТВЕРДИТЬ ВЫДЕЛЕННЫЕ", VaadinIcon.CHECK.create(), e -> confirm());
        init();
    }

    private void init() {
        grid.setDataProvider(dataProvider);

        grid.addColumn(TradingEntity::getCity)
                .setHeader("ГОРОД")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(1);

        grid.addColumn(TradingEntity::getAddress)
                .setHeader("АДРЕС")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(1);

        //todo квадратура

        grid.addColumn(new NumberRenderer<>(TradingEntity::getPrice, "%(,.2f руб",
                Locale.forLanguageTag("RU"), "0.00 руб"))
                .setHeader("СТОИМОСТЬ")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(1);

        //todo источник

        grid.addColumn(TradingEntity::getUrl)
                .setHeader("ССЫЛКА")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(1);

        Checkbox selectAll = createSelectAll();

        grid.addComponentColumn(this::createCheckBox)
                .setHeader(selectAll)
                .setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(1);

        checkboxGroup.addValueChangeListener(event -> {
            if (event.getValue().size() == items.size()) {
                selectAll.setValue(true);
                selectAll.setIndeterminate(false);
            } else if (event.getValue().size() == 0) {
                selectAll.setValue(false);
                selectAll.setIndeterminate(false);
            } else {
                selectAll.setIndeterminate(true);
            }
        });

        confirmBtn.getStyle()
                .set("border", "1px solid")
                .set("margin-left", "auto");
        HorizontalLayout buttonsLayout = new HorizontalLayout(confirmBtn);
        buttonsLayout.setSizeFull();
        buttonsLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        buttonsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(buttonsLayout, grid);
        verticalLayout.setAlignItems(FlexComponent.Alignment.END);
        setContent(verticalLayout);
    }

    private List<TradingEntity> getAll() {
        return tradingService.findAll();
    }

    private void confirm() {
        Set<Checkbox> selectedCheckboxes = checkboxGroup.getSelectedItems();
        List<String> tradingIds = new ArrayList<>();
        for (Checkbox checkbox : selectedCheckboxes) {
            if (checkbox.getId().isPresent()) {
                tradingIds.add(checkbox.getId().get());
            }
        }
        tradingService.confirm(tradingIds);
    }

    private Checkbox createCheckBox(TradingEntity entity) {
        Checkbox checkbox = new Checkbox();
        checkbox.setValue(entity.isConfirmed());
        checkbox.setId(String.valueOf(entity.getId()));
        checkbox.addValueChangeListener(event -> {
            if (event.getValue()) {
                checkboxGroup.select(checkbox);
            } else {
                checkboxGroup.deselect(checkbox);
            }
        });
        items.add(checkbox);
        checkboxGroup.setItems(items);
        return checkbox;
    }

    private Checkbox createSelectAll() {
        Checkbox selectAll = new Checkbox();
        selectAll.addValueChangeListener(event -> {
            if (selectAll.getValue()) {
                items.forEach(checkbox -> checkbox.setValue(true));
            } else {
                items.forEach(checkbox -> checkbox.setValue(false));
            }
        });
        return selectAll;
    }

}
