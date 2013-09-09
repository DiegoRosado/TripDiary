package com.anywherelabs.tripDiary.data;

import com.anywherelabs.tripDiary.Constants;
import com.anywherelabs.util.time.TimeUtils;

import java.sql.Timestamp;

public class NoteBean {
    
    // Constants
    public static final long NO_NOTE_ID = Constants.NO_ID;
    private static final long NO_TRIP_POINT_ID = TripPointBean.NO_TRIP_POINT_ID;
    
    //Attributes
    private long id;
    private String name;
    private String description;
    private Timestamp timestamp;
    private long tripPointId; 
    
    //Constructors
    public NoteBean(long id, String name, String description, Timestamp timestamp, long tripPointId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.timestamp = timestamp;
        this.tripPointId = tripPointId;        
    }
    
//    public NoteBean(long id, String name, String description, Timestamp timestamp) {
//        this(id, name, description, timestamp, NO_TRIP_POINT_ID);
//    }
    
    public NoteBean(long id, String name, String description, long tripPointId) {
        this(id, name, description, TimeUtils.getNow(), tripPointId);
    }
    
//    public NoteBean(long id, String name, String description) {
//        this(id, name, description, TimeUtils.getNow(), NO_TRIP_POINT_ID);
//    }
    
//    public NoteBean(String name, String description, Timestamp timestamp) {
//        this(NO_NOTE_ID, name, description, timestamp, NO_TRIP_POINT_ID);
//    }
    
    public NoteBean(String name, String description, long tripPointId) {
        this(NO_NOTE_ID, name, description, TimeUtils.getNow(), tripPointId);
    }
    
//    public NoteBean(String name, String description) {
//        this(NO_NOTE_ID, name, description, TimeUtils.getNow(), NO_TRIP_POINT_ID);
//    }
    
    // Methods    
    //Getters
    public long getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public long getTripPointId() {
        return tripPointId;
    }

    //Setters
    public void setName(String name) {
        this.name = name;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setTripPointId(long tripPointId) {
        this.tripPointId = tripPointId;
    }

    // toString
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("NoteBean[");
        stringBuilder.append(id).append(", ");
        stringBuilder.append(name).append(", ");
        if (description!=null) {
            stringBuilder.append(description).append(", ");
        }
//        if (time!=null) {
            stringBuilder.append(timestamp.toString()).append(", ");
//        }
        if (tripPointId!=NO_TRIP_POINT_ID) {
            stringBuilder.append(tripPointId).append("]");
        }
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
        NoteBean other = (NoteBean) obj;
        if (id != other.id)
            return false;
        return true;
    }

}
