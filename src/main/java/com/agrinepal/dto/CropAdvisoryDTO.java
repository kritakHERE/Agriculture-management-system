package com.agrinepal.dto;

import com.agrinepal.model.Season;

import java.io.Serializable;
import java.util.List;

public class CropAdvisoryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Season season;
    private final String cropName;
    private final int suitabilityScore;
    private final List<String> recommendedCrops;
    private final double estimatedYield;
    private final String advisoryMessage;

    public CropAdvisoryDTO(Season season, String cropName, int suitabilityScore,
            List<String> recommendedCrops, double estimatedYield, String advisoryMessage) {
        this.season = season;
        this.cropName = cropName;
        this.suitabilityScore = suitabilityScore;
        this.recommendedCrops = recommendedCrops;
        this.estimatedYield = estimatedYield;
        this.advisoryMessage = advisoryMessage;
    }

    public Season getSeason() {
        return season;
    }

    public String getCropName() {
        return cropName;
    }

    public int getSuitabilityScore() {
        return suitabilityScore;
    }

    public List<String> getRecommendedCrops() {
        return recommendedCrops;
    }

    public double getEstimatedYield() {
        return estimatedYield;
    }

    public String getAdvisoryMessage() {
        return advisoryMessage;
    }
}
