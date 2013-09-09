package com.anywherelabs.tripDiary;

import com.anywherelabs.tripDiary.data.TripBean;
import com.anywherelabs.tripDiary.data.TripPointBean;
import com.anywherelabs.tripDiary.util.activity.ActivityUtils;
import com.anywherelabs.util.dialog.DialogUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class OpenTrip extends Activity {

    // Attributes
    private TripBean trip;
    private TripPointBean tripPoint; // If tripPoint != null we have already selected a TripPoint
    private TextView titleText;
    private TextView createTripPointText;
    private TextView selectTripPointText;
    
    // Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.open_trip);
        init(savedInstanceState);
        updateView();
    }
    
    private void init(Bundle savedInstanceState) {
        titleText = (TextView) findViewById(R.id.openTrip_Title);
        createTripPointText = (TextView) findViewById(R.id.openTrip_createTripPointButton);
        selectTripPointText = (TextView) findViewById(R.id.openTrip_selectTripPointButton);

        trip = ActivityUtils.recoverTrip(savedInstanceState, this);
        tripPoint = ActivityUtils.recoverTripPoint(savedInstanceState, this);
    }
    
    private void updateView() {
        String title = trip.getName();
        if (tripPoint!=null) {
            createTripPointText.setVisibility(View.GONE);
            selectTripPointText.setVisibility(View.GONE);
            title = title + " - " + tripPoint.getName();
        }
        
        titleText.setText(title);
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Nothing yet
        if (data!=null) {
            tripPoint = ActivityUtils.recoverTripPoint(data.getExtras(), this);
        }
        updateView();
    }

    //onClick functions
    public void onClickCreateTripPoint(View view) {
        createTripPoint();
    }
    
    private void createTripPoint() {
        Intent createTripPointIntent = new Intent(this, CreateTripPoint.class);
        createTripPointIntent.putExtra(Constants.INTENT_KEY_TRIP_ID, trip.getId());
        startActivityForResult(createTripPointIntent, Constants.REQUEST_CREATE_TRIP_POINT);
    }
    
    public void onClickSelectTripPoint(View view) {
        selectTripPoint();
    }
    
    private void selectTripPoint() {
        Intent selectTripPointIntent = new Intent(this, SelectTripPoint.class);
        selectTripPointIntent.putExtra(Constants.INTENT_KEY_TRIP_ID, trip.getId());
        startActivityForResult(selectTripPointIntent, Constants.REQUEST_OPEN_TRIP_POINT);
    }
    
    public void onClickExportTrip(View view) {
        DialogUtils.showOkDialog(this, "Export Trip");
    }

    public void onClickAutotrack(View view) {
//        Intent tripSettingsIntent = new Intent(this, TripSettings.class);
//        tripSettingsIntent.putExtra(Constants.INTENT_KEY_TRIP_ID, trip.getId());
//        startActivityForResult(tripSettingsIntent, Constants.REQUEST_TRIP_SETTINGS);
        DialogUtils.showOkDialog(this, "Autotrack Trip");
    }
    
}