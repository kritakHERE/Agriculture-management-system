package com.agrinepal.dao;

import com.agrinepal.model.WeatherAlert;

public class WeatherAlertDAO extends FileBasedDAO<WeatherAlert> {
    public WeatherAlertDAO() {
        super("data/weatheralerts.dat");
    }
}
