package com.anywherelabs.tripDiary;

import com.anywherelabs.tripDiary.data.TripPointBean;
import com.anywherelabs.tripDiary.util.activity.ActivityUtils;
import com.anywherelabs.tripDiary.util.geomap.MyItemizedOverlay;
import com.anywherelabs.tripDiary.util.location.LocationUtils;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class ShowLocation extends MapActivity {

    // Constants
    private static final String CLASS_NAME = ShowLocation.class.toString();
    
    // Attributes
    private TripPointBean tripPoint;
    private MapView mapView;
    
    // Methods
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(CLASS_NAME,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_location);

        init(savedInstanceState);
        Log.d(CLASS_NAME,"Init done");
        
        updateView();
    }
    
    private void init(Bundle savedInstanceState) {
        mapView = (MapView) findViewById(R.id.showLocation_MapView);
        mapView.setBuiltInZoomControls(true);
        
        tripPoint = ActivityUtils.recoverTripPoint(savedInstanceState, this);
        assert tripPoint!=null : "TripPoint can NOT be null";
    }
    
    private void updateView() {
        Location location = tripPoint.getLocation();
        if (location!=null) {
            mapView.getController().setCenter(LocationUtils.toGeoPoint(location));
            addLocationMarker(location);
            mapView.invalidate();
        }
    }

    private void addLocationMarker(Location location) {
        Drawable icon = getResources().getDrawable(R.drawable.pushpin);
        MyItemizedOverlay mapOverlay = new MyItemizedOverlay(icon);
        OverlayItem overlayItem = new OverlayItem(LocationUtils.toGeoPoint(location), "title", "snippet");
        mapOverlay.addItem(overlayItem);
        setOverlay(mapOverlay);
    }
    
    private void setOverlay(Overlay mapOverlay) {
        List<Overlay> listOfOverlays = mapView.getOverlays();
        listOfOverlays.clear();
        listOfOverlays.add(mapOverlay);        
    }


    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    // Overlay class to show pins on MapView
    class MapOverlay extends com.google.android.maps.Overlay
    {
        @Override
        public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) 
        {
            super.draw(canvas, mapView, shadow);                   
 
            //---translate the GeoPoint to screen pixels---
            Point screenPts = new Point();
            GeoPoint geoPoint = LocationUtils.toGeoPoint(tripPoint.getLocation());
            mapView.getProjection().toPixels(geoPoint, screenPts);
 
            //---add the marker---
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.pushpin);
            int height = bmp.getHeight();
            int width = bmp.getWidth();
            // to set the image in the right place we have to remove the height of the picture and half of the width
            canvas.drawBitmap(bmp, screenPts.x-(width/2), screenPts.y-height, null);         
            return true;
        }
    } 

}
