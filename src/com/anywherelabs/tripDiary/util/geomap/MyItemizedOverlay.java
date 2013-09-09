package com.anywherelabs.tripDiary.util.geomap;

import com.anywherelabs.tripDiary.Constants;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

public class MyItemizedOverlay extends ItemizedOverlay<OverlayItem> {
    
    // Constants
    //TODO Try to implement this using a DEFAULT_MARKER loaded from resources without using context to load the resource
//    private static final Drawable DEFAULT_MARKER = Resources.load(R.drawable.pushpin); 
    private static final int RELEASE_TOUCH_SCREEN = Constants.ACTION_EVENT_RELEASE_TOUCH_SCREEN;
    private static final int PRESS_TOUCH_SCREEN = Constants.ACTION_EVENT_PRESS_TOUCH_SCREEN;
    
    // Attributes
    private List<OverlayItem> items;
    private Drawable marker;
    private TouchEventListener touchEventListener;
 
    // Constructors
    public MyItemizedOverlay(Drawable defaultMarker) {
        super(defaultMarker);
        items = new ArrayList<OverlayItem>();
        marker = defaultMarker;
    }
 
    // Methods
    @Override
    protected OverlayItem createItem(int index) {
        return items.get(index);
    }
 
    @Override
    public int size() {
        return items.size();
 
    }
 
    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
        super.draw(canvas, mapView, shadow);
        boundCenterBottom(marker);
 
    }
 
    public void addItem(OverlayItem item) {
        items.add(item);
        populate();
    }
    
    public void clear() {
        items = new ArrayList<OverlayItem>();
        populate();
    }
 
    /**
     * Set the TouchEventListener which will be notified by touchEvents. Set to null
     * if you do not want to be notified.
     * @param listener
     */
    public synchronized void setTouchEventListener(TouchEventListener listener) {
        this.touchEventListener = listener;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event, MapView mapView) {
        boolean handled = false;
        if (touchEventListener!=null) {
            if (event.getAction() == RELEASE_TOUCH_SCREEN) {     
                GeoPoint geoPoint = MapUtils.getGeoPoint(mapView, event);
                touchEventListener.mapTouchEventLocation(geoPoint);
                handled = true;
            }
        }
        //super.onTouchEvent(event, mapView);
        return handled;
    }
}