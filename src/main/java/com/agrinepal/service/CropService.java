package com.agrinepal.service;

import com.agrinepal.dao.CropDAO;
import com.agrinepal.model.Crop;
import com.agrinepal.model.Season;
import com.agrinepal.model.SoilType;
import com.agrinepal.util.IdGenerator;
import com.agrinepal.util.ValidationUtil;

import java.util.List;

public class CropService {
    private final CropDAO cropDAO = new CropDAO();

    public Crop create(String name, int growthDuration, String waterNeed, SoilType soilType, List<Season> seasons) {
        ValidationUtil.require(!ValidationUtil.isBlank(name), "Crop name is required.");
        ValidationUtil.require(growthDuration > 0, "Growth duration must be positive.");

        Crop crop = new Crop(IdGenerator.generate("CRP"), name.trim(), growthDuration, waterNeed.trim(), soilType,
                seasons);
        cropDAO.save(crop);
        return crop;
    }

    public void update(Crop crop) {
        cropDAO.update(crop);
    }

    public void delete(String cropId) {
        cropDAO.delete(cropId);
    }

    public List<Crop> all() {
        return cropDAO.findAll();
    }

    public void seedDefaultCrops() {
        if (!cropDAO.findAll().isEmpty()) {
            return;
        }
        create("Rice", 120, "High", SoilType.CLAY, List.of(Season.BASANTA, Season.BARSHA));
        create("Maize", 90, "Medium", SoilType.LOAMY, List.of(Season.GRISHMA));
        create("Wheat", 110, "Medium", SoilType.LOAMY, List.of(Season.SHARAD, Season.HEMANTA, Season.SHISHIR));
        create("Mustard", 95, "Low", SoilType.SANDY, List.of(Season.SHARAD, Season.HEMANTA));
        create("Vegetables", 60, "Medium", SoilType.LOAMY, List.of(Season.BASANTA, Season.GRISHMA, Season.SHISHIR));
    }
}
