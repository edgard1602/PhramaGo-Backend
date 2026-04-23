package com.pharmago.backend.util;

public final class GeoUtils {

    private static final int EARTH_RADIUS_KM = 6371;

    // Classe utilitaire → pas d'instanciation
    private GeoUtils() {}

    /**
     * Calcule la distance en kilomètres entre deux points GPS
     * en utilisant la formule de Haversine
     */
    public static double calculerDistance(
            double lat1, double lon1,
            double lat2, double lon2) {

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

    /**
     * Arrondit la distance à 2 décimales
     * Ex: 2.3456 → 2.35
     */
    public static double arrondir(double distance) {
        return Math.round(distance * 100.0) / 100.0;
    }
}