package com.anywherelabs.tripDiary.data;

import com.anywherelabs.tripDiary.Constants;

import java.util.ArrayList;
import java.util.List;

public class TripBean {

    // Constants
    public static final String TRIP_ID_KEY = "_id";
    public static final String TRIP_NAME_KEY = "name";
    public static final String TRIP_PEOPLE_KEY = "people";
    public static final String TRIP_COMMENT_KEY = "comment";
    
    public static final long NO_TRIP_ID = Constants.NO_ID;
    
    // Attributes
    private long id;
    private String name;
    private String people;
    private String description;
    private List<TripPointBean> tripPoints;
    
    //Constructors
    public TripBean(long id, String name, String people, 
            String comment, List<TripPointBean> tripPoints) {
        this.id = id;
        this.name = name;
        this.people = people;
        this.description = comment;
        this.tripPoints = tripPoints;
    }
    
    public TripBean(long id, String name, String people, String description) {
        this(id, name, people, description, new ArrayList<TripPointBean>());
    }
    
    public TripBean(long id, String name, String people) {
        this(id, name, people, "");
    }
    
    public TripBean(long id, String name) {
        this(id, name, "");
    }

    public TripBean(String name, String people, 
            String comment, List<TripPointBean> tripPoints) {
        this(NO_TRIP_ID, name, people, comment, tripPoints);
    }
    
    public TripBean(String name, String people, String description) {
        this(NO_TRIP_ID, name, people, description, new ArrayList<TripPointBean>());
    }
    
    public TripBean(String name, String people) {
        this(NO_TRIP_ID, name, people, "");
    }
    
    public TripBean(String name) {
        this(NO_TRIP_ID, name, "");
    }

    // Methods
    //Getters
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPeople() {
        return people;
    }

    public String getDescription() {
        return description;
    }

    public List<TripPointBean> getTripPoints() {
        return tripPoints;
    }

    //Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTripPoints(List<TripPointBean> tripPoints) {
        this.tripPoints = tripPoints;
    }
    
    //toString
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("TripBean[");
        stringBuilder.append(id).append(", ");
        stringBuilder.append(name).append(", ");
        // if (time!=null) {
            stringBuilder.append(people.toString()).append(", ");
        // }
        // if (tripPointId!=NO_TRIP_POINT) {
            stringBuilder.append(description).append(getTripPointNames()).append("]");
        // }
        return stringBuilder.toString();
    }
    
    private String getTripPointNames() {
        StringBuilder stringBuilder = new StringBuilder(" - Points[");
        for (TripPointBean point : tripPoints) {
            stringBuilder.append(point.getName()).append(",");
        }
        // remove last comma
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

}
