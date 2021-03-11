package com.db.tradestore.exceptions;

public class InvalidTradeException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public InvalidTradeException(String message){
		super(message);
	}
}
