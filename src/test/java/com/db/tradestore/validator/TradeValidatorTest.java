package com.db.tradestore.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.db.tradestore.domain.Trade;
import com.db.tradestore.repository.TradeRepository;

@ExtendWith(MockitoExtension.class)
public class TradeValidatorTest {
	
    @InjectMocks
    private TradeValidator tradeValidator;
    @Mock
    TradeRepository tradeRepository;

    
    @Test
    public void shouldReturnFalseIfMatrityDateIsBeforeCurrentDate() {
    	Trade trade = new Trade();
    	trade.setTradeId("T1");
    	trade.setMaturityDate(LocalDate.of(2019, 04, 19));
    	    	
    	boolean response = tradeValidator.isValidMaturityDate(trade);
    	
    	assertFalse(response);
    }
    
    @Test
    public void shouldReturnTrueIfMatrityDateIsAfterOrEqualToCurrentDate() {
    	Trade trade = new Trade();
    	trade.setTradeId("T1");
    	trade.setMaturityDate(LocalDate.now());
    	    	
    	boolean response = tradeValidator.isValidMaturityDate(trade);
    	
    	assertTrue(response);
    }
    
    @Test
    public void shouldReturnTrueIfMatrityDateAndVersionIsValid() {
    	Trade trade = new Trade();
    	trade.setTradeId("T1");
    	trade.setMaturityDate(LocalDate.now());
    	
    	Mockito.when(tradeRepository.findById(trade.getTradeId())).thenReturn(Optional.empty());
    	    	
    	boolean response = tradeValidator.validate(trade);
    	
    	assertTrue(response);
    	Mockito.verify(tradeRepository).findById(trade.getTradeId());
    }
    
    @Test
    public void shouldReturnFalseIfMatrityDateIsValidButVersionIsInValid() {
    	Trade trade1 = new Trade();
    	trade1.setTradeId("T1");
    	trade1.setVersion(2);
    	trade1.setMaturityDate(LocalDate.now());
    	
    	Trade trade2 = new Trade();
    	trade2.setTradeId("T1");
    	trade2.setVersion(1);
    	trade2.setMaturityDate(LocalDate.now());
    	
    	List<Trade> list = new ArrayList<Trade>();
    	list.add(trade1);
    	
    	Mockito.when(tradeRepository.findById(trade2.getTradeId())).thenReturn(Optional.ofNullable(list));
    	    	
    	boolean response = tradeValidator.validate(trade2);
    	
    	assertFalse(response);
    	Mockito.verify(tradeRepository).findById(trade2.getTradeId());
    }
    
    @Test
    public void shouldReturnFalseWhenVersionIsValidButMatrityDateIsInValid() {

    	Trade trade2 = new Trade();
    	trade2.setTradeId("T1");
    	trade2.setVersion(2);
    	trade2.setMaturityDate(LocalDate.of(2019, 04, 19));
    	    	    	
    	boolean response = tradeValidator.validate(trade2);
    	
    	assertFalse(response);
    }

}
