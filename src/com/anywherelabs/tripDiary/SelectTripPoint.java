package com.anywherelabs.tripDiary;

import com.anywherelabs.tripDiary.data.TripBean;
import com.anywherelabs.tripDiary.data.TripPointBean;
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

public class SelectTripPoint extends Activity {

    // Attributes
    private DbAdapter dbAdapter;
    private TripBean trip;
    private List<TripPointBean> tripPoints = new ArrayList<TripPointBean>();
    private ListViewTripPointsAdapter listViewTripPointsAdapter;
    
    // Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.select_trip_point);
        init(savedInstanceState);
        
    }
    
    private void init(Bundle savedInstanceState) {
        // init data
        dbAdapter = new DbAdapter(this);
        trip = recoverTrip(savedInstanceState);
        populateTripPoints();

        // set view adapter and match with data
        ListView listViewTripPoints = (ListView) findViewById(R.id.selectTripPoint_ListView);
        listViewTripPointsAdapter = new ListViewTripPointsAdapter(LayoutInflater.from(this));
        listViewTripPoints.setAdapter(listViewTripPointsAdapter);

        // Set onClick event handler
        listViewTripPoints.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                       int position, long id) {
                openTripPoint(tripPoints.get(position));
            }
          });

        // Register listener for context menu when long-press
        registerForContextMenu(listViewTripPoints);
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, 
            ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.select_trip_point_menu, menu);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        deleteTripPoint(info.id);
        updateListView();
        return true;
    }
    
    private void deleteTripPoint(long tripPointId) {
        // delete tripPoint from database
        DbAdapter dbAdapter = new DbAdapter(this);
        dbAdapter.open();
        dbAdapter.deleteTripPoint(tripPointId);
        dbAdapter.close();
        // delete trip from data source
        deleteFromTripPointsWithId(tripPointId);
    }
    
    private void deleteFromTripPointsWithId(long tripPointId) {
        for (TripPointBean tripPoint: tripPoints) {
            if (tripPoint.getId()==tripPointId) {
                tripPoints.remove(tripPoint);
                break;
            }
        }
    }
    
    private void updateListView() {
        listViewTripPointsAdapter.notifyDataSetChanged();
        //listViewTripPointsAdapter.notifyDataSetInvalidated();
    }
    
    private TripBean recoverTrip(Bundle savedInstanceState) {
        TripBean tmpTrip = null;
        long tripId = (savedInstanceState == null) ? TripBean.NO_TRIP_ID :
            savedInstanceState.getLong(Constants.INTENT_KEY_TRIP_ID);
        if (tripId == TripBean.NO_TRIP_ID) {
            Bundle extras = getIntent().getExtras();
            tripId = (extras == null) ? TripBean.NO_TRIP_ID : 
                extras.getLong(Constants.INTENT_KEY_TRIP_ID);
        }
        if (tripId != TripBean.NO_TRIP_ID) {
            dbAdapter.open();
            tmpTrip = dbAdapter.getTrip(tripId);
            dbAdapter.close();
        }
        return tmpTrip;
    }
    
    private void openTripPoint(TripPointBean tripPoint) {
        Intent openTripPointIntent = new Intent(this, OpenTripPoint.class);
        openTripPointIntent.putExtra(Constants.INTENT_KEY_TRIP_POINT_ID, tripPoint.getId());
        startActivityForResult(openTripPointIntent, Constants.REQUEST_OPEN_TRIP_POINT);
//        setResult(RESULT_OK, openTripPointIntent);
//        finish();
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Close and go back
        setResult(RESULT_OK);
        finish();
    }

    private void populateTripPoints() {
        DbAdapter dbAdapter = new DbAdapter(this);
        dbAdapter = dbAdapter.open();
        tripPoints = dbAdapter.getAllTripPointFromTrip(trip.getId());
        dbAdapter.close();
    }
    
    //-------------------------------------------------------
    // Inner class
    private class ListViewTripPointsAdapter extends BaseAdapter {

        // Attributes
        private LayoutInflater inflater;
        
        // Constructors
        public ListViewTripPointsAdapter(LayoutInflater inflater) {
            this.inflater = inflater;
        }
        
        @Override
        public int getCount() {
            return tripPoints.size();
        }

        @Override
        public Object getItem(int position) {
            return tripPoints.get(position);
        }

        @Override
        public long getItemId(int position) {
            return tripPoints.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tripTextView;
            if (convertView==null) {
                convertView = inflater.inflate(R.layout.trip_point_row, null);
                
                tripTextView = (TextView) convertView.findViewById(R.id.selectTripPoint_TripPointRow);
                
                convertView.setTag(tripTextView);

            } else {
                tripTextView = (TextView) convertView.getTag();
            }
            tripTextView.setText(tripPoints.get(position).getName());
            return convertView;
        }
        
    }
    // End inner class
    //-------------------------------------------------------
    
}
