package com.db.tradestore.cron;

import org.awaitility.Duration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import static org.awaitility.Awaitility.await;
import com.db.tradestore.TradestoreApplication;
import com.db.tradestore.cron.TradeStatusUpdateScheduler;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;


@SpringJUnitConfig(TradestoreApplication.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TradeStausUpdateSchedulerTest {
	
    @SpyBean
    private TradeStatusUpdateScheduler tradeStatusUpdateScheduler;

    @Test
    public void shouldGetCalledTwiceInOneMinute() {
    	await()
        .atMost(Duration.ONE_MINUTE)
        .untilAsserted(() -> verify(tradeStatusUpdateScheduler, atLeast(1)).updateTradeStatus());
    }
}
