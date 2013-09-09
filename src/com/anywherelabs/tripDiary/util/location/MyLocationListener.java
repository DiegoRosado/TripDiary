package com.anywherelabs.tripDiary.util.location;

import com.anywherelabs.tripDiary.Constants;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MyLocationListener implements LocationListener {
    
    // Constants
    private static final String CLASS_NAME = MyLocationListener.class.toString();
    private static final int GPS_READ_EVENT = Constants.GPS_READ_EVENT;
    
    // Attributes
    private Thread timeoutThread;
    private Location location;
    private LocationManager locationManager;
    private Handler handler;
    private long timeout;

    // Constructors
    public MyLocationListener(Context context, Handler handler, long timeout) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.timeout = timeout;
        this.handler = handler;
    }
    
    // Methods
    public synchronized void startListeningLocation() {
        Log.d(CLASS_NAME,"startListeningLocation()");
        timeoutThread = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(timeout);
                    location = null;
                    Log.d(CLASS_NAME,"GPS reading timeout exhausted. No location read");
                } catch (InterruptedException e) {
                    Log.d(CLASS_NAME,"GPS read location");
                } finally {
                    handler.sendMessage(createLocationMessage());
                    Log.d(CLASS_NAME,"Sent event GPS_READ_EVENT");
                }
            }
        }); 
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        timeoutThread.start();
    }

    public synchronized void stopListeningLocation() {
        Log.d(CLASS_NAME,"stopListeningLocation()");
        locationManager.removeUpdates(this);
    }

    
    public void onLocationChanged(Location location) {
        Log.d(CLASS_NAME, "onLocationChanged()");
        this.location = location;
        timeoutThread.interrupt();
    }
    
    private Message createLocationMessage() {
        Message message = new Message();
        message.obj = location;
        message.what = GPS_READ_EVENT;
        return message;
    }

    
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Not used
    }

    public void onProviderEnabled(String provider) {
        // Not used
    }

    public void onProviderDisabled(String provider) {
        // Not used
    }
    
}
