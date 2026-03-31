package com.datecourse.support.h2;

import org.locationtech.jts.geom.Geometry;

public final class H2SpatialFunctions {

    private H2SpatialFunctions() {
    }

    public static Double stX(Geometry geometry) {
        if (geometry == null || geometry.getCoordinate() == null) {
            return null;
        }

        return geometry.getCoordinate().getX();
    }

    public static Double stY(Geometry geometry) {
        if (geometry == null || geometry.getCoordinate() == null) {
            return null;
        }

        return geometry.getCoordinate().getY();
    }
}
