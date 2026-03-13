package com.agrinepal.dto;

import com.agrinepal.model.District;

import java.io.Serializable;
import java.util.Map;

public class DashboardDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int totalFarmers;
    private final int totalPlots;
    private final int activeCropPlans;
    private final Map<String, Long> cropDistribution;
    private final Map<District, Long> districtDistribution;
    private final String seasonSummary;

    public DashboardDTO(int totalFarmers, int totalPlots, int activeCropPlans,
            Map<String, Long> cropDistribution, Map<District, Long> districtDistribution,
            String seasonSummary) {
        this.totalFarmers = totalFarmers;
        this.totalPlots = totalPlots;
        this.activeCropPlans = activeCropPlans;
        this.cropDistribution = cropDistribution;
        this.districtDistribution = districtDistribution;
        this.seasonSummary = seasonSummary;
    }

    public int getTotalFarmers() {
        return totalFarmers;
    }

    public int getTotalPlots() {
        return totalPlots;
    }

    public int getActiveCropPlans() {
        return activeCropPlans;
    }

    public Map<String, Long> getCropDistribution() {
        return cropDistribution;
    }

    public Map<District, Long> getDistrictDistribution() {
        return districtDistribution;
    }

    public String getSeasonSummary() {
        return seasonSummary;
    }
}
