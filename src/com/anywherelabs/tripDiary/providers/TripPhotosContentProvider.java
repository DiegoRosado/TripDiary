package com.anywherelabs.tripDiary.providers;

import com.anywherelabs.tripDiary.database.DbAdapter;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class TripPhotosContentProvider extends ContentProvider {
    
    // Constants
    private static final String TAG = TripPhotosContentProvider.class.toString();
    public static final String AUTHORITY = "com.anywherelabs.tripDiary.providers." + TAG;
    private static UriMatcher uriMatcher;
    
    // Attributes
    private DbAdapter dbAdapter;
    
    
    // Constructor
    public TripPhotosContentProvider() {
        
    }

    // Methods
    @Override
    public boolean onCreate() {
        dbAdapter = new DbAdapter(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        return null;
    }

    @Override
    public Uri insert(Uri arg0, ContentValues arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int delete(Uri arg0, String arg1, String[] arg2) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getType(Uri arg0) {
        // TODO Auto-generated method stub
        return null;
    }

}
