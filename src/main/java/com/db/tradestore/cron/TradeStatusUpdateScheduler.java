package com.db.tradestore.cron;

import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.db.tradestore.service.TradeService;

@Component
public class TradeStatusUpdateScheduler {
	
	@Autowired
	TradeService tradeService;

    @Scheduled(cron = "*/40 * * * * *")
    public void updateTradeStatus() {

      tradeService.updateTradeStatus();
      
      System.out.println("Triggered cron at :: " + Instant.now());
    }
}
