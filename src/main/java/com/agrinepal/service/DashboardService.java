package com.agrinepal.service;

import com.agrinepal.dao.CropDAO;
import com.agrinepal.dao.CropPlanDAO;
import com.agrinepal.dao.FarmPlotDAO;
import com.agrinepal.dao.FarmerDAO;
import com.agrinepal.dto.DashboardDTO;
import com.agrinepal.model.Crop;
import com.agrinepal.model.CropPlan;
import com.agrinepal.model.District;
import com.agrinepal.model.Season;
import com.agrinepal.util.NepaliSeasonUtil;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DashboardService {
    private final FarmerDAO farmerDAO = new FarmerDAO();
    private final FarmPlotDAO farmPlotDAO = new FarmPlotDAO();
    private final CropPlanDAO cropPlanDAO = new CropPlanDAO();
    private final CropDAO cropDAO = new CropDAO();

    public DashboardDTO getDashboard() {
        List<Crop> crops = cropDAO.findAll();
        Map<String, Crop> cropMap = crops.stream()
                .collect(Collectors.toMap(Crop::getId, Function.identity(), (a, b) -> a));

        Map<String, Long> cropDistribution = cropPlanDAO.findAll().stream()
                .collect(Collectors.groupingBy(p -> {
                    Crop c = cropMap.get(p.getCropId());
                    return c == null ? "Unknown" : c.getName();
                }, Collectors.counting()));

        Map<District, Long> districtDistribution = farmerDAO.findAll().stream()
                .collect(Collectors.groupingBy(f -> f.getDistrict(), Collectors.counting()));

        Season currentSeason = Season.values()[(java.time.LocalDate.now().getMonthValue() % 6)];
        String summary = "Current season: " + currentSeason + ". Recommended crops: "
                + String.join(", ", NepaliSeasonUtil.recommendedCrops(currentSeason));

        return new DashboardDTO(
                farmerDAO.findAll().size(),
                farmPlotDAO.findAll().size(),
                (int) cropPlanDAO.findAll().stream().filter(CropPlan::isActive).count(),
                cropDistribution,
                districtDistribution,
                summary);
    }
}
