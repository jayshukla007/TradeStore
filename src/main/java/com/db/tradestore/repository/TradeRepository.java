package com.db.tradestore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.db.tradestore.domain.Trade;

public interface TradeRepository extends CrudRepository<Trade, Integer> {
    @Override
    List<Trade> findAll();

    @Query("select t from Trade t where t.tradeId like %?1")
	Optional<List<Trade>> findById(String tradeId);

}
