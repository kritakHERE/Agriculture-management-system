package com.agrinepal.util;

import com.agrinepal.model.Season;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class NepaliSeasonUtil {
    private static final Map<Season, List<String>> MAPPING = new EnumMap<>(Season.class);

    static {
        MAPPING.put(Season.BASANTA, List.of("RICE", "VEGETABLES"));
        MAPPING.put(Season.GRISHMA, List.of("MAIZE", "VEGETABLES"));
        MAPPING.put(Season.BARSHA, List.of("RICE"));
        MAPPING.put(Season.SHARAD, List.of("WHEAT", "MUSTARD"));
        MAPPING.put(Season.HEMANTA, List.of("WHEAT", "MUSTARD"));
        MAPPING.put(Season.SHISHIR, List.of("VEGETABLES", "WHEAT"));
    }

    private NepaliSeasonUtil() {
    }

    public static boolean isCropSuitable(Season season, String cropName) {
        List<String> allowed = MAPPING.getOrDefault(season, List.of());
        return allowed.stream().anyMatch(c -> c.equalsIgnoreCase(cropName));
    }

    public static List<String> recommendedCrops(Season season) {
        return MAPPING.getOrDefault(season, List.of());
    }
}
