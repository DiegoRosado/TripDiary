package com.anywherelabs.tripDiary.database;

import com.anywherelabs.tripDiary.data.NoteBean;
import com.anywherelabs.tripDiary.data.PhotoBean;
import com.anywherelabs.tripDiary.data.TripBean;
import com.anywherelabs.tripDiary.data.TripPointBean;
import com.anywherelabs.tripDiary.data.VideoBean;
import com.anywherelabs.tripDiary.data.settings.TripSettingsBean;
import com.anywherelabs.tripDiary.util.location.LocationUtils;
import com.anywherelabs.util.utils.BooleanUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.net.Uri;
import android.util.Log;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DbAdapter {

    // Constants
    private static final String CLASS_NAME = DbAdapter.class.toString();
    private static final String DATABASE_NAME = "trip_diary";
    private static final int DATABASE_VERSION = 1;
    
    // Tables names
    private static final String TABLE_SETTINGS = "settings";
    private static final String TABLE_TRIP = "trip";
    private static final String TABLE_TRIP_SETTINGS = "trip_settings";
    private static final String TABLE_EXPORT_FACEBOOK_SETTINGS = "export_facebook_settings";
    private static final String TABLE_EXPORT_WORDPRESS_SETTINGS = "export_wordpress_settings";
    private static final String TABLE_EXPORT_IMPRESS_SETTINGS = "export_impress_settings";
    private static final String TABLE_EXPORT_EMAIL_SETTINGS = "export_email_settings";
    private static final String TABLE_TRIP_POINT = "trip_point";
    private static final String TABLE_PHOTO = "photo";
    private static final String TABLE_VIDEO = "video";
    private static final String TABLE_NOTE = "note";
    
    // Trip Columns
    private static final String COLUMN_TRIP_ID = "_id"; 
    private static final String COLUMN_TRIP_NAME = "name"; 
    private static final String COLUMN_TRIP_PEOPLE = "people"; 
    private static final String COLUMN_TRIP_DESCRIPTION = "description";
    
    // TripSettings Columns
    private static final String COLUMN_TRIP_SETTINGS_TRIP_ID = "trip_id"; 
    private static final String COLUMN_TRIP_SETTINGS_AUTOTRACK = "autotrack"; 
    private static final String COLUMN_TRIP_SETTINGS_AUTOEXPORT = "autoexport"; 
    
    // TripPoint Columns
    private static final String COLUMN_TRIP_POINT_ID = "_id"; 
    private static final String COLUMN_TRIP_POINT_TRIP_ID = "trip_id"; 
    private static final String COLUMN_TRIP_POINT_NAME = "point_name"; 
    private static final String COLUMN_TRIP_POINT_DESCRIPTION = "description"; 
    private static final String COLUMN_TRIP_POINT_TIMESTAMP = "time"; 
    private static final String COLUMN_TRIP_POINT_LOCATION = "location"; 

    // Photo Columns
    private static final String COLUMN_PHOTO_ID = "_id"; 
    private static final String COLUMN_PHOTO_TRIP_POINT_ID = "trip_point_id"; 
    private static final String COLUMN_PHOTO_TIMESTAMP = "time"; 
    private static final String COLUMN_PHOTO_URI = "uri"; 
    
    // Video Columns
    private static final String COLUMN_VIDEO_ID = "_id"; 
    private static final String COLUMN_VIDEO_TRIP_POINT_ID = "trip_point_id"; 
    private static final String COLUMN_VIDEO_TIMESTAMP = "time"; 
    private static final String COLUMN_VIDEO_URI = "uri"; 
    
    // Note Columns
    private static final String COLUMN_NOTE_ID = "_id"; 
    private static final String COLUMN_NOTE_NAME = "name"; 
    private static final String COLUMN_NOTE_TIMESTAMP = "time"; 
    private static final String COLUMN_NOTE_DESCRIPTION = "description"; 
    private static final String COLUMN_NOTE_TRIP_POINT_ID = "trip_point_id"; 

    //Create statements
    //TODO Define table settings columns
    private static final String CREATE_SETTINGS_TABLE_STATEMENT =
        "CREATE TABLE " + TABLE_SETTINGS + "()";
    private static final String CREATE_TRIP_TABLE_STATEMENT = 
        "CREATE TABLE " + TABLE_TRIP + "(_id INTEGER PRIMARY KEY, name TEXT, people TEXT, description TEXT)"; 
    private static final String CREATE_TRIP_SETTINGS_TABLE_STATEMENT =
        "CREATE TABLE " + TABLE_TRIP_SETTINGS + "(trip_id INTEGER PRIMARY KEY," +
        " autotrack INTEGER DEFAULT 0, autoexport INTEGER DEFAULT 0)";
    //TODO Create export settings tables
    private static final String CREATE_TRIP_POINT_TABLE_STATEMENT = 
        "CREATE TABLE " + TABLE_TRIP_POINT + "(_id INTEGER PRIMARY KEY, trip_id INTEGER," +
        " point_name TEXT, description TEXT, time INTEGER, location TEXT)"; 
    private static final String CREATE_PHOTO_TABLE_STATEMENT = 
        "CREATE TABLE " + TABLE_PHOTO + "(_id INTEGER PRIMARY KEY, trip_point_id INTEGER," +
        " time INTEGER, uri TEXT)"; 
    private static final String CREATE_VIDEO_TABLE_STATEMENT = 
        "CREATE TABLE " + TABLE_VIDEO + "(_id INTEGER PRIMARY KEY, trip_point_id INTEGER," +
        " time INTEGER, uri TEXT)"; 
    private static final String CREATE_NOTE_TABLE_STATEMENT = 
        "CREATE TABLE " + TABLE_NOTE + "(_id INTEGER PRIMARY KEY, trip_point_id INTEGER," +
        " time INTEGER, name TEXT, description TEXT)";
    
    // Attributes
    private final Context context;
    private DbHelper dbHelper;
    private SQLiteDatabase db;
  
    // Constructor
    public DbAdapter(Context context) {
        this.context = context;
    }
    
    // Methods
    /**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public DbAdapter open() throws SQLException {
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    /**
     * Close database
     */
    public void close() {
        dbHelper.close();
    }


    //CRUD operations for Trip objects
    //---------------------------------------------------------------------
    public long createTrip(TripBean trip) {
        ContentValues values = valuesFromTrip(trip);
        long tripId = db.insertOrThrow(TABLE_TRIP, null, values);
        return tripId;
    }
    
    private ContentValues valuesFromTrip(TripBean trip) {
        assert trip!=null : "TripBean can not be null";
        ContentValues values = new ContentValues();
        long tripId = trip.getId();
        values.put(COLUMN_TRIP_ID, (tripId==TripBean.NO_TRIP_ID) ? null : tripId);
        values.put(COLUMN_TRIP_NAME, trip.getName());
        values.put(COLUMN_TRIP_PEOPLE, trip.getDescription());
        values.put(COLUMN_TRIP_DESCRIPTION, trip.getDescription());
        return values;
    }

    
    public TripBean getTrip(long tripId) {
        Cursor cursor = db.query(TABLE_TRIP, null, "_id=" + tripId, null, null, null, null);
        cursor.move(-1);
        TripBean trip = getNextTrip(cursor);
        cursor.close();
        return trip;
    }
    
    public Cursor getTripByName(String tripName) {
        Cursor cursor = db.query(TABLE_TRIP, null, "name='" + tripName + "'", null, null, null, null);
        return cursor;
    }
    
    public List<TripBean> getAllTrips() {
        Cursor cursor = db.query(TABLE_TRIP, null, null, null, null, null, null);
        cursor.move(-1);
        List<TripBean> trips = getAllTrips(cursor);
        cursor.close();
        return trips;
    }
    
    public void updateTrip(TripBean trip) {
        ContentValues values = valuesFromTrip(trip);
        db.update(TABLE_TRIP, values, "_id="+trip.getId(), null);
    }
    
    public void deleteTrip(long tripId) {
        db.delete(TABLE_TRIP, "_id="+tripId, null);
        //delete related TripPoint data
        deleteTripPointsFromTrip(tripId);
    }
    
    private List<TripBean> getAllTrips(Cursor cursor) {
        List<TripBean> trips = new ArrayList<TripBean>();
        TripBean trip = getNextTrip(cursor);
        while (trip!=null) {
            trips.add(trip);
            trip = getNextTrip(cursor);
        }
        return trips;
    }
    
    private TripBean getNextTrip(Cursor cursor) {
        TripBean trip = null;
        if (cursor.move(1)) {
            long id = cursor.getLong(cursor.getColumnIndex(COLUMN_TRIP_ID));
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_TRIP_NAME));
            String people = cursor.getString(cursor.getColumnIndex(COLUMN_TRIP_PEOPLE));
            String description = cursor.getString(cursor.getColumnIndex(COLUMN_TRIP_DESCRIPTION));
            //TripPoints are not loaded to avoid overloading
            //List<TripPointBean> tripPoints = getAllTripPointFromTrip(id);

            trip = new TripBean(id, name, people, description, null);
        }
        return trip;
    }

    //CRUD operations for TripSettings objects
    //---------------------------------------------------------------------
    public TripSettingsBean getTripSettings(long tripId) {
        Cursor cursor = db.query(TABLE_TRIP_SETTINGS, null, "trip_id=" + tripId, null, null, null, null);
        cursor.move(-1);
        TripSettingsBean tripSettings = getNextTripSettings(cursor);
        cursor.close();
        return tripSettings;
    }

    public void saveTripSettings(TripSettingsBean tripSettings) {
        // Create tripSettings
        if (createTripSettings(tripSettings)==-1) {
            // Or update tripSettings
            updateTripSettings(tripSettings);
        }
    }
    
    private long createTripSettings(TripSettingsBean tripSettings) {
        ContentValues values = valuesFromTripSettings(tripSettings);
        return db.insert(TABLE_TRIP_SETTINGS, null, values);
    }
    
    private void updateTripSettings(TripSettingsBean tripSettings) {
        ContentValues values = valuesFromTripSettings(tripSettings);
        db.update(TABLE_TRIP_SETTINGS, values, "trip_id="+tripSettings.getTripId(), null);
    }
    
    private ContentValues valuesFromTripSettings(TripSettingsBean tripSettings) {
        assert tripSettings!=null : "TripSettings can not be null";
        ContentValues values = new ContentValues();
        long tripId = tripSettings.getTripId();
        values.put(COLUMN_TRIP_SETTINGS_TRIP_ID, tripId);
        values.put(COLUMN_TRIP_SETTINGS_AUTOTRACK, 
                BooleanUtils.fromBoolToInt(tripSettings.isAutotrack()));
        values.put(COLUMN_TRIP_SETTINGS_AUTOEXPORT, 
                BooleanUtils.fromBoolToInt(tripSettings.isAutoexport()));
        return values;
    }
    
    private TripSettingsBean getNextTripSettings(Cursor cursor) {
        TripSettingsBean tripSettings = null;
        if (cursor.move(1)) {
            long tripId = cursor.getLong(cursor.getColumnIndex(COLUMN_TRIP_SETTINGS_TRIP_ID));
            boolean autotrack = BooleanUtils.fromIntToBool(
                    cursor.getInt(cursor.getColumnIndex(COLUMN_TRIP_SETTINGS_AUTOTRACK)));
            boolean autoexport = BooleanUtils.fromIntToBool(
                    cursor.getInt(cursor.getColumnIndex(COLUMN_TRIP_SETTINGS_AUTOEXPORT)));

            tripSettings = new TripSettingsBean(tripId, autotrack, autoexport);
        }
        return tripSettings;
    }

    //CRUD operations for TripPoint objects
    //---------------------------------------------------------------------
    public long createTripPoint(TripPointBean tripPoint) {
        ContentValues values = valuesFromTripPoint(tripPoint);
        long tripPointId = db.insertOrThrow(TABLE_TRIP_POINT, null, values);
        return tripPointId;
    }
    
    private ContentValues valuesFromTripPoint(TripPointBean tripPoint) {
        assert tripPoint!=null : "TripPointBean can not be null";
        ContentValues values = new ContentValues();
        long tripPointId = tripPoint.getId();
        values.put(COLUMN_TRIP_POINT_ID, (tripPointId==TripPointBean.NO_TRIP_POINT_ID) ? null : tripPointId);
        values.put(COLUMN_TRIP_POINT_TRIP_ID, tripPoint.getTripId());
        values.put(COLUMN_TRIP_POINT_NAME, tripPoint.getName());
        values.put(COLUMN_TRIP_POINT_DESCRIPTION, tripPoint.getDescription());
        values.put(COLUMN_TRIP_POINT_TIMESTAMP, tripPoint.getTimestamp().getTime());
        values.put(COLUMN_TRIP_POINT_LOCATION, LocationUtils.toString(tripPoint.getLocation()));
        return values;
    }
    
    public TripPointBean getTripPoint(long tripPointId) {
        Cursor cursor = db.query(TABLE_TRIP_POINT, null, "_id=" + tripPointId, null, null, null, null);
        cursor.move(-1);
        TripPointBean tripPoint = getNextTripPoint(cursor);
        cursor.close();
        return tripPoint;
    }
    
    public List<TripPointBean> getAllTripPointFromTrip(long tripId) {
        Cursor cursor = db.query(TABLE_TRIP_POINT, null, "trip_id=" + tripId, null, null, null, null);
        cursor.move(-1);
        List<TripPointBean> tripPoints = getTripPoints(cursor);
        cursor.close();
        return tripPoints;
    }
    
    public void deleteTripPointsFromTrip(long tripId) {
        db.delete(TABLE_TRIP_POINT, "trip_id="+tripId, null);
    }
    
    public void deleteTripPoint(long tripPointId) {
        db.delete(TABLE_TRIP_POINT, "_id="+tripPointId, null);
        // delete related stuff
        deletePhotosFromTripPoint(tripPointId);
        deleteNotesPointFromTripPoint(tripPointId);
    }

    private List<TripPointBean> getTripPoints(Cursor cursor) {
        List<TripPointBean> tripPoints = new ArrayList<TripPointBean>();
        TripPointBean tripPoint = getNextTripPoint(cursor);
        while (tripPoint!=null) {
            tripPoints.add(tripPoint);
            tripPoint = getNextTripPoint(cursor);
        }
        return tripPoints;
    }
    
    private TripPointBean getNextTripPoint(Cursor cursor) {
        TripPointBean tripPoint = null;
        if (cursor.move(1)) {
            long tripId = cursor.getLong(cursor.getColumnIndex(COLUMN_TRIP_POINT_TRIP_ID));
            long tripPointId = cursor.getLong(cursor.getColumnIndex(COLUMN_TRIP_POINT_ID));
            String tripPointName = cursor.getString(cursor.getColumnIndex(COLUMN_TRIP_POINT_NAME));
            String description = cursor.getString(cursor.getColumnIndex(COLUMN_TRIP_POINT_DESCRIPTION));
            Timestamp timestamp = new Timestamp(cursor.getLong(cursor.getColumnIndex(COLUMN_TRIP_POINT_TIMESTAMP)));
            Location location = LocationUtils.toLocation(
                    cursor.getString(cursor.getColumnIndex(COLUMN_TRIP_POINT_LOCATION)));
            
            tripPoint = new TripPointBean(tripId, tripPointId, tripPointName,
                    description , timestamp, location);
        }
        return tripPoint;
    }
    
    public void updateTripPoint(TripPointBean tripPoint) {
        ContentValues values = valuesFromTripPoint(tripPoint);
        db.update(TABLE_TRIP_POINT, values, "_id="+tripPoint.getId(), null);
    }

    //CRUD operations for Photo objects
    //---------------------------------------------------------------------
    public long createPhoto(PhotoBean photo) {
        ContentValues values = valuesFromPhoto(photo);
        long photoId = db.insertOrThrow(TABLE_PHOTO, null, values);
        return photoId;
    }
    
    private ContentValues valuesFromPhoto(PhotoBean photo) {
        assert photo!=null : "PhotoBean can not be null";
        ContentValues values = new ContentValues();
        long photoId = photo.getId();
        values.put(COLUMN_PHOTO_ID, (photoId==PhotoBean.NO_PHOTO_ID) ? null : photoId);
        values.put(COLUMN_PHOTO_URI, photo.getUri().toString());
        values.put(COLUMN_PHOTO_TIMESTAMP, photo.getTimestamp().getTime());
        values.put(COLUMN_PHOTO_TRIP_POINT_ID, photo.getTripPointId());
        return values;
    }

    public List<PhotoBean> getPhotosFromTripPointId(long tripPointId) {
        Cursor cursor = db.query(TABLE_PHOTO, null, "trip_point_id=" + tripPointId, null, null, null, null);
        cursor.move(-1);
        List<PhotoBean> tripPoints = getPhotos(cursor);
        cursor.close();
        return tripPoints;
    }

    private List<PhotoBean> getPhotos(Cursor cursor) {
        List<PhotoBean> photos = new ArrayList<PhotoBean>();
        PhotoBean photo = getNextPhoto(cursor);
        while (photo!=null) {
            photos.add(photo);
            photo = getNextPhoto(cursor);
        }
        return photos;
    }

    private void deletePhotosFromTripPoint(long tripId) {
        //TODO delete Photos
    }
    
    private PhotoBean getNextPhoto(Cursor cursor) {
        PhotoBean photo = null;
        if (cursor.move(1)) {
            long photoId = cursor.getLong(cursor.getColumnIndex(COLUMN_PHOTO_ID));
            long tripPointId = cursor.getLong(cursor.getColumnIndex(COLUMN_PHOTO_TRIP_POINT_ID));
            Timestamp timestamp = new Timestamp(cursor.getLong(cursor.getColumnIndex(COLUMN_PHOTO_TIMESTAMP)));
            Uri photoUri = Uri.parse(cursor.getString(cursor.getColumnIndex(COLUMN_PHOTO_URI)));
            
            photo = new PhotoBean(photoId, photoUri, timestamp, tripPointId);
        }
        return photo;
    }
    
    public void deletePhotoWithUri(String imageUri) {
        db.delete(TABLE_PHOTO, "uri='" + imageUri + "'", null);
    }
    
    //CRUD operations for Video objects
    //---------------------------------------------------------------------
    public long createVideo(VideoBean video) {
        ContentValues values = valuesFromVideo(video);
        long photoId = db.insertOrThrow(TABLE_VIDEO, null, values);
        return photoId;
    }
    
    private ContentValues valuesFromVideo(VideoBean video) {
        assert video!=null : "VideoBean can not be null";
        ContentValues values = new ContentValues();
        long videoId = video.getId();
        values.put(COLUMN_VIDEO_ID, (videoId==PhotoBean.NO_PHOTO_ID) ? null : videoId);
        values.put(COLUMN_VIDEO_URI, video.getUri().toString());
        values.put(COLUMN_VIDEO_TIMESTAMP, video.getTimestamp().getTime());
        values.put(COLUMN_VIDEO_TRIP_POINT_ID, video.getTripPointId());
        return values;
    }

    public List<VideoBean> getVideosFromTripPointId(long tripPointId) {
        Cursor cursor = db.query(TABLE_VIDEO, null, "trip_point_id=" + tripPointId, null, null, null, null);
        cursor.move(-1);
        List<VideoBean> tripPoints = getVideos(cursor);
        cursor.close();
        return tripPoints;
    }

    private List<VideoBean> getVideos(Cursor cursor) {
        List<VideoBean> photos = new ArrayList<VideoBean>();
        VideoBean photo = getNextVideo(cursor);
        while (photo!=null) {
            photos.add(photo);
            photo = getNextVideo(cursor);
        }
        return photos;
    }

    private VideoBean getNextVideo(Cursor cursor) {
        VideoBean video = null;
        if (cursor.move(1)) {
            long videoId = cursor.getLong(cursor.getColumnIndex(COLUMN_VIDEO_ID));
            long tripPointId = cursor.getLong(cursor.getColumnIndex(COLUMN_VIDEO_TRIP_POINT_ID));
            Timestamp timestamp = new Timestamp(cursor.getLong(cursor.getColumnIndex(COLUMN_VIDEO_TIMESTAMP)));
            Uri photoUri = Uri.parse(cursor.getString(cursor.getColumnIndex(COLUMN_VIDEO_URI)));
            
            video = new VideoBean(videoId, photoUri, timestamp, tripPointId);
        }
        return video;
    }
    
    public void deleteVideoWithUri(String videoUri) {
        db.delete(TABLE_VIDEO, "uri='" + videoUri + "'", null);
    }
    
    private void deleteVideosFromTripPoint(long tripId) {
        //TODO delete Videos
    }
    
    //CRUD operations for Note objects
    //---------------------------------------------------------------------
    public long createNote(NoteBean note) {
        ContentValues values = valuesFromNote(note);
        long noteId = db.insertOrThrow(TABLE_NOTE, null, values);
        return noteId;
    }
    
    private ContentValues valuesFromNote(NoteBean note) {
        assert note!=null : "NoteBean can not be null";
        ContentValues values = new ContentValues();
        long noteId = note.getId();
        values.put(COLUMN_NOTE_ID, (noteId==NoteBean.NO_NOTE_ID) ? null : noteId);
        values.put(COLUMN_NOTE_NAME, note.getName());
        values.put(COLUMN_NOTE_DESCRIPTION, note.getDescription());
        values.put(COLUMN_NOTE_TIMESTAMP, note.getTimestamp().getTime());
        values.put(COLUMN_NOTE_TRIP_POINT_ID, note.getTripPointId());
        return values;
    }
    
    public NoteBean getNoteByName(String name) {
        Cursor cursor = db.query(TABLE_NOTE, null, "name='" + name +"'", null, null, null, null);
        cursor.moveToPosition(-1);
        NoteBean note = getNextNote(cursor);
        cursor.close();
        return note;
    }
    
    private NoteBean getNextNote(Cursor cursor) {
        NoteBean note = null;
        if (cursor.move(1)) {
            long noteId = cursor.getLong(cursor.getColumnIndex(COLUMN_NOTE_ID));
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_NAME));
            String description = cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_DESCRIPTION));
            Timestamp time = new Timestamp(cursor.getLong(cursor.getColumnIndex(COLUMN_NOTE_TIMESTAMP)));
            long tripPointId = cursor.getLong(cursor.getColumnIndex(COLUMN_NOTE_TRIP_POINT_ID));
            
            note = new NoteBean(noteId, name, description, time, tripPointId);
        }
        return note;
    }
    
    public List<NoteBean> getAllNotesFromTripPoint(long tripPointId) {
        Cursor cursor = db.query(TABLE_NOTE, null, "trip_point_id="+tripPointId, null, null, null, null);
        cursor.move(-1);
        List<NoteBean> notes = getNotes(cursor);
        cursor.close();
        return notes;
    }

    private List<NoteBean> getNotes(Cursor cursor) {
        List<NoteBean> notes = new ArrayList<NoteBean>();
        NoteBean note = getNextNote(cursor);
        while (note!=null) {
            notes.add(note);
            note = getNextNote(cursor);
        }
        return notes;
    }

    public void deleteNote(long noteId) {
        db.delete(TABLE_NOTE, "_id="+noteId, null);
    }

    private void deleteNotesPointFromTripPoint(long tripId) { 
        //TODO delete Notes
    }
    
    
    // Inner class DbHelper
    //------------------------------------------------------------------------------
    private static class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            createTables(db);
        }
        
        private void createTables(SQLiteDatabase db) {
            db.execSQL(CREATE_TRIP_TABLE_STATEMENT);
            db.execSQL(CREATE_TRIP_SETTINGS_TABLE_STATEMENT);
            db.execSQL(CREATE_TRIP_POINT_TABLE_STATEMENT);
            db.execSQL(CREATE_PHOTO_TABLE_STATEMENT);
            db.execSQL(CREATE_VIDEO_TABLE_STATEMENT);
            db.execSQL(CREATE_NOTE_TABLE_STATEMENT);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i(CLASS_NAME,"onUpgrade() method called. oldVersion was " + oldVersion
                    + ", newVersion is " + newVersion);
//            dropTables(db);
//            createTables(db);
        }
        
        private void dropTables(SQLiteDatabase db) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIP);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIP_POINT);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTO);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIDEO);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE);
        }
        
    }

}
