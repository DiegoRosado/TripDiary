package com.anywherelabs.tripDiary.util.activity;

import com.anywherelabs.tripDiary.Constants;
import com.anywherelabs.tripDiary.data.TripBean;
import com.anywherelabs.tripDiary.data.TripPointBean;
import com.anywherelabs.tripDiary.data.settings.TripSettingsBean;
import com.anywherelabs.tripDiary.database.DbAdapter;

import android.app.Activity;
import android.os.Bundle;

public class ActivityUtils {

    // Constructors
    private ActivityUtils() {
        // Utility Class
    }
    
    
    // Methods
    /**
     * Retrieve a TripBean object from the Bundle or the Intent. It access to database
     * to retrieve the whole object because the Bundle and the Intent save just the tripId.
     * @param savedInstanceState
     * @param activity
     * @return retrieved TripBean or null if not found.
     */
    public static TripBean recoverTrip(Bundle savedInstanceState, Activity activity) {
        TripBean tmpTrip = null;
        long tripId = (savedInstanceState == null) ? TripBean.NO_TRIP_ID :
             savedInstanceState.getLong(Constants.INTENT_KEY_TRIP_ID);
        if (tripId == TripBean.NO_TRIP_ID) {
            Bundle extras = activity.getIntent().getExtras();
            tripId = (extras == null) ? TripBean.NO_TRIP_ID : 
                extras.getLong(Constants.INTENT_KEY_TRIP_ID);
        }
        if (tripId != TripBean.NO_TRIP_ID) {
            DbAdapter dbAdapter = new DbAdapter(activity);
            dbAdapter.open();
            tmpTrip = dbAdapter.getTrip(tripId);
            dbAdapter.close();
        }
        return tmpTrip;
    }

    
    /**
     * Retrieve a TripPointBean object from the Bundle or the Intent. It access to database
     * to retrieve the whole object because the Bundle and the Intent save just the tripPointId.
     * @param savedInstanceState
     * @param activity
     * @return retrieved TripPointBean or null if not found.
     */
    public static TripPointBean recoverTripPoint(Bundle savedInstanceState, Activity activity) {
        TripPointBean tmpTripPoint = null;
        long tripPointId = (savedInstanceState == null) ? TripPointBean.NO_TRIP_POINT_ID :
            savedInstanceState.getLong(Constants.INTENT_KEY_TRIP_POINT_ID);
        if (tripPointId == TripPointBean.NO_TRIP_POINT_ID) {
            Bundle extras = activity.getIntent().getExtras();
            tripPointId = (extras == null) ? TripPointBean.NO_TRIP_POINT_ID : 
                extras.getLong(Constants.INTENT_KEY_TRIP_POINT_ID);
        }
        if (tripPointId != TripPointBean.NO_TRIP_POINT_ID) {
            DbAdapter dbAdapter = new DbAdapter(activity);
            dbAdapter.open();
            tmpTripPoint = dbAdapter.getTripPoint(tripPointId);
            dbAdapter.close();
        }
        return tmpTripPoint;
    }

    
    /**
     * Retrieve a TripBean object from the Bundle or the Intent. It access to database
     * to retrieve the whole object because the Bundle and the Intent save just the tripId.
     * @param savedInstanceState
     * @param activity
     * @return retrieved TripBean or null if not found.
     */
    public static TripSettingsBean recoverTripSettings(Bundle savedInstanceState, Activity activity) {
        TripSettingsBean tmpTripSettings = null;
        long tripId = (savedInstanceState == null) ? TripBean.NO_TRIP_ID :
             savedInstanceState.getLong(Constants.INTENT_KEY_TRIP_ID);
        if (tripId == TripBean.NO_TRIP_ID) {
            Bundle extras = activity.getIntent().getExtras();
            tripId = (extras == null) ? TripBean.NO_TRIP_ID : 
                extras.getLong(Constants.INTENT_KEY_TRIP_ID);
        }
        if (tripId != TripBean.NO_TRIP_ID) {
            DbAdapter dbAdapter = new DbAdapter(activity);
            dbAdapter.open();
            tmpTripSettings = dbAdapter.getTripSettings(tripId);
            dbAdapter.close();
        }
        return tmpTripSettings;
    }
    
    
    
}
