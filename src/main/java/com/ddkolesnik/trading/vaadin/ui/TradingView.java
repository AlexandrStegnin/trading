package com.ddkolesnik.trading.vaadin.ui;

import com.ddkolesnik.trading.configuration.support.OperationEnum;
import com.ddkolesnik.trading.model.TradingEntity;
import com.ddkolesnik.trading.service.AppUserService;
import com.ddkolesnik.trading.service.TradingService;
import com.ddkolesnik.trading.vaadin.custom.CustomAppLayout;
import com.ddkolesnik.trading.vaadin.form.TradingForm;
import com.ddkolesnik.trading.vaadin.support.VaadinViewUtils;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;

import java.util.List;

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
    private TradingForm tradingForm;

    public TradingView(TradingService tradingService, AppUserService userService) {
        super(userService);
        this.tradingService = tradingService;
        this.grid = new Grid<>();
        this.dataProvider = new ListDataProvider<>(getAll());
        init();
    }

    private void init() {
        grid.setDataProvider(dataProvider);

        grid.addColumn(TradingEntity::getLot)
                .setHeader("ЛОТ")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(1);

        grid.addColumn(TradingEntity::getDescription)
                .setHeader("ОПИСАНИЕ")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(1);

        grid.addColumn(TradingEntity::getAddress)
                .setHeader("АДРЕС")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(1);

        grid.addColumn(TradingEntity::getTradingNumber)
                .setHeader("НОМЕР ТОРГОВ")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(1);

        grid.addColumn(TradingEntity::getEfrsbId)
                .setHeader("ИДЕНТИФИКАТОР В ЕФРСБ")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(1);

        grid.addColumn(TradingEntity::getAuctionStep)
                .setHeader("ШАГ АУКЦИОНА")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(1);

        grid.addColumn(TradingEntity::getDepositAmount)
                .setHeader("СУММА ЗАДАТКА")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(1);

        grid.addColumn(TradingEntity::getTradingTime)
                .setHeader("ВРЕМЯ ТОРГОВ")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(1);

        grid.addColumn(TradingEntity::getAcceptRequestsDate)
                .setHeader("ПРИЁМ ЗАЯВОК")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(1);

        grid.addColumn(TradingEntity::getUrl)
                .setHeader("ССЫЛКА")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(1);

        grid.addColumn(TradingEntity::getPrice)
                .setHeader("СТОИМОСТЬ")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(1);

        grid.addColumn(TradingEntity::getSeller)
                .setHeader("ПРОДАВЕЦ")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(1);

        grid.addColumn(TradingEntity::getCity)
                .setHeader("ГОРОД")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(1);

        grid.addComponentColumn(role -> VaadinViewUtils.makeEditorColumnActions(
                e -> showAppTokenForm(OperationEnum.UPDATE, role),
                e -> showAppTokenForm(OperationEnum.DELETE, role)))
                .setTextAlign(ColumnTextAlign.CENTER)
                .setEditorComponent(new Div())
                .setFlexGrow(2)
                .setHeader("ДЕЙСТВИЯ");
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(grid);
        verticalLayout.setAlignItems(FlexComponent.Alignment.END);
        setContent(verticalLayout);
    }

    private List<TradingEntity> getAll() {
        return tradingService.findAll();
    }

    private void showAppTokenForm(final OperationEnum operation, final TradingEntity entity) {
        TradingForm tradingForm = new TradingForm(operation, entity, tradingService);
        this.tradingForm = tradingForm;
        tradingForm.addOpenedChangeListener(e -> refreshDataProvider(e.isOpened(), operation, entity));
        tradingForm.open();
    }

    private void refreshDataProvider(final boolean isOpened, final OperationEnum operation, final TradingEntity entity) {
        if (!isOpened && !tradingForm.isCanceled()) {
            if (operation.compareTo(OperationEnum.CREATE) == 0) dataProvider.getItems().add(entity);
            else if (operation.compareTo(OperationEnum.DELETE) == 0) dataProvider.getItems().remove(entity);
            dataProvider.refreshAll();
        }
    }

}
