package com.anywherelabs.tripDiary;

import com.anywherelabs.util.dialog.DialogUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TripDiary extends Activity {


    // Methods    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_diary);
    }
    
    // onClick button methods
    public void createTrip(View view) {
        createTrip();
    }
    
    public void openTrip(View view) {
        openTrip();
    }
    
    public void globalSettings(View view) {
        DialogUtils.showOkDialog(this,"Global Settings");
    }
    //---------------------------
    //end onClick button methods
    
    private void createTrip() {
        Intent createTripIntent = new Intent(this, CreateTrip.class);
        startActivityForResult(createTripIntent, Constants.REQUEST_CREATE_TRIP);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Do nothing
    }
    
    private void openTrip() {
        Intent openTripIntent = new Intent(this, SelectTrip.class);
        startActivityForResult(openTripIntent, Constants.REQUEST_OPEN_TRIP_LIST);
    }
    
}