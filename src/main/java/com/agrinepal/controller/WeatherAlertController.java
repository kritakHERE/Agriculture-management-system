package com.agrinepal.controller;

import com.agrinepal.model.AlertSeverity;
import com.agrinepal.model.AlertType;
import com.agrinepal.model.District;
import com.agrinepal.model.WeatherAlert;
import com.agrinepal.service.WeatherAlertService;

import java.time.LocalDate;
import java.util.List;

public class WeatherAlertController {
    private final WeatherAlertService weatherAlertService;

    public WeatherAlertController(WeatherAlertService weatherAlertService) {
        this.weatherAlertService = weatherAlertService;
    }

    public WeatherAlert create(District district, AlertSeverity severity, AlertType type,
            String message, LocalDate startDate, LocalDate endDate) {
        return weatherAlertService.create(district, severity, type, message, startDate, endDate);
    }

    public List<WeatherAlert> all() {
        return weatherAlertService.all();
    }
}
