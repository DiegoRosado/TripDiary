package com.anywherelabs.tripDiary.util.geomap;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

import android.view.MotionEvent;

public class MapUtils {

    
    // Constructors
    private MapUtils() {
        // Utils class
    }
    
    // Methods
    public static GeoPoint getGeoPoint(MapView mapView, MotionEvent event) {
        GeoPoint geoPoint = mapView.getProjection().fromPixels(
                (int) event.getX(),
                (int) event.getY());
        return geoPoint;
    }
}
