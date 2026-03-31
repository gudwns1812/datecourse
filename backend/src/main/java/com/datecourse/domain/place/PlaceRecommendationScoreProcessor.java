package com.datecourse.domain.place;

import com.datecourse.storage.entity.Place;
import java.util.Locale;
import org.springframework.stereotype.Component;

@Component
public class PlaceRecommendationScoreProcessor {

    public double calculate(Place place, String query, double distanceMeters, double radiusMeters) {
        double textScore = calculateTextScore(place, query);
        double distanceScore = calculateDistanceScore(distanceMeters, radiusMeters);
        return textScore + distanceScore;
    }

    private double calculateTextScore(Place place, String query) {
        if (query == null || query.isBlank()) {
            return 0d;
        }

        double score = 0d;
        if (containsIgnoreCase(place.getName(), query)) {
            score += 100d;
        }
        if (containsIgnoreCase(place.getCategory(), query)) {
            score += 60d;
        }
        return score;
    }

    private double calculateDistanceScore(double distanceMeters, double radiusMeters) {
        if (radiusMeters <= 0d) {
            return 0d;
        }

        double normalized = 1d - (distanceMeters / radiusMeters);
        return Math.max(0d, normalized * 100d);
    }

    private boolean containsIgnoreCase(String source, String query) {
        if (source == null || query == null) {
            return false;
        }

        return source.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT));
    }
}
