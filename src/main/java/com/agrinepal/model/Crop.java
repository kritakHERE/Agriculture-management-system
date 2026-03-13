package com.agrinepal.model;

import java.util.ArrayList;
import java.util.List;

public class Crop implements Identifiable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private int growthDurationDays;
    private String waterNeed;
    private SoilType recommendedSoil;
    private List<Season> suitableSeasons;

    public Crop(String id, String name, int growthDurationDays, String waterNeed, SoilType recommendedSoil,
            List<Season> suitableSeasons) {
        this.id = id;
        this.name = name;
        this.growthDurationDays = growthDurationDays;
        this.waterNeed = waterNeed;
        this.recommendedSoil = recommendedSoil;
        this.suitableSeasons = new ArrayList<>(suitableSeasons);
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getGrowthDurationDays() {
        return growthDurationDays;
    }

    public void setGrowthDurationDays(int growthDurationDays) {
        this.growthDurationDays = growthDurationDays;
    }

    public String getWaterNeed() {
        return waterNeed;
    }

    public void setWaterNeed(String waterNeed) {
        this.waterNeed = waterNeed;
    }

    public SoilType getRecommendedSoil() {
        return recommendedSoil;
    }

    public void setRecommendedSoil(SoilType recommendedSoil) {
        this.recommendedSoil = recommendedSoil;
    }

    public List<Season> getSuitableSeasons() {
        return new ArrayList<>(suitableSeasons);
    }

    public void setSuitableSeasons(List<Season> suitableSeasons) {
        this.suitableSeasons = new ArrayList<>(suitableSeasons);
    }

    @Override
    public String toString() {
        return name;
    }
}
