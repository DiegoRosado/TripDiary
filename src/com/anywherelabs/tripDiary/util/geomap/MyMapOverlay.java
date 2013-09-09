package com.anywherelabs.tripDiary.util.geomap;

import com.anywherelabs.tripDiary.R;
import com.anywherelabs.tripDiary.util.location.LocationUtils;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;

public class MyMapOverlay extends com.google.android.maps.Overlay {
    @Override
    public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) 
    {
        super.draw(canvas, mapView, shadow);                   

//        //---translate the GeoPoint to screen pixels---
//        Point screenPts = new Point();
//        GeoPoint geoPoint = LocationUtils.toGeoPoint(tripPoint.getLocation());
//        mapView.getProjection().toPixels(geoPoint, screenPts);
//
//        //---add the marker---
//        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.pushpin);
//        int height = bmp.getHeight();
//        int width = bmp.getWidth();
//        // to set the image in the right place we have to remove the height of the picture and half of the width
//        canvas.drawBitmap(bmp, screenPts.x-(width/2), screenPts.y-height, null);         
        return true;
    }
} 

