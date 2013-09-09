package com.anywherelabs.tripDiary;

public class Constants {

    // Constants
    //Request Codes
    public static final int REQUEST_CREATE_TRIP = 1;
    public static final int REQUEST_OPEN_TRIP_LIST = 2;
    public static final int REQUEST_OPEN_TRIP = 3;
    public static final int REQUEST_CREATE_TRIP_POINT = 4;
    public static final int REQUEST_OPEN_TRIP_POINT = 5;
    public static final int REQUEST_TRIP_SETTINGS = 6;
    
    
    public static final int GPS_READ_EVENT = 1000;

    public static final int CAMERA_PIC_REQUEST = 1337;     
    public static final int CAMERA_VIDEO_REQUEST = 1338;     
    
    //Result Codes

    //Intent extra key
    public static final String INTENT_KEY_TRIP_ID = "tripId";
    public static final String INTENT_KEY_TRIP_POINT_ID = "tripPointId";
    public static final String INTENT_KEY_NOTE_ID = "noteId";

    
    // IDs
    public static final long NO_ID = 0;
    
    
    // Timeouts
    public static final int DEFAULT_GPS_TIMEOUT = 3 * 1000;
    
    // Event Actions
    public static final int ACTION_EVENT_RELEASE_TOUCH_SCREEN = 1;
    public static final int ACTION_EVENT_PRESS_TOUCH_SCREEN = 0;


}
