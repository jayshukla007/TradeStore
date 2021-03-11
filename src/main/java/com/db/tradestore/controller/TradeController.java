package com.db.tradestore.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.db.tradestore.domain.Trade;
import com.db.tradestore.exceptions.InvalidTradeException;
import com.db.tradestore.service.TradeService;
import com.db.tradestore.validator.TradeValidator;

@RestController
public class TradeController {

    @Autowired
    TradeService tradeService;
    
    @Autowired
    TradeValidator tradeValidator;

    
    @PostMapping("/trade")
    public ResponseEntity<String> storeTrades(@RequestBody Trade trade){
       if(!tradeValidator.validate(trade)) {
    	   throw new InvalidTradeException("Invalid trade : "+trade.getTradeId());
       }
       tradeService.storeTrades(trade);
       return ResponseEntity.ok().build();
    }

    @GetMapping("/trade")
    public ResponseEntity<List<Trade>> findAllTrades(){
    	Optional<List<Trade>> trades = tradeService.findAllTrades();
        
        return trades.isPresent() && !trades.get().isEmpty()
                ? ResponseEntity.ok(trades.get())
                : ResponseEntity.notFound().build();
    }
}
