package com.anywherelabs.tripDiary;

import com.anywherelabs.tripDiary.data.TripPointBean;
import com.anywherelabs.tripDiary.database.DbAdapter;
import com.anywherelabs.tripDiary.util.activity.ActivityUtils;
import com.anywherelabs.tripDiary.util.geomap.MyItemizedOverlay;
import com.anywherelabs.tripDiary.util.geomap.TouchEventListener;
import com.anywherelabs.tripDiary.util.location.LocationUtils;
import com.anywherelabs.tripDiary.util.location.MyLocationListener;
import com.anywherelabs.util.dialog.DialogUtils;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.List;

public class SetChangeLocation extends MapActivity implements TouchEventListener {

    // Constants
    private static final String CLASS_NAME = SetChangeLocation.class.toString();
    private static final int GPS_TIMEOUT = Constants.DEFAULT_GPS_TIMEOUT;
    private static final int GPS_READ_EVENT = Constants.GPS_READ_EVENT;

    // Attributes
    private TripPointBean tripPoint;
    private MapView mapView;
    private ImageView pushpinView;
    private boolean pushpinButtonPressed;
    private EditText gpsLocationText;
    private MyLocationListener locationListener;
    private Location location;
    private Location lastKnownLocation;
    private MyItemizedOverlay mapOverlay;
    private DbAdapter dbAdapter;
    
    // Methods
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(CLASS_NAME,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_change_location);

        init(savedInstanceState);
        Log.d(CLASS_NAME,"Init done");
        
        updateView();
    }
    
    private void init(Bundle savedInstanceState) {
        dbAdapter = new DbAdapter(this);
        mapView = (MapView) findViewById(R.id.setChangeLocation_MapView);
        mapView.setBuiltInZoomControls(true);
        gpsLocationText = (EditText) findViewById(R.id.setChangeLocation_GpsText);
        pushpinView = (ImageView) findViewById(R.id.setChangeLocation_PushPinButton);
        pushpinButtonPressed = false;
        
        tripPoint = ActivityUtils.recoverTripPoint(savedInstanceState, this);
        assert tripPoint!=null : "TripPoint can NOT be null";
        location = tripPoint.getLocation();
        
        initItemizedOverlay();
        
        final Handler handler = new LocationHandler();
        locationListener = new MyLocationListener(this, handler, GPS_TIMEOUT);
    }
    
    private void initItemizedOverlay() {
        Drawable icon = getResources().getDrawable(R.drawable.pushpin);
        mapOverlay = new MyItemizedOverlay(icon);
        setOverlay(mapOverlay);
    }
    
    private void updateView() {
        if (location!=null) {
            gpsLocationText.setText(LocationUtils.toString(location));
            mapView.getController().setCenter(LocationUtils.toGeoPoint(location));
            mapOverlay.clear();
            addLocationMarker(location);
            mapView.invalidate();
        }
    }

    private void addLocationMarker(Location location) {
        OverlayItem overlayItem = new OverlayItem(LocationUtils.toGeoPoint(location), null, null);
        mapOverlay.addItem(overlayItem);
        setOverlay(mapOverlay);
    }
    
    private void setOverlay(Overlay mapOverlay) {
        List<Overlay> listOfOverlays = mapView.getOverlays();
        listOfOverlays.clear();
        listOfOverlays.add(mapOverlay);        
    }

    public void mapTouchEventLocation(GeoPoint geoPoint) {
        location = LocationUtils.fromGeoPoint(geoPoint);
        setPushpinButtonToPressed(false);
        updateView();
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    private void updateLocation(Location location) {
        this.location = location;
    }
    
    private void updateLocationView() {
        if (location!=null) {
            showLocation();
            updateView();
        } else {
            if (lastKnownLocation!=null) {
                location = lastKnownLocation;
                showLocationLastKnowLocation();
                updateView();
            } else {
                gpsLocationText.setText(R.string.location_CouldNotGetLocation);
            }
        }
    }
    
    private void showLocationLastKnowLocation() {
        String prefix = "(last known location) ";
        gpsLocationText.setText(prefix + location.getLongitude() + "," + location.getLatitude());
    }
    
    private void showLocation() {
        assert location!=null : "location can not be null";
        gpsLocationText.setText(location.getLongitude() + "," + location.getLatitude());
    }
    
    private synchronized void stopListeningLocation() {
        locationListener.stopListeningLocation();
    }
    
    private synchronized void startListeningLocation() {
        updateLastLocation();
        gpsLocationText.setText(R.string.location_SearchingLocation);
        locationListener.startListeningLocation();
    }
    
    protected void onResume() {
        super.onResume();
    }
    
    private void updateLastLocation() {
        if (location!=null) {
            lastKnownLocation = location;
        }
    }

    
    // onClick methods
    public void onClickRefreshGps(View view) {
        gpsLocationText.setText(R.string.location_SearchingLocation);
        startListeningLocation();
    }
    
    public void onClickOkButton(View view) {
        tripPoint.setLocation(location);
        dbAdapter.open();
        dbAdapter.updateTripPoint(tripPoint);
        dbAdapter.close();
        setResult(RESULT_OK);
        finish();
    }
    
    public void onClickCancelButton(View view) {
        setResult(RESULT_OK);
        finish();
    }
    
    public void onClickPushpinButton(View view) {
        setPushpinButtonToPressed(!pushpinButtonPressed);
    }
    
    private synchronized void setPushpinButtonToPressed(boolean pressed) {
        int imageResourceId = pressed ? 
                R.drawable.pushpin_button_pressed :
                R.drawable.pushpin_button_released;
        pushpinView.setImageResource(imageResourceId);
        pushpinButtonPressed = pressed;
        if (pressed) {
            mapOverlay.setTouchEventListener(this);
        } else {
            mapOverlay.setTouchEventListener(null);
        }
    }
    
    // LocationHandler class
    //-------------------------------------------------------
    private class LocationHandler extends Handler {
        
        // Methods
        @Override
        public void handleMessage(Message msg) {
            stopListeningLocation();
            if (msg.what==GPS_READ_EVENT) {
                updateLocation((Location)msg.obj);
                updateLocationView();
            }
        }
    }

}
