package com.ddkolesnik.trading.vaadin.form;

import com.ddkolesnik.trading.command.Command;
import com.ddkolesnik.trading.command.trading.CreateTradingCommand;
import com.ddkolesnik.trading.command.trading.DeleteTradingCommand;
import com.ddkolesnik.trading.command.trading.UpdateTradingCommand;
import com.ddkolesnik.trading.configuration.support.OperationEnum;
import com.ddkolesnik.trading.model.TradingEntity;
import com.ddkolesnik.trading.service.TradingService;
import com.ddkolesnik.trading.vaadin.support.VaadinViewUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;


/**
 * @author Alexandr Stegnin
 */

public class TradingForm extends Dialog {

    private final TradingService tradingService;
    private final TradingEntity tradingEntity;
    private final TextField appName;
    private final TextField token;
    private final Binder<TradingEntity> tradingBinder;
    private final OperationEnum operation;
    private final Button cancel;
    private final HorizontalLayout buttons;
    private final VerticalLayout content;
    private final Button submit;
    private boolean canceled = false;

    public TradingForm(OperationEnum operation, TradingEntity tradingEntity, TradingService tradingService) {
        this.appName = new TextField("НАЗВАНИЕ ПРИЛОЖЕНИЯ");
        this.token = new TextField("ТОКЕН");
        this.tradingBinder = new BeanValidationBinder<>(TradingEntity.class);
        this.tradingService = tradingService;
        this.operation = operation;
        this.submit = VaadinViewUtils.createButton(
                operation.name.toUpperCase(), "", "submit", "8px 10px 8px 10px");
        this.cancel = VaadinViewUtils.createButton("ОТМЕНИТЬ", "", "cancel", "8px 10px 8px 10px");
        this.buttons = new HorizontalLayout();
        this.content = new VerticalLayout();
        this.tradingEntity = tradingEntity;
        init();
    }

    private void init() {
        prepareButtons(operation);
        stylizeForm();
        buttons.add(submit, cancel);
        content.add(appName, token, buttons);
        add(content);
        tradingBinder.setBean(tradingEntity);
        tradingBinder.bindInstanceFields(this);
    }

    private void executeCommand(Command command) {
        if (operation.compareTo(OperationEnum.DELETE) == 0) {
            command.execute();
            this.close();
        } else if (tradingBinder.writeBeanIfValid(tradingEntity)) {
            command.execute();
            this.close();
        }
    }

    private void prepareButtons(OperationEnum operation) {
        switch (operation) {
            case CREATE:
                submit.addClickListener(e -> executeCommand(new CreateTradingCommand(tradingEntity, tradingService)));
                break;
            case UPDATE:
                submit.addClickListener(e -> executeCommand(new UpdateTradingCommand(tradingEntity, tradingService)));
                break;
            case DELETE:
                submit.addClickListener(e -> executeCommand(new DeleteTradingCommand(tradingEntity, tradingService)));
                break;
        }
        cancel.addClickListener(e -> {
            this.canceled = true;
            this.close();
        });
    }

    public boolean isCanceled() {
        return canceled;
    }

    private void stylizeForm() {
        appName.setPlaceholder("ВВЕДИТЕ НАЗВАНИЕ");
        appName.setRequiredIndicatorVisible(true);
        appName.setWidthFull();
        token.setWidthFull();
        token.setReadOnly(true);

        buttons.setWidthFull();
        buttons.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        content.setHeightFull();
        setWidth("400px");
        setHeightFull();
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
    }

}
