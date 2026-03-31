package com.datecourse.domain.place;

import com.datecourse.storage.entity.Place;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Component;

@Component
public class PlaceSearchFilterProcessor {

    public List<Place> process(List<Place> places, PlaceSearchCommand command) {
        return places.stream()
                .filter(place -> matchesCategory(place, command.category()))
                .filter(place -> matchesQuery(place, command.query()))
                .toList();
    }

    private boolean matchesCategory(Place place, String category) {
        if (category == null || category.isBlank()) {
            return true;
        }

        return category.equals(place.getCategory());
    }

    private boolean matchesQuery(Place place, String query) {
        if (query == null || query.isBlank()) {
            return true;
        }

        return containsIgnoreCase(place.getName(), query)
                || containsIgnoreCase(place.getCategory(), query);
    }

    private boolean containsIgnoreCase(String source, String query) {
        if (source == null || query == null) {
            return false;
        }

        return source.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT));
    }
}
