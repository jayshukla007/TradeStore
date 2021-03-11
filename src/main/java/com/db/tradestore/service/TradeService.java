package com.db.tradestore.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.tradestore.domain.Trade;
import com.db.tradestore.repository.TradeRepository;
import com.db.tradestore.validator.TradeValidator;

@Service
public class TradeService {

    @Autowired
    TradeRepository tradeRepository;
    
    @Autowired
    TradeValidator tradeValidator;
    
    public void storeTrades(Trade trade) {
    	Optional<List<Trade>> exsitingTrades = tradeRepository.findById(trade.getTradeId());
    	
    	if(exsitingTrades.isPresent() && !exsitingTrades.get().isEmpty()) {
    		Optional<Trade> exisitingTrade = exsitingTrades.get().stream().filter(t -> trade.getVersion() == t.getVersion()).findFirst();
    		if(exisitingTrade.isPresent()) {
    			trade.setId(exisitingTrade.get().getId());
    		}
    	}
    	trade.setCreatedDate(LocalDate.now());
    	tradeRepository.save(trade);
    }
    
    public Optional<List<Trade>> findAllTrades() {
    	return Optional.ofNullable(tradeRepository.findAll());
    }
    
    public void updateTradeStatus() {
    	if(!tradeRepository.findAll().isEmpty()) {
    		tradeRepository.findAll().stream().forEach(trade -> {
    			if(!tradeValidator.isValidMaturityDate(trade)) {
    				trade.setExpired('Y');
    				tradeRepository.save(trade);
    			}
    		});
    	}
    }
}
