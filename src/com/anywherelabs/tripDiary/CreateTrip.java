package com.anywherelabs.tripDiary;

import com.anywherelabs.tripDiary.data.TripBean;
import com.anywherelabs.tripDiary.database.DbAdapter;
import com.anywherelabs.util.dialog.DialogUtils;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CreateTrip extends Activity {
    
    // Attributes
    private EditText tripNameText;
    private EditText peopleText;
    private EditText startPointText;
    private EditText endPointText;
    private EditText commentText;
    
    // Methods
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_trip);
        init();
    }
    
    private void init() {
        tripNameText = (EditText) findViewById(R.id.createTrip_NameText);
        peopleText = (EditText) findViewById(R.id.createTrip_PeopleText);
        startPointText = (EditText) findViewById(R.id.createTrip_StartPointText);
        endPointText = (EditText) findViewById(R.id.createTrip_EndPointText);
        commentText = (EditText) findViewById(R.id.createTrip_DescriptionText);
    }

    protected void onResume() {
        super.onResume();
    }
    
    // onClick button methods
    //--------------------------------
    // Ok Click
    public void onClickOkButton(View view) {
        TripBean tripBean = getTripFromView();
        if (tripBean!=null) {
            DbAdapter dbAdapter = new DbAdapter(this);
            dbAdapter.open();
            
            Cursor cursor = dbAdapter.getTripByName(tripBean.getName());
            if (cursor.getCount()==0) {
                long tripId = dbAdapter.createTrip(tripBean);
                cursor.close();
                dbAdapter.close();
                // Go to Open Trip with the new create trip
                Intent openTripIntent = new Intent(this, OpenTrip.class);
                openTripIntent.putExtra(Constants.INTENT_KEY_TRIP_ID, tripId);
                startActivityForResult(openTripIntent, Constants.REQUEST_OPEN_TRIP);
                
            } else {
                cursor.close();
                dbAdapter.close();
                // TripName already exists at database
                String message= getResources().getString(R.string.createTrip_AlreadyExistsTripName);
                DialogUtils.showOkDialog(this,message);
            }
            
        }
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Close and go back
        setResult(RESULT_OK);
        finish();
    }

    private TripBean getTripFromView() {
        TripBean tripBean = null;
        
        String tripName = tripNameText.getText().toString();
        String people = peopleText.getText().toString();
        String startPoint = startPointText.getText().toString();
        String endPoint = endPointText.getText().toString();
        String comment = commentText.getText().toString();
        // Check valid input
        if (validateInput(tripName, people, startPoint, endPoint, comment)) {
            tripBean = new TripBean(TripBean.NO_TRIP_ID, tripName, people, comment);
        }
        return tripBean;
    }
    
    private boolean validateInput(String tripName, String people, String startPoint, 
            String endPoint, String comment) {
        if ((tripName!=null) && (tripName.length()>0)) {
            return true;
        } else {
            String message= getResources().getString(R.string.createTrip_NoValidInput_TripNameEmpty);
            DialogUtils.showOkDialog(this,message);
            return false;
        }
    }
    
    // Cancel Click
    public void onClickCancelButton(View view) {
        setResult(RESULT_OK);
        finish();
    }
    // end onClick button methods
    //--------------------------------
    
}
