package com.anywherelabs.tripDiary.util.media.video;

import com.anywherelabs.tripDiary.data.VideoBean;
import com.anywherelabs.tripDiary.database.DbAdapter;

import android.content.Context;
import android.net.Uri;

import java.util.List;

public class VideoUtils {

    // Constructor
    private VideoUtils() {
        // Utility class
    }
    
    // Methods
    
    public static List<VideoBean> getVideosFromTripPointId(Context context, long printPointId) {
        //TODO Check photo uri exists as file in the SD Card
        DbAdapter dbAdapter = new DbAdapter(context);
        dbAdapter.open();
        List<VideoBean> videos = dbAdapter.getVideosFromTripPointId(printPointId);
        dbAdapter.close();
        return videos;
    }
    
    public static int getVideoId(Uri videoUri) {
        // TODO
        return 1;
    }
    
}
