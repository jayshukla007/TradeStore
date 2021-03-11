package com.db.tradestore;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.db.tradestore.domain.Trade;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TradestoreApplication.class)
public class TradeStoreEndpointTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ObjectMapper mapper;
        
    @Test
    public void returnHttpStatusNotAcceptableIfTradeIsNotValid() throws JsonProcessingException {
        
    	Trade trade = new Trade();
    	trade.setTradeId("T2");
    	trade.setMaturityDate(LocalDate.of(2019, 04, 19));
    	trade.setVersion(1);
    	    	
    	
        HttpEntity<String> entity = getStringHttpEntity(trade);

        ResponseEntity<String> response = restTemplate.postForEntity("/trade", entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
    }

    @Test
    public void shouldStoreValidTrade() throws JsonProcessingException {
        
    	Trade trade = new Trade();
    	trade.setTradeId("T2");
    	trade.setMaturityDate(LocalDate.now());
    	trade.setVersion(1);
    	
        HttpEntity<String> entity = getStringHttpEntity(trade);

        ResponseEntity<String> response = restTemplate.postForEntity("/trade", entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    
    @Test
    public void shouldReturnNotFoundIfNoTradesArePresent() throws JsonProcessingException {

        ResponseEntity<String> response = restTemplate.getForEntity("/trade", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    
    @Test
    public void shouldReturnValidTrades() throws JsonProcessingException {
    	Trade trade = new Trade();
    	trade.setTradeId("T2");
    	trade.setMaturityDate(LocalDate.now());
    	trade.setVersion(1);
    	
        HttpEntity<String> entity = getStringHttpEntity(trade);
        restTemplate.postForEntity("/trade", entity, String.class);

        ResponseEntity<String> response = restTemplate.getForEntity("/trade", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    private HttpEntity<String> getStringHttpEntity(Object object) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonMeterData = mapper.writeValueAsString(object);
        return (HttpEntity<String>) new HttpEntity(jsonMeterData, headers);
    }

}

