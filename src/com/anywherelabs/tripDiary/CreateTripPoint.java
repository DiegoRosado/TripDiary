package com.anywherelabs.tripDiary;

import com.anywherelabs.tripDiary.data.TripBean;
import com.anywherelabs.tripDiary.data.TripPointBean;
import com.anywherelabs.tripDiary.database.DbAdapter;
import com.anywherelabs.tripDiary.util.location.LocationUtils;
import com.anywherelabs.tripDiary.util.location.MyLocationListener;
import com.anywherelabs.util.dialog.DialogUtils;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class CreateTripPoint extends Activity {
    
    // Constants
    private static final String CLASS_NAME = CreateTripPoint.class.toString();
    private static final int GPS_READ_EVENT = Constants.GPS_READ_EVENT;
    private static final int GPS_TIMEOUT = Constants.DEFAULT_GPS_TIMEOUT;

    // Attributes
    private TripBean trip;
    private TextView locationText;
    private TextView tripPointNameText;
    private TextView tripPointDescriptionText;
    private MyLocationListener locationListener;
    private Location location;
    private Location lastKnownLocation;
    private DbAdapter dbAdapter;
    
    // Methods
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(CLASS_NAME,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_trip_point);

        init();
        
        startListeningLocation();
    }
    
    private TripBean getTripFromDb(long tripId) {
        dbAdapter.open();
        TripBean tmpTrip = dbAdapter.getTrip(tripId);
        dbAdapter.close();
        return tmpTrip;
    }
    
    private void init() {
        locationText = (TextView) findViewById(R.id.createTripPoint_LocationText);
        tripPointNameText = (TextView) findViewById(R.id.createTripPoint_NameText);
        tripPointDescriptionText = (TextView) findViewById(R.id.createTripPoint_DescriptionText);
        
        dbAdapter = new DbAdapter(this);
        long tripId = getIntent().getExtras().getLong(Constants.INTENT_KEY_TRIP_ID);
        trip = getTripFromDb(tripId);

        final Handler handler = new LocationHandler();
        locationListener = new MyLocationListener(this, handler, GPS_TIMEOUT);
    }
    
    private void updateLocation(Location location) {
        this.location = location;
    }
    
    private void updateLocationView() {
        if (location!=null) {
            showLocation();
        } else {
            if (lastKnownLocation!=null) {
                location = lastKnownLocation;
                showLocationLastKnowLocation();
            } else {
                locationText.setText(R.string.location_CouldNotGetLocation);
            }
        }
    }
    
    private void showLocationLastKnowLocation() {
        String prefix = "(last known location) ";
        locationText.setText(prefix + location.getLongitude() + "," + location.getLatitude());
    }
    
    private void showLocation() {
        locationText.setText(location.getLongitude() + "," + location.getLatitude());
    }
    
    private synchronized void stopListeningLocation() {
        locationListener.stopListeningLocation();
    }
    
    private synchronized void startListeningLocation() {
        updateLastLocation();
        locationText.setText(R.string.location_SearchingLocation);
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
    
    private TripPointBean getTripPointFromView() {
        TripPointBean tripPoint = null;
        String tripPointName = tripPointNameText.getText().toString();
        if (tripPointName.length()>0) {
            String tripPointDescription = tripPointDescriptionText.getText().toString();
            String locationString = locationText.getText().toString();
            tripPoint = new TripPointBean(trip.getId(), tripPointName, 
                    tripPointDescription, LocationUtils.toLocation(locationString));
        }
        return tripPoint;
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Do nothing
    }
    
    // onClick button methods
    //--------------------------------
    public void onClickOkButton(View view) {
        TripPointBean tripPoint = getTripPointFromView();
        if (tripPoint!=null) {
            try {
                dbAdapter.open();
                long tripPointId = dbAdapter.createTripPoint(tripPoint);
                dbAdapter.close();
                Log.i(CLASS_NAME, "created tripPoint " + tripPoint.getName());
                callOpenTripPointActivity(tripPointId);
            } catch (Exception e) {
                dbAdapter.close();
                Log.e(CLASS_NAME, "Unknown exception at okButtonOnClick() method");
            }
        } else {
            DialogUtils.showOkDialog(this, "TripPoint name can not be empty");
        }
    }
    
    private void closeActivity(int result) {
        setResult(result);
        finish();
    }
    
    public void onClickCancelButton(View view) {
        closeActivity(RESULT_OK);
    }
    
    public void onClickGpsConnectionButton(View view) {
        startListeningLocation();
    }

    //---------------
    public void callOpenTripPointActivity(long tripPointId) {
        Intent openTripPointIntent = new Intent(this, OpenTripPoint.class);
        openTripPointIntent.putExtra(Constants.INTENT_KEY_TRIP_POINT_ID, tripPointId);
        startActivityForResult(openTripPointIntent, Constants.REQUEST_OPEN_TRIP_POINT);
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
