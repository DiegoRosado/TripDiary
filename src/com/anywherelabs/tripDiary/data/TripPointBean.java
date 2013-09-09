package com.anywherelabs.tripDiary.data;

import com.anywherelabs.tripDiary.Constants;
import com.anywherelabs.tripDiary.util.location.LocationUtils;
import com.anywherelabs.util.time.TimeUtils;

import android.location.Location;

import java.sql.Timestamp;
import java.util.List;

public class TripPointBean {
    
    // Constants
    public static final long NO_TRIP_POINT_ID = Constants.NO_ID;
    
    // Attributes
    private long tripId; // references to the trip this tripPoint belong to
    private long id;
    private String name;
    private String description;
    private Timestamp timestamp;
    private Location location;
    private List<PhotoBean> photos;
    private List<NoteBean> notes;

    // Constructors
    public TripPointBean(long tripId, long tripPointId, String tripPointName,
            String description, Timestamp timestamp, Location location) {
        this.tripId = tripId;
        this.id = tripPointId;
        this.name = tripPointName;
        this.description = description;
        this.timestamp = timestamp;
        this.location = location;
    }

    public TripPointBean(long tripId, long tripPointId, String tripPointName,
            String description, Location location) {
        this(tripId, tripPointId, tripPointName, description, 
                TimeUtils.getNow(), location);
    }

    public TripPointBean(long tripId, String tripPointName,
            String description, Location location) {
        this(tripId, NO_TRIP_POINT_ID, tripPointName, description, location);
    }
    
    // Methods
    //Getters
    public long getTripId() {
        return tripId;
    }

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

    public Location getLocation() {
        return location;
    }

    public List<PhotoBean> getPhotos() {
        return photos;
    }

    public List<NoteBean> getNotes() {
        return notes;
    }

    //Setters
    public void setTripId(long tripId) {
        this.tripId = tripId;
    }

    public void setId(long tripPointId) {
        this.id = tripPointId;
    }

    public void setName(String tripPointName) {
        this.name = tripPointName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setPhotos(List<PhotoBean> photos) {
        this.photos = photos;
    }

    public void setNotes(List<NoteBean> notes) {
        this.notes = notes;
    }
    
    // Others methods
    // add/remove photo
    public void addPhoto(PhotoBean photo) {
        photos.add(photo);
    }
    
    public void removePhoto(PhotoBean photo) {
        photos.remove(photo);
    }
    
    // add/remove note
    public void addNote(NoteBean note) {
        notes.add(note);
    }
    
    public void removeNote(NoteBean note) {
        notes.remove(note);
    }

    //toString
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("TripPointBean[");
        stringBuilder.append(tripId).append(", ");
        stringBuilder.append(id).append(", ");
        stringBuilder.append(name).append(", ");
        if (description!=null) {
            stringBuilder.append(description);
        }
        stringBuilder.append(", ");
        if (timestamp!=null) {
            stringBuilder.append(timestamp.toGMTString());
        }
        stringBuilder.append(", ");
        if (location!=null) {
            stringBuilder.append(LocationUtils.toString(location));
        }
        stringBuilder.append("]");

        return stringBuilder.toString();
    }

}
