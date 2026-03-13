package com.agrinepal.controller;

import com.agrinepal.model.District;
import com.agrinepal.model.MarketPrice;
import com.agrinepal.service.MarketPriceService;

import java.time.LocalDate;
import java.util.List;

public class MarketPriceController {
    private final MarketPriceService marketPriceService;

    public MarketPriceController(MarketPriceService marketPriceService) {
        this.marketPriceService = marketPriceService;
    }

    public MarketPrice create(String cropId, District district, LocalDate date, double pricePerKg) {
        return marketPriceService.create(cropId, district, date, pricePerKg);
    }

    public List<MarketPrice> all() {
        return marketPriceService.all();
    }
}
