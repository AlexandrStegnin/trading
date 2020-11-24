package com.ddkolesnik.trading.command.trading;

import com.ddkolesnik.trading.command.Command;
import com.ddkolesnik.trading.model.TradingEntity;
import com.ddkolesnik.trading.service.TradingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author Alexandr Stegnin
 */

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DeleteTradingCommand implements Command {

    TradingEntity tradingEntity;

    TradingService tradingService;

    @Override
    public void execute() {
        tradingService.delete(tradingEntity);
    }
}
