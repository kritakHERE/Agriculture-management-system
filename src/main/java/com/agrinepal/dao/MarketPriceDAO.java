package com.agrinepal.dao;

import com.agrinepal.model.MarketPrice;

public class MarketPriceDAO extends FileBasedDAO<MarketPrice> {
    public MarketPriceDAO() {
        super("data/marketprices.dat");
    }
}
