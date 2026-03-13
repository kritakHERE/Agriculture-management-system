package com.agrinepal.model;

import java.time.LocalDate;

public class WeatherAlert implements Identifiable {
    private static final long serialVersionUID = 1L;

    private String id;
    private District district;
    private AlertSeverity severity;
    private AlertType type;
    private String message;
    private LocalDate startDate;
    private LocalDate endDate;

    public WeatherAlert(String id, District district, AlertSeverity severity, AlertType type, String message,
            LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.district = district;
        this.severity = severity;
        this.type = type;
        this.message = message;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String getId() {
        return id;
    }

    public District getDistrict() {
        return district;
    }

    public AlertSeverity getSeverity() {
        return severity;
    }

    public AlertType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public boolean isActive() {
        LocalDate now = LocalDate.now();
        return (now.isEqual(startDate) || now.isAfter(startDate)) && (now.isEqual(endDate) || now.isBefore(endDate));
    }
}
