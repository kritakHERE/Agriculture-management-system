package com.agrinepal.dao;

import com.agrinepal.model.FarmPlot;

public class FarmPlotDAO extends FileBasedDAO<FarmPlot> {
    public FarmPlotDAO() {
        super("data/farmplots.dat");
    }
}
