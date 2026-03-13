package com.agrinepal.service;

import com.agrinepal.dao.MarketPriceDAO;
import com.agrinepal.model.District;
import com.agrinepal.model.MarketPrice;
import com.agrinepal.util.IdGenerator;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MarketPriceService {
    private final MarketPriceDAO marketPriceDAO = new MarketPriceDAO();

    public MarketPrice create(String cropId, District district, LocalDate priceDate, double pricePerKg) {
        MarketPrice price = new MarketPrice(IdGenerator.generate("MKT"), cropId, district, priceDate, pricePerKg);
        marketPriceDAO.save(price);
        return price;
    }

    public List<MarketPrice> all() {
        return marketPriceDAO.findAll();
    }

    public List<MarketPrice> historyByCrop(String cropId) {
        return marketPriceDAO.findAll().stream()
                .filter(p -> p.getCropId().equals(cropId))
                .sorted(Comparator.comparing(MarketPrice::getPriceDate).reversed())
                .collect(Collectors.toList());
    }

    public String compareDistricts(String cropId) {
        List<MarketPrice> latest = marketPriceDAO.findAll().stream()
                .filter(p -> p.getCropId().equals(cropId))
                .collect(Collectors.groupingBy(MarketPrice::getDistrict,
                        Collectors.maxBy(Comparator.comparing(MarketPrice::getPriceDate))))
                .values().stream()
                .flatMap(Optional -> Optional.stream())
                .toList();

        if (latest.isEmpty()) {
            return "No price data available.";
        }
        StringBuilder sb = new StringBuilder();
        latest.forEach(p -> sb.append(p.getDistrict()).append(": ").append(p.getPricePerKg()).append("/kg\n"));
        return sb.toString();
    }
}
