package com.agrinepal.controller;

import com.agrinepal.model.District;
import com.agrinepal.model.Farmer;
import com.agrinepal.service.FarmerService;

import java.util.List;

public class FarmerController {
    private final FarmerService farmerService;

    public FarmerController(FarmerService farmerService) {
        this.farmerService = farmerService;
    }

    public Farmer create(String name, District district, String primaryCrop, boolean active, String phone) {
        return farmerService.create(name, district, primaryCrop, active, phone);
    }

    public List<Farmer> all() {
        return farmerService.all();
    }
}
