package com.db.tradestore.validator;

import com.db.tradestore.domain.Trade;

public interface ITradeValidator {
	boolean validate(Trade trade);
}
