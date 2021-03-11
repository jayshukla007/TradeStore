package com.db.tradestore.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.db.tradestore.exceptions.InvalidTradeException;

@ControllerAdvice
public class TradeExceptionHandler extends ResponseEntityExceptionHandler {
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {

        return new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
 
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@ExceptionHandler(InvalidTradeException.class)
    public final ResponseEntity<Object> handleUserNotFoundException(InvalidTradeException ex, WebRequest request) {

        return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }
}
