package com.agrinepal.service;

import com.agrinepal.dao.CropDAO;
import com.agrinepal.dao.CropPlanDAO;
import com.agrinepal.dao.FarmPlotDAO;
import com.agrinepal.dao.FarmerDAO;
import com.agrinepal.dto.CropAdvisoryDTO;
import com.agrinepal.model.Crop;
import com.agrinepal.model.CropPlan;
import com.agrinepal.model.FarmPlot;
import com.agrinepal.model.Season;
import com.agrinepal.util.IdGenerator;
import com.agrinepal.util.NepaliSeasonUtil;
import com.agrinepal.util.ValidationUtil;

import java.time.LocalDate;
import java.util.List;

public class CropPlanService {
    private final CropPlanDAO cropPlanDAO = new CropPlanDAO();
    private final CropDAO cropDAO = new CropDAO();
    private final FarmerDAO farmerDAO = new FarmerDAO();
    private final FarmPlotDAO farmPlotDAO = new FarmPlotDAO();

    public CropPlan create(String farmerId, String plotId, String cropId, Season season, LocalDate plantingDate) {
        ValidationUtil.require(farmerDAO.findById(farmerId).isPresent(), "Farmer not found.");
        ValidationUtil.require(farmPlotDAO.findById(plotId).isPresent(), "Plot not found.");
        Crop crop = cropDAO.findById(cropId).orElseThrow(() -> new IllegalArgumentException("Crop not found."));

        boolean suitable = crop.getSuitableSeasons().contains(season)
                || NepaliSeasonUtil.isCropSuitable(season, crop.getName());
        String note = suitable ? "Season-crop combination is suitable." : "Warning: crop-season mismatch detected.";

        CropPlan plan = new CropPlan(
                IdGenerator.generate("PLN"),
                farmerId,
                plotId,
                cropId,
                season,
                plantingDate,
                plantingDate.plusDays(crop.getGrowthDurationDays()),
                true,
                note);
        cropPlanDAO.save(plan);
        return plan;
    }

    public List<CropPlan> all() {
        return cropPlanDAO.findAll();
    }

    public void delete(String planId) {
        cropPlanDAO.delete(planId);
    }

    public CropAdvisoryDTO advisory(Season season, String cropId, String plotId) {
        Crop crop = cropDAO.findById(cropId).orElseThrow(() -> new IllegalArgumentException("Crop not found."));
        FarmPlot plot = farmPlotDAO.findById(plotId).orElseThrow(() -> new IllegalArgumentException("Plot not found."));

        int score = 50;
        if (crop.getSuitableSeasons().contains(season) || NepaliSeasonUtil.isCropSuitable(season, crop.getName())) {
            score += 35;
        }
        if (crop.getRecommendedSoil() == plot.getSoilType()) {
            score += 15;
        }
        score = Math.min(score, 100);

        double unitMultiplier = "BIGHA".equalsIgnoreCase(plot.getUnit()) ? 13.5 : 1.0;
        double estimatedYield = plot.getSize() * unitMultiplier * (score / 100.0) * 100.0;
        String message = score >= 75 ? "Recommended" : "Possible but with risks";

        return new CropAdvisoryDTO(
                season,
                crop.getName(),
                score,
                NepaliSeasonUtil.recommendedCrops(season),
                estimatedYield,
                message);
    }
}
