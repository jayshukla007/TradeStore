package com.db.tradestore.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.db.tradestore.domain.Trade;
import com.db.tradestore.exceptions.InvalidTradeException;
import com.db.tradestore.service.TradeService;
import com.db.tradestore.validator.TradeValidator;

@ExtendWith(MockitoExtension.class)
public class TradeControllerTest {
	
    @InjectMocks
    private TradeController tradeController;
    @Mock
    private TradeService tradeService;
    @Mock
    private TradeValidator tradeValidator;
    
    @Test
    public void returnNotFoundIfTradesAreNotPresent() {
    	
    	Mockito.when(tradeService.findAllTrades()).thenReturn(Optional.empty());
    	
    	tradeController.findAllTrades();
    	
    	Mockito.verify(tradeService).findAllTrades();
        assertThat(tradeController.findAllTrades().getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    
    @Test
    public void returnAllTradesIfPresent() {
    	List<Trade> tradeList = new ArrayList<Trade>();
    	tradeList.add(new Trade());
    	Optional<List<Trade>> trades = Optional.ofNullable(tradeList);
    	
    	Mockito.when(tradeService.findAllTrades()).thenReturn(trades);
    	
    	tradeController.findAllTrades();
    	
    	Mockito.verify(tradeService).findAllTrades();
        assertThat(tradeController.findAllTrades().getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    
    @Test
    public void throwInvalidTradeExceptionIfTradeIsNotValid() {
    	Trade trade = new Trade();
    	trade.setTradeId("T1");
    	Mockito.when(tradeValidator.validate(trade)).thenReturn(false);
    	String expectedExceptionMessage = "Invalid trade : T1";
    	
    	Exception exception = assertThrows(InvalidTradeException.class, () -> {
    		tradeController.storeTrades(trade);
        });
    	
    	assertTrue(exception.getMessage().contains(expectedExceptionMessage));

    }
    
    @Test
    public void shouldStoreValidTrades() {
    	Trade trade = new Trade();
    	trade.setTradeId("T1");
    	Mockito.when(tradeValidator.validate(trade)).thenReturn(true);
    	
    	tradeController.storeTrades(trade);

    	Mockito.verify(tradeService).storeTrades(trade);
        assertThat(tradeController.storeTrades(trade).getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}
