package com.anywherelabs.tripDiary.util.media.photo;

import com.anywherelabs.tripDiary.data.PhotoBean;
import com.anywherelabs.tripDiary.database.DbAdapter;

import android.content.Context;

import java.util.List;

public class PhotoUtils {

    // Constructor
    private PhotoUtils() {
        // Utility class
    }
    
    // Methods
    
    public static List<PhotoBean> getPhotosFromTripPointId(Context context, long printPointId) {
        //TODO Check photo uri exists as file in the SD Card
        DbAdapter dbAdapter = new DbAdapter(context);
        dbAdapter.open();
        List<PhotoBean> photos = dbAdapter.getPhotosFromTripPointId(printPointId);
        dbAdapter.close();
        return photos;
    }
}
