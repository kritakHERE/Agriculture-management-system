package com.agrinepal.controller;

import com.agrinepal.dto.CropAdvisoryDTO;
import com.agrinepal.model.CropPlan;
import com.agrinepal.model.Season;
import com.agrinepal.service.CropPlanService;

import java.time.LocalDate;
import java.util.List;

public class CropPlanController {
    private final CropPlanService cropPlanService;

    public CropPlanController(CropPlanService cropPlanService) {
        this.cropPlanService = cropPlanService;
    }

    public CropPlan create(String farmerId, String plotId, String cropId, Season season, LocalDate plantingDate) {
        return cropPlanService.create(farmerId, plotId, cropId, season, plantingDate);
    }

    public CropAdvisoryDTO advisory(Season season, String cropId, String plotId) {
        return cropPlanService.advisory(season, cropId, plotId);
    }

    public List<CropPlan> all() {
        return cropPlanService.all();
    }
}
