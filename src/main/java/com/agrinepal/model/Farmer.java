package com.agrinepal.model;

import java.time.LocalDate;

public class Farmer implements Identifiable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private District district;
    private String primaryCrop;
    private boolean active;
    private String phone;
    private LocalDate registeredDate;

    public Farmer(String id, String name, District district, String primaryCrop, boolean active, String phone,
            LocalDate registeredDate) {
        this.id = id;
        this.name = name;
        this.district = district;
        this.primaryCrop = primaryCrop;
        this.active = active;
        this.phone = phone;
        this.registeredDate = registeredDate;
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

    public void setName(String name) {
        this.name = name;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public String getPrimaryCrop() {
        return primaryCrop;
    }

    public void setPrimaryCrop(String primaryCrop) {
        this.primaryCrop = primaryCrop;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(LocalDate registeredDate) {
        this.registeredDate = registeredDate;
    }

    @Override
    public String toString() {
        return name + " - " + district;
    }
}
