package com.pharmago.backend.config;

import java.util.List;
import java.util.Map;

public final class VillesConfig {

    private VillesConfig() {}

    public record VilleConfig(
            String nom,
            Double latitude,
            Double longitude,
            Integer rayon
    ) {}

    // Zones par ville pour couvrir toute la ville
    public static final Map<String, List<VilleConfig>> VILLES_ZONES =
            Map.of(
                    "Lomé", List.of(
                            new VilleConfig("Lomé-Centre",  6.1375, 1.2123, 9000),
                            new VilleConfig("Lomé-Nord",    6.1700, 1.2200, 9000),
                            new VilleConfig("Lomé-Nord-Ouest", 6.225244, 1.154138,9000),
                            new VilleConfig("Lomé-Agoé1",6.237017, 1.204252,9000),
                            new VilleConfig("Lomé-caisse",6.167195, 1.228247,9000),
                            new VilleConfig("Lomé-Hejranawé",6.206567, 1.245045,9000),
                            new VilleConfig("Lomé-Est",     6.1400, 1.2500, 9000),
                            new VilleConfig("Lomé-Ouest",   6.1300, 1.1900, 9000),
                            new VilleConfig("Lomé-sagbado",6.206812, 1.114776,9000),
                            new VilleConfig("Lomé-Adakpamé",6.169973, 1.285392,9000),
                            new VilleConfig("Lomé-Baguida", 6.1100, 1.2800, 9000)
                    ),
                    "Kpalimé", List.of(
                            new VilleConfig("Kpalimé", 6.8991, 0.6241, 5000)
                    ),
                    "Atakpamé", List.of(
                            new VilleConfig("Atakpamé", 7.5333, 1.1333, 5000)
                    )
            );
}