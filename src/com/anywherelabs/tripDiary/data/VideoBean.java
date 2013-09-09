package com.anywherelabs.tripDiary.data;

import com.anywherelabs.tripDiary.Constants;
import com.anywherelabs.util.time.TimeUtils;

import android.net.Uri;

import java.sql.Timestamp;

public class VideoBean {
    
    // Constants
    public static final long NO_VIDEO_ID = Constants.NO_ID;
    
    // Attributes
    private long id;
    private Uri uri;
    private Timestamp timestamp;
    private long tripPointId;
    
    // Constructors
    public VideoBean(long id, Uri uri, Timestamp timestamp, long tripPointId) {
        this.id = id;
        this.uri = uri;
        this.timestamp = timestamp;
        this.tripPointId = tripPointId;
    }
    
    public VideoBean(Uri uri, Timestamp timestamp, long tripPointId) {
        this(NO_VIDEO_ID, uri, timestamp, tripPointId);
    }
    
    public VideoBean(long id, Uri uri, Timestamp timestamp) {
        this(id, uri, timestamp, TripPointBean.NO_TRIP_POINT_ID);
    }

    public VideoBean(Uri uri, Timestamp timestamp) {
        this(NO_VIDEO_ID, uri, timestamp, TripPointBean.NO_TRIP_POINT_ID);
    }

    public VideoBean(long id, Uri uri, long tripPointId) {
        this(id, uri, TimeUtils.getNow(), tripPointId);
    }

    public VideoBean(Uri uri, long tripPointId) {
        this(NO_VIDEO_ID, uri, TimeUtils.getNow(), tripPointId);
    }

    public VideoBean(long id, Uri uri) {
        this(id, uri, TimeUtils.getNow());
    }

    public VideoBean(Uri uri) {
        this(NO_VIDEO_ID, uri, TimeUtils.getNow());
    }

    // Methods
    //Getters
    public long getId() {
        return id;
    }

    public Uri getUri() {
        return uri;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public long getTripPointId() {
        return tripPointId;
    }

    //Setters
    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setTripPointId(long tripPointId) {
        this.tripPointId = tripPointId;
    }
    
    //toString
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("PhotoBean[");
        stringBuilder.append(id).append(", ");
        stringBuilder.append(uri).append(", ");
//        if (time!=null) {
            stringBuilder.append(timestamp.toString()).append(", ");
//        }
//          if (tripPointId!=NO_TRIP_POINT) {
            stringBuilder.append(tripPointId).append("]");
//        }
        return stringBuilder.toString();
    }

    // HashCode and equals
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int)id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        VideoBean other = (VideoBean) obj;
        if (id != other.id)
            return false;
        return true;
    }

}
