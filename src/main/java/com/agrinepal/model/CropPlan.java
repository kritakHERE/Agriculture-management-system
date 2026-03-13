package com.agrinepal.model;

import java.time.LocalDate;

public class CropPlan implements Identifiable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String farmerId;
    private String plotId;
    private String cropId;
    private Season season;
    private LocalDate plantingDate;
    private LocalDate harvestDate;
    private boolean active;
    private String advisoryNote;

    public CropPlan(String id, String farmerId, String plotId, String cropId, Season season, LocalDate plantingDate,
            LocalDate harvestDate, boolean active, String advisoryNote) {
        this.id = id;
        this.farmerId = farmerId;
        this.plotId = plotId;
        this.cropId = cropId;
        this.season = season;
        this.plantingDate = plantingDate;
        this.harvestDate = harvestDate;
        this.active = active;
        this.advisoryNote = advisoryNote;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getFarmerId() {
        return farmerId;
    }

    public String getPlotId() {
        return plotId;
    }

    public String getCropId() {
        return cropId;
    }

    public Season getSeason() {
        return season;
    }

    public LocalDate getPlantingDate() {
        return plantingDate;
    }

    public LocalDate getHarvestDate() {
        return harvestDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getAdvisoryNote() {
        return advisoryNote;
    }

    public void setAdvisoryNote(String advisoryNote) {
        this.advisoryNote = advisoryNote;
    }
}
