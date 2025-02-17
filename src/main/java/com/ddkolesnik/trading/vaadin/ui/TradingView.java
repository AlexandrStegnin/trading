package com.ddkolesnik.trading.vaadin.ui;

import com.ddkolesnik.trading.configuration.support.OperationEnum;
import com.ddkolesnik.trading.model.TradingEntity;
import com.ddkolesnik.trading.service.AppUserService;
import com.ddkolesnik.trading.service.TradingService;
import com.ddkolesnik.trading.service.TrelloService;
import com.ddkolesnik.trading.vaadin.custom.CustomAppLayout;
import com.ddkolesnik.trading.vaadin.form.TradingForm;
import com.ddkolesnik.trading.vaadin.support.VaadinViewUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
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
import org.springframework.beans.factory.annotation.Value;

import java.util.*;

import static com.ddkolesnik.trading.configuration.support.Location.TRADING_PAGE;

/**
 * @author Alexandr Stegnin
 */

@Route(value = TRADING_PAGE)
@PageTitle(TradingView.PAGE_TITLE)
@Theme(value = Material.class, variant = Material.LIGHT)
public class TradingView extends CustomAppLayout {

    @Value("${yandex.map.url.template}")
    private String yandexMapUrl;

    private final String SHOW_CONFIRMED = "ПОКАЗАТЬ ТОЛЬКО ПОДТВЕРЖДЁННЫЕ";

    protected static final String PAGE_TITLE = "ДАННЫЕ ПО ТОРГАМ";

    private final TradingService tradingService;
    private final Grid<TradingEntity> grid;
    private final ListDataProvider<TradingEntity> dataProvider;
    private final CheckboxGroup<Checkbox> checkboxGroup;
    private final Set<Checkbox> items;
    private final Button showConfirmed;
    private final Button deleteBtn;
    private TradingForm tradingForm;
    private final TrelloService trelloService;

    public TradingView(TradingService tradingService, AppUserService userService, TrelloService trelloService) {
        super(userService);
        this.tradingService = tradingService;
        this.trelloService = trelloService;
        this.grid = new Grid<>();
        this.dataProvider = new ListDataProvider<>(getAll());
        this.checkboxGroup = new CheckboxGroup<>();
        this.items = new LinkedHashSet<>();
        this.deleteBtn = new Button("УДАЛИТЬ ВЫДЕЛЕННЫЕ", VaadinIcon.TRASH.create(), e -> delete());
        this.showConfirmed = new Button(SHOW_CONFIRMED, VaadinIcon.CHECK_SQUARE_O.create(), e -> toggle());
        init();
    }

    private void init() {
        grid.setDataProvider(dataProvider);
        grid.setMultiSort(true);

        grid.addColumn(TradingEntity::getCity, "ГОРОД")
                .setHeader("ГОРОД")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(1);

        grid.addColumn(this::getAddress)
                .setHeader("АДРЕС")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(3);

        grid.addComponentColumn(this::showOnMap)
                .setHeader("НА КАРТЕ")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(1);

        grid.addColumn(TradingEntity::getArea, "ПЛОЩАДЬ")
                .setHeader("ПЛОЩАДЬ")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(1);

        grid.addColumn(new NumberRenderer<>(TradingEntity::getPrice, "%(,.2f руб",
                Locale.forLanguageTag("RU"), "0.00 руб"))
                .setHeader("СТОИМОСТЬ")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(1);

        grid.addColumn(TradingEntity::getLotSource, "ИСТОЧНИК")
                .setHeader("ИСТОЧНИК")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(1);

        grid.addComponentColumn(this::createAnchor)
                .setHeader("ССЫЛКА")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(1);

        grid.addColumn(TradingEntity::getSeller, "ПРОДАВЕЦ")
                .setHeader("ПРОДАВЕЦ")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(1);

        grid.addColumn(TradingEntity::getComment, "КОММЕНТАРИЙ")
                .setHeader("КОММЕНТАРИЙ")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(5);

        grid.addComponentColumn(trading -> VaadinViewUtils.makeEditColumnAction(
                e -> showTradingForm(trading)))
                .setTextAlign(ColumnTextAlign.CENTER)
                .setEditorComponent(new Div())
                .setFlexGrow(1);

        grid.addComponentColumn(trading -> VaadinViewUtils.makeColumnAction(
                e -> showDialog(trading), VaadinIcon.SHARE_SQUARE))
                .setTextAlign(ColumnTextAlign.CENTER)
                .setEditorComponent(new Div())
                .setHeader("В TRELLO")
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

        deleteBtn.getStyle()
                .set("border", "1px solid")
                .set("margin-left", "auto");
        HorizontalLayout buttonsLayout = new HorizontalLayout(showConfirmed, deleteBtn);
        buttonsLayout.setWidthFull();
        buttonsLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        buttonsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(buttonsLayout, grid);
        verticalLayout.setAlignItems(FlexComponent.Alignment.END);
        verticalLayout.setHeightFull();
        setContent(verticalLayout);
    }

    private List<TradingEntity> getAll() {
        return tradingService.findAll();
    }

    private Checkbox createCheckBox(TradingEntity entity) {
        Checkbox checkbox = new Checkbox();
        checkbox.setValue(entity.isConfirmed());
        checkbox.setId(String.valueOf(entity.getId()));
        if (!entity.isConfirmed()) {
            checkbox.addValueChangeListener(event -> {
                if (event.getValue()) {
                    checkboxGroup.select(checkbox);
                } else {
                    checkboxGroup.deselect(checkbox);
                }
            });
        } else {
            checkbox.setReadOnly(true);
        }
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

    private void toggle() {
        if (showConfirmed.getText().equals(SHOW_CONFIRMED)) {
            showConfirmed.setIcon(VaadinIcon.THIN_SQUARE.create());
            showConfirmed.setText("ПОКАЗАТЬ ВСЕ ОБЪЯВЛЕНИЯ");
            showOnlyConfirmed();
        } else {
            showConfirmed.setIcon(VaadinIcon.CHECK_SQUARE_O.create());
            showConfirmed.setText(SHOW_CONFIRMED);
            dataProvider.clearFilters();
        }
    }

    private void showOnlyConfirmed() {
        dataProvider.clearFilters();
        dataProvider.addFilter(TradingEntity::isConfirmed);
    }

    private Anchor createAnchor(TradingEntity entity) {
        Anchor anchor = new Anchor(entity.getUrl(), "ПОКАЗАТЬ");
        anchor.setTarget("_blank");
        return anchor;
    }

    private void delete() {
        tradingService.delete(getSelectedTradingIds());
        VaadinViewUtils.showNotification("Данные успешно удалены");
    }

    private List<String> getSelectedTradingIds() {
        Set<Checkbox> selectedCheckboxes = checkboxGroup.getSelectedItems();
        List<String> tradingIds = new ArrayList<>();
        for (Checkbox checkbox : selectedCheckboxes) {
            if (checkbox.getId().isPresent()) {
                tradingIds.add(checkbox.getId().get());
            }
        }
        return tradingIds;
    }

    private String getAddress(TradingEntity entity) {
        return entity.getCleanAddress() == null ? entity.getAddress() : entity.getCleanAddress();
    }

    private Anchor showOnMap(TradingEntity entity) {
        Double lon = entity.getLongitude();
        Double lat = entity.getLatitude();
        if (lon == null || lat == null) {
            return new Anchor();
        }
        String longitude = String.valueOf(lon).replace(",", ".");
        String latitude = String.valueOf(lat).replace(",", ".");
        String url = String.format(yandexMapUrl, longitude ,latitude);
        Anchor anchor = new Anchor(url, "ПОКАЗАТЬ");
        anchor.setTarget("_blank");
        return anchor;
    }

    private void showTradingForm(TradingEntity tradingEntity) {
        TradingForm tradingForm = new TradingForm(OperationEnum.UPDATE, tradingEntity, tradingService);
        this.tradingForm = tradingForm;
        tradingForm.addOpenedChangeListener(event -> reload(!event.isOpened(), !this.tradingForm.isCanceled()));
        tradingForm.open();
    }

    private void reload(final boolean isClosed, final boolean isNotCanceled) {
        if (isClosed && isNotCanceled) dataProvider.refreshAll();
    }

    private void sendToTrello(TradingEntity tradingEntity) {
        trelloService.createCard(tradingEntity);
    }

    private void showDialog(TradingEntity entity) {
        Button ok = new Button("ДА");
        Dialog dialog = VaadinViewUtils.initConfirmDialog("ПЕРЕМЕСТИТЬ ОБЪЕКТ НА ДОСКУ TRELLO?", ok);
        ok.addClickListener(event -> {
            sendToTrello(entity);
            dialog.close();
            tradingService.delete(entity);
            VaadinViewUtils.showNotification("ОБЪЕКТ УСПЕШНО ПЕРЕМЕЩЁН В TRELLO");
        });
        dialog.open();
    }

}
