package com.anywherelabs.tripDiary;

import com.anywherelabs.tripDiary.data.PhotoBean;
import com.anywherelabs.tripDiary.data.TripPointBean;
import com.anywherelabs.tripDiary.data.VideoBean;
import com.anywherelabs.tripDiary.database.DbAdapter;
import com.anywherelabs.tripDiary.util.activity.ActivityUtils;
import com.anywherelabs.util.time.TimeUtils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.sql.Timestamp;

public class OpenTripPoint extends Activity {
    
    // Constants
    private static final String CLASS_NAME = OpenTripPoint.class.toString();
    private static final int CAMERA_PIC_REQUEST = Constants.CAMERA_PIC_REQUEST;     
    private static final int CAMERA_VIDEO_REQUEST = Constants.CAMERA_VIDEO_REQUEST;     
    
    // Attributes
    // If tripPoint != null we have already selected a TripPoint
    private TripPointBean tripPoint; 
    private TextView titleText;
    private DbAdapter dbAdapter;
    private Uri photoUri;
    private Uri videoUri;
    
    // Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.open_trip_point);
        init(savedInstanceState);
        updateView();
    }
    
    private void init(Bundle savedInstanceState) {
        dbAdapter = new DbAdapter(this);
        
        titleText = (TextView) findViewById(R.id.openTripPoint_Title);

        tripPoint = ActivityUtils.recoverTripPoint(savedInstanceState, this);
    }
    
    private void updateView() {
        titleText.setText(tripPoint.getName());
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CAMERA_PIC_REQUEST:
                photoDone(resultCode, data);
                break;
            case CAMERA_VIDEO_REQUEST:
                videoDone(resultCode, data);
                break;
            default:
                // ???
        }
        
        updateView();
    }
    
    private void photoDone(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            //TODO change TimeUtils.getNow() for retrieving the time from data
            Timestamp now = TimeUtils.getNow();
            PhotoBean photo = new PhotoBean(photoUri, now, tripPoint.getId());
            dbAdapter.open();
            dbAdapter.createPhoto(photo);
            dbAdapter.close();
        } else {
            Log.e(CLASS_NAME,"Error making photo");
        }
    }

    private void videoDone(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            //TODO change TimeUtils.getNow() for retrieving the time from data
            Timestamp now = TimeUtils.getNow();
            VideoBean video = new VideoBean(videoUri, now, tripPoint.getId());
            dbAdapter.open();
            dbAdapter.createVideo(video);
            dbAdapter.close();
        } else {
            Log.e(CLASS_NAME,"Error making photo");
        }
    }

    //onClick functions
    //----------------------------------
    // set/Change Location
    public void onClickSetChangeLocation(View view) {
        setChangeLocation();
    }
    
    private void setChangeLocation() {
        Intent setChangeLocationIntent = new Intent(this, SetChangeLocation.class);
        setChangeLocationIntent.putExtra(Constants.INTENT_KEY_TRIP_POINT_ID, tripPoint.getId());
        startActivity(setChangeLocationIntent);
    }
    
    // Show Location
    public void onClickShowLocation(View view) {
        showLocation();
    }
    
    private void showLocation() {
        Intent showLocationIntent = new Intent(this, ShowLocation.class);
        showLocationIntent.putExtra(Constants.INTENT_KEY_TRIP_POINT_ID, tripPoint.getId());
        startActivity(showLocationIntent);
    }

    // Take Photo
    public void onClickTakePhoto(View view) {
        takePhotoFromBuildInCamera();
    }

    private void takePhotoFromBuildInCamera() {
        Timestamp now = TimeUtils.getNow();
        String photoTitle = tripPoint.getName() + "_" + TimeUtils.toFileNameFormat(now);
        Intent cameraIntent = createCameraIntent(photoTitle);
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);  
    }
    
    private Intent createCameraIntent(String title) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        photoUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        return cameraIntent;
    }
    
    // Show Photos
    public void onClickShowPhotos(View view) {
        showPhotos();
    }

    private void showPhotos() {
        Intent showPhotosIntent = new Intent(this, ShowPhotos.class);
        showPhotosIntent.putExtra(Constants.INTENT_KEY_TRIP_POINT_ID, tripPoint.getId());
        startActivity(showPhotosIntent);
    }
    
    // Take Video
    public void onClickTakeVideo(View view) {
        takeVideoFromBuildInCamera();
    }

    private void takeVideoFromBuildInCamera() {
        String videoFileName = tripPoint.getName() + "-" + TimeUtils.toFileNameFormat(TimeUtils.getNow());
        ContentValues values = new ContentValues();
        values.put(MediaStore.Video.Media.TITLE, videoFileName);
        videoUri = getContentResolver().insert(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
        startActivityForResult(cameraIntent, CAMERA_VIDEO_REQUEST);  
    }
    
    // Show Videos
    public void onClickShowVideos(View view) {
        showVideos();
    }
    
    private void showVideos() {
        Intent showVideosIntent = new Intent(this, ShowVideos.class);
        showVideosIntent.putExtra(Constants.INTENT_KEY_TRIP_POINT_ID, tripPoint.getId());
        startActivity(showVideosIntent);
    }

    // Take Note
    public void onClickTakeNote(View view) {
        takeNote();
    }

    private void takeNote() {
        Intent takeNoteIntent = new Intent(this, TakeNote.class);
        takeNoteIntent.putExtra(Constants.INTENT_KEY_TRIP_POINT_ID, tripPoint.getId());
        startActivity(takeNoteIntent);
    }

    // Show Notes
    public void onClickShowNotes(View view) {
        showNotes();
    }
    
    private void showNotes() {
        Intent showNotesIntent = new Intent(this, SelectNote.class);
        showNotesIntent.putExtra(Constants.INTENT_KEY_TRIP_POINT_ID, tripPoint.getId());
        startActivity(showNotesIntent);
    }

    // End onClick functions
    //----------------------------------

}
