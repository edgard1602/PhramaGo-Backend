package com.pharmago.backend.config;

import java.util.Map;

public final class VillesConfig {

    private VillesConfig() {}

    public record VilleConfig(
            String nom,
            Double latitude,
            Double longitude,
            Integer rayon
    ) {}

    public static final Map<String, VilleConfig> VILLES = Map.of(
            "Lomé",     new VilleConfig("Lomé",     6.1375, 1.2123, 15000),
            "Kpalimé",  new VilleConfig("Kpalimé",  6.8991, 0.6241, 5000),
            "Atakpamé", new VilleConfig("Atakpamé", 7.5333, 1.1333, 5000)
    );
}