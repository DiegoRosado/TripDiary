package com.anywherelabs.tripDiary.util.location;

import com.google.android.maps.GeoPoint;

import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class LocationUtils {
    
    private static final String CLASS_NAME = LocationUtils.class.toString();
    
    public static String toString(Location location) {
        try {
            if (location!=null) {
                StringBuilder stringBuilder = new StringBuilder(location.getLongitude()+"");
                stringBuilder.append(",").append(location.getLatitude()+"");
                if (location.getAltitude()!=Double.NaN) {
                    stringBuilder.append(",").append(location.getAltitude()+"");
                }
                return stringBuilder.toString();
            } else {
                return null;
            }
        } catch (Exception e) {
            Log.e(CLASS_NAME, "Unkown exception");
            return null;
        }
    }
    
    public static Location toLocation(String string) {
        Location location = null;
        try {
            // Check not null
            if (string!=null) {
                string = string.trim();
                // Check not empty
                if (string.length()>0) {
                    String[] split = string.split(",");
                    // Check valid format
                    if (split.length>1) {
                        double longitude = Double.parseDouble(split[0]);
                        double latitude = Double.parseDouble(split[1]);
                        double altitude = Double.NaN;
                        if (split.length>2) {
                            altitude = Double.parseDouble(split[2]);
                        }
                        location = new Location(LocationManager.GPS_PROVIDER);
                        setValues(location, longitude, latitude, altitude);
                    } else {
                        String message = "The string argument in LocationUtils.toLocation(String string) method, did not have a valid format. string = " + string;
                        Log.e(LocationUtils.class.toString(), message);
                        //throw new IllegalArgumentException(message);
                    }
                } else {
                    Log.w(LocationUtils.class.toString(), "Location string.trim() is empty at LocationUtils.toLocation(String string) method.");
                }
            } else {
                Log.w(LocationUtils.class.toString(), "Location string is null at LocationUtils.toLocation(String string) method.");
            }
            
        } catch (NumberFormatException e) {
            Log.e(LocationUtils.class.toString(), "NumberFormatException in LocationUtils.toLocation(String string) method, with argument string equals to " + string);
        } catch (Exception e) {
            Log.e(LocationUtils.class.toString(), "Unknown Exception. With string equals to " + string);
        }
        return location;
    }
    
    public static Location setValues(Location location, double longitude, 
            double latitude, double altitude) {
        location.setLongitude(longitude);
        location.setLatitude(latitude);
        location.setAltitude(altitude);
        return location;
    }

    public static Location setValues(Location location, double longitude, double latitude) {
        setValues(location, longitude, latitude, Double.NaN);
        return location;
    }

    public static GeoPoint toGeoPoint(Location location) {
        double longitude = location.getLongitude() * 1E6;
        double latitude = location.getLatitude() * 1E6;
        GeoPoint geoPoint = new GeoPoint((int)latitude, (int)longitude);
        return geoPoint;
    }
    
    public static Location fromGeoPoint(GeoPoint geoPoint) {
        double longitude = geoPoint.getLongitudeE6()/(double)1000000;
        double latitude = geoPoint.getLatitudeE6()/(double)1000000;
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLongitude(longitude);
        location.setLatitude(latitude);
        return location;
    }
}
