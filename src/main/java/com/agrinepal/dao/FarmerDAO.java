package com.agrinepal.dao;

import com.agrinepal.model.Farmer;

public class FarmerDAO extends FileBasedDAO<Farmer> {
    public FarmerDAO() {
        super("data/farmers.dat");
    }
}
