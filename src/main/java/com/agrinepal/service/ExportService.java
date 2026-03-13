package com.agrinepal.service;

import com.agrinepal.dao.CropDAO;
import com.agrinepal.dao.FarmerDAO;
import com.agrinepal.dao.MarketPriceDAO;
import com.agrinepal.model.Crop;
import com.agrinepal.model.Farmer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

public class ExportService {
    private final FarmerDAO farmerDAO = new FarmerDAO();
    private final MarketPriceDAO marketPriceDAO = new MarketPriceDAO();
    private final CropDAO cropDAO = new CropDAO();

    public Path exportFarmers(Path outputFile) {
        String header = "FarmerID,Name,District,PrimaryCrop,Status,Phone,RegisteredDate\n";
        String body = farmerDAO.findAll().stream()
                .map(this::toFarmerLine)
                .collect(Collectors.joining("\n"));
        return writeText(outputFile, header + body);
    }

    public Path exportMarketPrices(Path outputFile) {
        Map<String, String> cropMap = cropDAO.findAll().stream()
                .collect(Collectors.toMap(Crop::getId, Crop::getName, (a, b) -> a));
        String header = "PriceID,Crop,District,Date,PricePerKg\n";
        DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE;
        String body = marketPriceDAO.findAll().stream()
                .map(p -> p.getId() + "," + cropMap.getOrDefault(p.getCropId(), "Unknown") + "," + p.getDistrict() + ","
                        + p.getPriceDate().format(fmt) + "," + p.getPricePerKg())
                .collect(Collectors.joining("\n"));
        return writeText(outputFile, header + body);
    }

    private String toFarmerLine(Farmer f) {
        return f.getId() + "," + f.getName() + "," + f.getDistrict() + "," + f.getPrimaryCrop() + ","
                + (f.isActive() ? "ACTIVE" : "INACTIVE") + "," + f.getPhone() + "," + f.getRegisteredDate();
    }

    private Path writeText(Path path, String content) {
        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            Files.writeString(path, content);
            return path;
        } catch (IOException e) {
            throw new RuntimeException("Failed to export file: " + path, e);
        }
    }
}
