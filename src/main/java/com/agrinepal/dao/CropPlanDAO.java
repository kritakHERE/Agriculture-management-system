package com.agrinepal.dao;

import com.agrinepal.model.CropPlan;

public class CropPlanDAO extends FileBasedDAO<CropPlan> {
    public CropPlanDAO() {
        super("data/cropplans.dat");
    }
}
