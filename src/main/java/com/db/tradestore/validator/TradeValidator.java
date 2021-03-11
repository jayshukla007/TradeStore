package com.db.tradestore.validator;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.db.tradestore.domain.Trade;
import com.db.tradestore.repository.TradeRepository;

@Component
public class TradeValidator implements ITradeValidator {
	
    @Autowired
    TradeRepository tradeRepository;

	@Override
	public boolean validate(Trade trade) {
        if(isValidMaturityDate(trade)) {
            Optional<List<Trade>> exsitingTrades = tradeRepository.findById(trade.getTradeId());
            
            if(exsitingTrades.isPresent() && !exsitingTrades.get().isEmpty()) {
            	Trade tradeWithLatestVersion = exsitingTrades.get().stream().reduce(Trade::maxVersion).get();
            	return isValidVersion(trade, tradeWithLatestVersion);
            } else {
            	return true;
            }            
         }
         return false;
	}
	
    private boolean isValidVersion(Trade trade,Trade existingTrade) {
        if(trade.getVersion() >= existingTrade.getVersion()){
            return true;
        }
        return false;
    }
    
    public boolean isValidMaturityDate(Trade trade){
         if(trade.getMaturityDate().isBefore(LocalDate.now())) { 
        	 return false;
         }
         
       return true;
    }

}
