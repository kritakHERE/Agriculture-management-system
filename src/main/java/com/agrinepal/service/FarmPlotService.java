package com.agrinepal.service;

import com.agrinepal.dao.FarmPlotDAO;
import com.agrinepal.model.FarmPlot;
import com.agrinepal.model.SoilType;
import com.agrinepal.util.IdGenerator;
import com.agrinepal.util.ValidationUtil;

import java.util.List;
import java.util.stream.Collectors;

public class FarmPlotService {
    private final FarmPlotDAO farmPlotDAO = new FarmPlotDAO();

    public FarmPlot create(String farmerId, String location, double size, String unit, SoilType soilType,
            boolean irrigated) {
        ValidationUtil.require(!ValidationUtil.isBlank(farmerId), "Farmer ID is required.");
        ValidationUtil.require(size > 0, "Plot size must be greater than zero.");
        ValidationUtil.require("ROPANI".equalsIgnoreCase(unit) || "BIGHA".equalsIgnoreCase(unit),
                "Unit must be Ropani or Bigha.");

        FarmPlot plot = new FarmPlot(IdGenerator.generate("PLT"), farmerId, location, size, unit.toUpperCase(),
                soilType, irrigated);
        farmPlotDAO.save(plot);
        return plot;
    }

    public void update(FarmPlot plot) {
        farmPlotDAO.update(plot);
    }

    public void delete(String plotId) {
        farmPlotDAO.delete(plotId);
    }

    public List<FarmPlot> all() {
        return farmPlotDAO.findAll();
    }

    public List<FarmPlot> byFarmer(String farmerId) {
        return farmPlotDAO.findAll().stream().filter(p -> p.getFarmerId().equals(farmerId))
                .collect(Collectors.toList());
    }
}
