package com.anywherelabs.tripDiary;

import com.anywherelabs.tripDiary.data.NoteBean;
import com.anywherelabs.tripDiary.data.TripPointBean;
import com.anywherelabs.tripDiary.database.DbAdapter;
import com.anywherelabs.tripDiary.util.activity.ActivityUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SelectNote extends Activity {
    
    // Attributes
    private TripPointBean tripPoint;
    private List<NoteBean> notes = new ArrayList<NoteBean>();
    private ListViewNotesAdapter listViewAdapter;
    
    // Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.select_note);
        
        init(savedInstanceState);        
    }
    
    private void init(Bundle savedInstanceState) {
        
        tripPoint = ActivityUtils.recoverTripPoint(savedInstanceState, this);
        
        ListView listViewNotes = (ListView) findViewById(R.id.selectNote_ListView);
        listViewAdapter = new ListViewNotesAdapter(LayoutInflater.from(this));
        listViewNotes.setAdapter(listViewAdapter);


        listViewNotes.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                       int position, long id) {
                openNote(notes.get(position));
            }
          });
        
        // Register listener for context menu when long-press
        registerForContextMenu(listViewNotes);
        
        populateNotes();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, 
            ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.select_note_menu, menu);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        deleteNote(info.id);
        updateListView();
        return true;
    }
    
    private void deleteNote(long noteId) {
        // delete trip from database
        DbAdapter dbAdapter = new DbAdapter(this);
        dbAdapter.open();
        dbAdapter.deleteNote(noteId);
        dbAdapter.close();
    }
    
    private void updateListView() {
        listViewAdapter.notifyDataSetChanged();
    }
    
    private void openNote(NoteBean note) {
        Intent openNoteIntent = new Intent(this, OpenNote.class);
        openNoteIntent.putExtra(Constants.INTENT_KEY_TRIP_ID, note.getId());
        startActivityForResult(openNoteIntent, Constants.REQUEST_OPEN_TRIP);
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Close and go back
        setResult(RESULT_OK);
        finish();
    }

    private void populateNotes() {
        DbAdapter dbAdapter = new DbAdapter(this);
        dbAdapter = dbAdapter.open();
        notes = dbAdapter.getAllNotesFromTripPoint(tripPoint.getId());
        dbAdapter.close();
    }
    
    //-------------------------------------------------------
    // Inner class
    private class ListViewNotesAdapter extends BaseAdapter {

        // Attributes
        private LayoutInflater inflater;
        
        // Constructors
        public ListViewNotesAdapter(LayoutInflater inflater) {
            this.inflater = inflater;
        }
        
        @Override
        public int getCount() {
            return notes.size();
        }

        @Override
        public Object getItem(int position) {
            return notes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return notes.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tripTextView;
            if (convertView==null) {
                convertView = inflater.inflate(R.layout.trip_row, null);
                tripTextView = (TextView) convertView.findViewById(R.id.selectTrip_TripRow);
                convertView.setTag(tripTextView);
            } else {
                tripTextView = (TextView) convertView.getTag();
            }
            tripTextView.setText(notes.get(position).getName());
            return convertView;
        }
        
    }
    // End inner class
    //-------------------------------------------------------

}
