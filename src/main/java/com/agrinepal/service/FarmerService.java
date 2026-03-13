package com.agrinepal.service;

import com.agrinepal.dao.FarmerDAO;
import com.agrinepal.model.District;
import com.agrinepal.model.Farmer;
import com.agrinepal.util.IdGenerator;
import com.agrinepal.util.ValidationUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class FarmerService {
    private final FarmerDAO farmerDAO = new FarmerDAO();

    public Farmer create(String name, District district, String primaryCrop, boolean active, String phone) {
        ValidationUtil.require(!ValidationUtil.isBlank(name), "Farmer name is required.");
        boolean duplicate = farmerDAO.findAll().stream()
                .anyMatch(f -> f.getName().equalsIgnoreCase(name.trim()) && f.getDistrict() == district);
        ValidationUtil.require(!duplicate, "Duplicate farmer found with same name and district.");

        Farmer farmer = new Farmer(
                IdGenerator.generate("FAR"),
                name.trim(),
                district,
                primaryCrop.trim(),
                active,
                phone,
                LocalDate.now());
        farmerDAO.save(farmer);
        return farmer;
    }

    public void update(Farmer farmer) {
        farmerDAO.update(farmer);
    }

    public void delete(String farmerId) {
        farmerDAO.delete(farmerId);
    }

    public List<Farmer> all() {
        return farmerDAO.findAll();
    }

    public List<Farmer> search(String keyword, District district, String crop, Boolean active) {
        String key = keyword == null ? "" : keyword.toLowerCase(Locale.ROOT);
        String cropFilter = crop == null ? "" : crop.toLowerCase(Locale.ROOT);

        return farmerDAO.findAll().stream()
                .filter(f -> key.isBlank() || f.getName().toLowerCase(Locale.ROOT).contains(key)
                        || f.getId().toLowerCase(Locale.ROOT).contains(key))
                .filter(f -> district == null || f.getDistrict() == district)
                .filter(f -> cropFilter.isBlank() || f.getPrimaryCrop().toLowerCase(Locale.ROOT).contains(cropFilter))
                .filter(f -> active == null || f.isActive() == active)
                .collect(Collectors.toList());
    }
}
