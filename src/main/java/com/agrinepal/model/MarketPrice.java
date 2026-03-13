package com.agrinepal.model;

import java.time.LocalDate;

public class MarketPrice implements Identifiable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String cropId;
    private District district;
    private LocalDate priceDate;
    private double pricePerKg;

    public MarketPrice(String id, String cropId, District district, LocalDate priceDate, double pricePerKg) {
        this.id = id;
        this.cropId = cropId;
        this.district = district;
        this.priceDate = priceDate;
        this.pricePerKg = pricePerKg;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getCropId() {
        return cropId;
    }

    public District getDistrict() {
        return district;
    }

    public LocalDate getPriceDate() {
        return priceDate;
    }

    public double getPricePerKg() {
        return pricePerKg;
    }

    public void setPricePerKg(double pricePerKg) {
        this.pricePerKg = pricePerKg;
    }
}
