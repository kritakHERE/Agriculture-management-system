package com.agrinepal.service;

import com.agrinepal.dao.WeatherAlertDAO;
import com.agrinepal.model.AlertSeverity;
import com.agrinepal.model.AlertType;
import com.agrinepal.model.District;
import com.agrinepal.model.WeatherAlert;
import com.agrinepal.util.IdGenerator;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class WeatherAlertService {
    private final WeatherAlertDAO weatherAlertDAO = new WeatherAlertDAO();

    public WeatherAlert create(District district, AlertSeverity severity, AlertType type,
            String message, LocalDate startDate, LocalDate endDate) {
        WeatherAlert alert = new WeatherAlert(
                IdGenerator.generate("WTH"),
                district,
                severity,
                type,
                message,
                startDate,
                endDate);
        weatherAlertDAO.save(alert);
        return alert;
    }

    public List<WeatherAlert> all() {
        return weatherAlertDAO.findAll();
    }

    public List<WeatherAlert> activeByDistrict(District district) {
        return weatherAlertDAO.findAll().stream()
                .filter(a -> a.getDistrict() == district && a.isActive())
                .collect(Collectors.toList());
    }
}
