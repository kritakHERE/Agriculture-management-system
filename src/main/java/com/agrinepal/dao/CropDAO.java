package com.agrinepal.dao;

import com.agrinepal.model.Crop;

public class CropDAO extends FileBasedDAO<Crop> {
    public CropDAO() {
        super("data/crops.dat");
    }
}
