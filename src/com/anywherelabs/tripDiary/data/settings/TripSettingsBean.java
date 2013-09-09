package com.anywherelabs.tripDiary.data.settings;

import java.util.ArrayList;
import java.util.List;

public class TripSettingsBean {

    // Attributes
    private long tripId;
    private boolean autotrack;
    private boolean autoexport;
    List<AbstractExportator> exportators;
    
    // Constructors
    public TripSettingsBean(long tripId) {
        this.tripId = tripId;
        exportators = new ArrayList<AbstractExportator>();
    }

    public TripSettingsBean(long tripId, boolean autotrack, boolean autoexport) {
        this(tripId);
        this.autotrack = autotrack;
        this.autoexport = autoexport;
    }
    
    // Methods
    //Getters
    public long getTripId() {
        return tripId;
    }
    
    public boolean isAutotrack() {
        return autotrack;
    }

    public boolean isAutoexport() {
        return autoexport;
    }

    public List<AbstractExportator> getExportators() {
        return exportators;
    }

    //Setters
    public void setAutotrack(boolean autotrack) {
        this.autotrack = autotrack;
    }

    public void setAutoexport(boolean autoexport) {
        this.autoexport = autoexport;
    }

//    public void setExportators(List<AbstractExportable> exportators) {
//        this.exportators = exportators;
//    }

    public void addExportator(AbstractExportator exportator) {
        exportators.add(exportator);
    }
    
    
}
