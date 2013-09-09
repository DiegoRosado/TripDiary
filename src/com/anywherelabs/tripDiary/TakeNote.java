package com.anywherelabs.tripDiary;

import com.anywherelabs.tripDiary.data.NoteBean;
import com.anywherelabs.tripDiary.data.TripPointBean;
import com.anywherelabs.tripDiary.database.DbAdapter;
import com.anywherelabs.tripDiary.util.activity.ActivityUtils;
import com.anywherelabs.util.dialog.DialogUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class TakeNote extends Activity {
    
    // Attributes
    private DbAdapter dbAdapter;
    private TripPointBean tripPoint;
    private TextView titleText;
    private EditText nameText;
    private EditText descriptionText;
    
    // Methods
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_note);
        
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        dbAdapter = new DbAdapter(this);
        
        tripPoint = ActivityUtils.recoverTripPoint(savedInstanceState, this);
        assert (tripPoint!=null) : "tripPoint can NOT be null";
        
        titleText = (TextView) findViewById(R.id.takeNote_Title);
        nameText = (EditText) findViewById(R.id.takeNote_NameText);
        descriptionText = (EditText) findViewById(R.id.takeNote_DescriptionText);

        titleText.setText(titleText.getText().toString() + " - " + tripPoint.getName());
    }
    
    protected void onResume() {
        super.onResume();
    }
    
    // onClick button methods
    //--------------------------------
    // Ok Click
    public void okButtonOnClick(View view) {
        NoteBean note = getNoteFromView();
        if (note!=null) {
            dbAdapter.open();
            dbAdapter.createNote(note);
            dbAdapter.close();
            // Go back
            setResult(RESULT_OK);
            finish();
        }
    }
    
    private NoteBean getNoteFromView() {
        NoteBean note = null;
        String name = nameText.getText().toString();
        String description = descriptionText.getText().toString();
        if (validateInput(name, description)) {
            note = new NoteBean(name, description, tripPoint.getId());
        }
        return note;
    }
    
    private boolean validateInput(String noteName, String noteDescription) {
        if ((noteName!=null) && (noteName.length()>0)) {
            return true;
        } else {
            String message= getResources().getString(R.string.takeNote_NoValidInput_NoteNameEmpty);
            DialogUtils.showOkDialog(this,message);
            return false;
        }
    }
    
    // Cancel Click
    public void cancelButtonOnClick(View view) {
        setResult(RESULT_OK);
        finish();
    }

}
