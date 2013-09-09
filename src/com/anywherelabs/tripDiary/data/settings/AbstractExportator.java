package com.anywherelabs.tripDiary.data.settings;

public abstract class AbstractExportator {
    
    // Attributes
    private boolean autoExport;
    
    // Constructor
    
    // Methods
    
    //Getters
    public boolean isAutoExportActive() {
        return autoExport;
    }
    
    //Setters
    public void setAutoExportActive(boolean autoExportActive) {
        autoExport = autoExportActive;
    }
    
    // Others
    public abstract void export();
    
}
