package com.agrinepal.model;

public class FarmPlot implements Identifiable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String farmerId;
    private String locationNote;
    private double size;
    private String unit;
    private SoilType soilType;
    private boolean irrigated;

    public FarmPlot(String id, String farmerId, String locationNote, double size, String unit, SoilType soilType,
            boolean irrigated) {
        this.id = id;
        this.farmerId = farmerId;
        this.locationNote = locationNote;
        this.size = size;
        this.unit = unit;
        this.soilType = soilType;
        this.irrigated = irrigated;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(String farmerId) {
        this.farmerId = farmerId;
    }

    public String getLocationNote() {
        return locationNote;
    }

    public void setLocationNote(String locationNote) {
        this.locationNote = locationNote;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public SoilType getSoilType() {
        return soilType;
    }

    public void setSoilType(SoilType soilType) {
        this.soilType = soilType;
    }

    public boolean isIrrigated() {
        return irrigated;
    }

    public void setIrrigated(boolean irrigated) {
        this.irrigated = irrigated;
    }

    @Override
    public String toString() {
        return id + " (" + size + " " + unit + ")";
    }
}
