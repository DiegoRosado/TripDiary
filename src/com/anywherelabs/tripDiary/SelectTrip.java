package com.anywherelabs.tripDiary;


import com.anywherelabs.tripDiary.data.TripBean;
import com.anywherelabs.tripDiary.database.DbAdapter;

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

public class SelectTrip extends Activity {

    // Attributes
    private List<TripBean> trips = new ArrayList<TripBean>();
    private ListViewTripsAdapter listViewAdapter;
    
    // Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        populateTrips();
        
        setContentView(R.layout.select_trip);
        
        ListView listViewTrips = (ListView) findViewById(R.id.selectTrip_ListView);
        listViewAdapter = new ListViewTripsAdapter(LayoutInflater.from(this));
        listViewTrips.setAdapter(listViewAdapter);


        listViewTrips.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                       int position, long id) {
                openTrip(trips.get(position));
            }
          });
        
        // Register listener for context menu when long-press
        registerForContextMenu(listViewTrips);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, 
            ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.select_trip_menu, menu);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        deleteTrip(info.id);
        updateListView();
        return true;
    }
    
    private void deleteTrip(long tripId) {
        // delete trip from database
        DbAdapter dbAdapter = new DbAdapter(this);
        dbAdapter.open();
        dbAdapter.deleteTrip(tripId);
        dbAdapter.close();
        // delete trip from data source
        deleteFromTripsWithId(tripId);
    }
    
    private void deleteFromTripsWithId(long tripId) {
        for (TripBean trip: trips) {
            if (trip.getId()==tripId) {
                trips.remove(trip);
                break;
            }
        }
    }
    
    private void updateListView() {
        listViewAdapter.notifyDataSetChanged();
        //listViewAdapter.notifyDataSetInvalidated();
    }
    
    private void openTrip(TripBean trip) {
        Intent openTripIntent = new Intent(this, OpenTrip.class);
        openTripIntent.putExtra(Constants.INTENT_KEY_TRIP_ID, trip.getId());
        startActivityForResult(openTripIntent, Constants.REQUEST_OPEN_TRIP);
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Close and go back
        setResult(RESULT_OK);
        finish();
    }

    private void populateTrips() {
        DbAdapter dbAdapter = new DbAdapter(this);
        dbAdapter = dbAdapter.open();
        trips = dbAdapter.getAllTrips();
        dbAdapter.close();
    }
    
    //-------------------------------------------------------
    // Inner class
    private class ListViewTripsAdapter extends BaseAdapter {

        // Attributes
        private LayoutInflater inflater;
        
        // Constructors
        public ListViewTripsAdapter(LayoutInflater inflater) {
            this.inflater = inflater;
        }
        
        @Override
        public int getCount() {
            return trips.size();
        }

        @Override
        public Object getItem(int position) {
            return trips.get(position);
        }

        @Override
        public long getItemId(int position) {
            return trips.get(position).getId();
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
            tripTextView.setText(trips.get(position).getName());
            return convertView;
        }
        
    }
    // End inner class
    //-------------------------------------------------------
    
}
