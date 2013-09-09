package com.anywherelabs.tripDiary;

import com.anywherelabs.tripDiary.data.TripBean;
import com.anywherelabs.tripDiary.data.settings.TripSettingsBean;
import com.anywherelabs.tripDiary.database.DbAdapter;
import com.anywherelabs.tripDiary.util.activity.ActivityUtils;
import com.anywherelabs.util.dialog.DialogUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class TripSettings extends Activity {
    
    // Attributes
    private TripBean trip;
    private TripSettingsBean tripSettings;
    private DbAdapter dbAdapter;
    private CheckBox autotrackCheckBox;
    private CheckBox autoexportCheckBox;
    private CheckBox exportFacebookCheckBox;
    private CheckBox exportWordpressCheckBox;
    private CheckBox exportImpressCheckBox;
    private CheckBox exportEmailCheckBox;
    
    // Methods
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_settings);
        init(savedInstanceState);
        updateView();
    }
    
    private void init(Bundle savedInstanceState) {
        dbAdapter = new DbAdapter(this);
        // View elements
        autotrackCheckBox = (CheckBox) findViewById(R.id.tripSettings_Autotrack);
        autoexportCheckBox = (CheckBox) findViewById(R.id.tripSettings_Autoexport);
        exportFacebookCheckBox = (CheckBox) findViewById(R.id.tripSettings_FacebookCheckbox);
        exportWordpressCheckBox = (CheckBox) findViewById(R.id.tripSettings_WordpressCheckbox);
        exportImpressCheckBox = (CheckBox) findViewById(R.id.tripSettings_ImpressCheckbox);
        exportEmailCheckBox = (CheckBox) findViewById(R.id.tripSettings_EmailCheckbox);
        // bean elements
        trip = ActivityUtils.recoverTrip(savedInstanceState, this);
        tripSettings = recoverTripSettingsOrCreate(savedInstanceState, this); 
    }

    private TripSettingsBean recoverTripSettingsOrCreate(Bundle savedInstanceState, Activity activity) {
        TripSettingsBean tmpTripSettings = ActivityUtils.recoverTripSettings(savedInstanceState, activity);
        if (tmpTripSettings==null) {
            tmpTripSettings = new TripSettingsBean(trip.getId());
        }
        return tmpTripSettings;
    }
    
    private void updateView() {
        // update view using tripSettings bean values
        autotrackCheckBox.setChecked(tripSettings.isAutotrack());
        autoexportCheckBox.setChecked(tripSettings.isAutoexport());
//        exportFacebookCheckBox.setChecked(tripSettings.isExportFacebookActive());
//        exportWordpressCheckBox.setChecked(tripSettings.isExportWordpressActive());
//        exportImpressCheckBox.setChecked(tripSettings.isExportImpressActive());
//        exportEmailCheckBox.setChecked(tripSettings.isExportEmailActive());
    }
    
    protected void onResume() {
        super.onResume();
    }
    
    // onClick button methods
    //--------------------------------
//    public void onClickAutoTrack(View view) {
//        DialogUtils.showOkDialog(this, "onClick Autotrack");
//    }
//
//    public void onClickAutoExport(View view) {
//        DialogUtils.showOkDialog(this, "onClick AutoExport");
//    }
//
//    public void onClickExportFacebook(View view) {
//        DialogUtils.showOkDialog(this, "onClick Export Facebook");
//    }
//
//    public void onClickExportWordpress(View view) {
//        DialogUtils.showOkDialog(this, "onClick Export Wordpress");
//    }
//
//    public void onClickExportImpress(View view) {
//        DialogUtils.showOkDialog(this, "onClick Export Impress");
//    }
//
//    public void onClickExportEmail(View view) {
//        DialogUtils.showOkDialog(this, "onClick Export Email");
//    }

    public void onClickFacebookSettings(View view) {
        DialogUtils.showOkDialog(this, "onClick Facebook Settings");
    }

    public void onClickWordpressSettings(View view) {
        DialogUtils.showOkDialog(this, "onClick Wordpress Settings");
    }

    public void onClickImpressSettings(View view) {
        DialogUtils.showOkDialog(this, "onClick Impress Settings");
    }

    public void onClickEmailSettings(View view) {
        DialogUtils.showOkDialog(this, "onClick Email Settings");
    }

    public void onClickOkButton(View view) {
        TripSettingsBean tripSettings = getTripSettingsFromView();
        dbAdapter.open();
        dbAdapter.saveTripSettings(tripSettings);
        dbAdapter.close();
        setResult(RESULT_OK);
        finish();
    }
    
    public void onClickCancelButton(View view) {
        setResult(RESULT_OK);
        finish();
    }
    // end onClick button methods
    //--------------------------------

    private TripSettingsBean getTripSettingsFromView() {
        boolean autotrack = autotrackCheckBox.isChecked();
        boolean autoexport = autoexportCheckBox.isChecked();
        //TODO get concrete export settings (e.g.: facebook, wordpress, ...)
        TripSettingsBean tmpTripSettings = new TripSettingsBean(trip.getId());
        tmpTripSettings.setAutotrack(autotrack);
        tmpTripSettings.setAutoexport(autoexport);
        return tmpTripSettings;        
    }
    
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // Close and go back
//        setResult(RESULT_OK);
//        finish();
//    }

}
