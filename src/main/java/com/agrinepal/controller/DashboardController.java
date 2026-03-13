package com.agrinepal.controller;

import com.agrinepal.dto.DashboardDTO;
import com.agrinepal.service.DashboardService;

public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    public DashboardDTO getData() {
        return dashboardService.getDashboard();
    }
}
