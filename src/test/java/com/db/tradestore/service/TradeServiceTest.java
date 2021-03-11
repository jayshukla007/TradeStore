package com.db.tradestore.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.db.tradestore.domain.Trade;
import com.db.tradestore.repository.TradeRepository;
import com.db.tradestore.validator.TradeValidator;

@ExtendWith(MockitoExtension.class)
public class TradeServiceTest {
	
    @InjectMocks
    private TradeService tradeService;
    @Mock
    TradeRepository tradeRepository;
    @Mock
    private TradeValidator tradeValidator;
    
    @Captor
    ArgumentCaptor<Trade> tradeCaptor;
    
	@Test
    public void shouldStoreTrade() {
    	Trade trade = new Trade();
    	trade.setTradeId("T1");
    	Mockito.when(tradeRepository.save(trade)).thenReturn(trade);
    	
    	tradeService.storeTrades(trade);
    	
    	Mockito.verify(tradeRepository).save(trade);
    	assertTrue(trade.getCreatedDate().equals(LocalDate.now()));
    }
	
	@Test
    public void shouldOverrideTradeIfSameVersionIsAlreadyPresent() {
    	Trade trade = new Trade();
    	trade.setId(3l);
    	trade.setTradeId("T1");
    	trade.setVersion(4);
    	trade.setMaturityDate(LocalDate.of(2019, 04, 19));
    	
    	Trade existingTrade = new Trade();
    	existingTrade.setTradeId("T1");
    	existingTrade.setId(1l);
    	existingTrade.setVersion(4);
    	existingTrade.setMaturityDate(LocalDate.of(2019, 04, 04));
    	List<Trade> list = new ArrayList<Trade>();
    	list.add(existingTrade);
    	
    	Mockito.when(tradeRepository.findById(trade.getTradeId())).thenReturn(Optional.ofNullable(list));
    	Mockito.when(tradeRepository.save(trade)).thenReturn(trade);
    	
    	tradeService.storeTrades(trade);
    	
    	Mockito.verify(tradeRepository).save(tradeCaptor.capture());
    	
    	Trade value = tradeCaptor.getValue();
    	
    	Mockito.verify(tradeRepository).save(trade);
    	assertTrue(existingTrade.getId().equals(value.getId()));
    }
	
	@Test
    public void shouldNotOverrideIfSameVersionTradesAreNotPresent() {
    	Trade trade = new Trade();
    	trade.setId(3l);
    	trade.setTradeId("T1");
    	trade.setVersion(5);
    	trade.setMaturityDate(LocalDate.of(2019, 04, 19));
    	
    	Trade existingTrade = new Trade();
    	existingTrade.setTradeId("T1");
    	existingTrade.setId(1l);
    	existingTrade.setVersion(4);
    	existingTrade.setMaturityDate(LocalDate.of(2019, 04, 04));
    	List<Trade> list = new ArrayList<Trade>();
    	list.add(existingTrade);
    	
    	Mockito.when(tradeRepository.findById(trade.getTradeId())).thenReturn(Optional.empty());
    	Mockito.when(tradeRepository.save(trade)).thenReturn(trade);
    	
    	tradeService.storeTrades(trade);
    	
    	Mockito.verify(tradeRepository).save(tradeCaptor.capture());
    	
    	Trade value = tradeCaptor.getValue();
    	
    	Mockito.verify(tradeRepository).save(trade);
    	assertFalse(existingTrade.getId().equals(value.getId()));
    }
    
    @Test
    public void returnAllTrades() {
    	List<Trade> tradeList = new ArrayList<Trade>();
    	tradeList.add(new Trade());
    	
    	Mockito.when(tradeRepository.findAll()).thenReturn(tradeList);
    	
    	tradeService.findAllTrades();
    	
    	Mockito.verify(tradeRepository).findAll();
    }
    
    
    @Test
    public void shouldUpdateTradeExpiryStatusIfMaturityDateIsPassed() {
    	Trade trade1 = new Trade();
    	trade1.setTradeId("T1");
    	trade1.setMaturityDate(LocalDate.of(2019, 04, 19));
    	
    	Trade trade2 = new Trade();
    	trade2.setTradeId("T2");
    	trade2.setMaturityDate(LocalDate.now());
    	Mockito.when(tradeValidator.isValidMaturityDate(trade1)).thenReturn(false);
    	Mockito.when(tradeValidator.isValidMaturityDate(trade2)).thenReturn(true);
    	
    	List<Trade> tradeList = new ArrayList<Trade>();
    	tradeList.add(trade1);
    	tradeList.add(trade2);
    	
    	Mockito.when(tradeRepository.findAll()).thenReturn(tradeList);
    	Mockito.when(tradeRepository.save(trade1)).thenReturn(trade1);
    	
    	tradeService.updateTradeStatus();

    	Mockito.verify(tradeRepository).save(trade1);
    	Mockito.verify(tradeRepository, times(2)).findAll();
    	Mockito.verify(tradeValidator).isValidMaturityDate(trade1);
    }

}
