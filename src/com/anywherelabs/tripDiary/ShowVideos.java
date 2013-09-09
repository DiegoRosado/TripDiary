package com.anywherelabs.tripDiary;

import com.anywherelabs.tripDiary.data.TripPointBean;
import com.anywherelabs.tripDiary.data.VideoBean;
import com.anywherelabs.tripDiary.database.DbAdapter;
import com.anywherelabs.tripDiary.util.activity.ActivityUtils;
import com.anywherelabs.tripDiary.util.media.video.VideoUtils;
import com.anywherelabs.util.dialog.DialogUtils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShowVideos extends Activity implements SurfaceHolder.Callback {

    // Constants
    private static final String CLASS_NAME = ShowVideos.class.toString();
    private static final String TITLE_SEPARATOR = " - ";
    
    
    // Attributes
    private TripPointBean tripPoint;
    private TextView titleText;
    private Gallery gallery;
    private List<Uri> videoUris;
    private DbAdapter dbAdapter;
    private MediaPlayer mediaPlayer;
    private SurfaceView videoView;
    private SurfaceHolder videoHolder;


    // Methods
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(CLASS_NAME,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_videos);

        init(savedInstanceState);
        Log.d(CLASS_NAME,"Init done");
    }
    
    private void init(Bundle savedInstanceState) {
        dbAdapter = new DbAdapter(this);
        
        tripPoint = ActivityUtils.recoverTripPoint(savedInstanceState, this);
        assert tripPoint!=null : "TripPoint can NOT be null";
        
        titleText = (TextView) findViewById(R.id.showVideos_Title);
        gallery = (Gallery) findViewById(R.id.showVideos_Gallery);
        initVideoView();
        
        titleText.setText(tripPoint.getName() + TITLE_SEPARATOR 
                + getString(R.string.showVideos_Videos));
        
        initGallery();
    }
    
    private void initVideoView() {
        videoView = (SurfaceView) findViewById(R.id.showVideos_Video);
        videoHolder = videoView.getHolder();
        videoHolder.addCallback(this);
        videoHolder.setSizeFromLayout();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDisplay(videoHolder);
    }
    
    private void initGallery() {
        loadVideoUris();
        gallery.setAdapter(new VideoFirstFrameAdapter());
        gallery.setOnItemClickListener(new ItemClickListener());
    }
    
    private void loadVideoUris() {
        List<VideoBean> videos = VideoUtils.getVideosFromTripPointId(this, tripPoint.getId());
        videoUris = new ArrayList<Uri>();
        for (VideoBean video : videos) {
            videoUris.add(video.getUri());
        }
    }
    
    // ImageAdapter class
    //--------------------------------------------
    private class VideoFirstFrameAdapter extends BaseAdapter {

        // Attributes
        private int itemBackground;
        
        // Constructor
        public VideoFirstFrameAdapter() {
            init();
        }
        
        private void init() {
            TypedArray typedArray = obtainStyledAttributes(R.styleable.Gallery);
            itemBackground = typedArray.getResourceId(R.styleable.Gallery_android_galleryItemBackground, 0);
            typedArray.recycle();
        }
        
        // Methods
        @Override
        public int getCount() {
            return videoUris.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parentView) {
            ImageView imageView = new ImageView(ShowVideos.this);
            Uri videoUri = videoUris.get(position);
            int videoId = VideoUtils.getVideoId(videoUri);
            imageView.setImageDrawable(new BitmapDrawable(getFirstVideoFrame(videoId)));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(new Gallery.LayoutParams(150,120));
            imageView.setBackgroundResource(itemBackground);
            return imageView;
        }
        
    }
    
    private Bitmap getFirstVideoFrame(int id) {
        Bitmap thumb = MediaStore.Video.Thumbnails.getThumbnail(
        getContentResolver(),
        id, MediaStore.Video.Thumbnails.MINI_KIND, null);
        return thumb;

    }
    
    // ItemClickListener
    //--------------------------------------------
    private class ItemClickListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView parentView, View view, int position, long id) {
            Uri videoUri = videoUris.get(position);
            try {
                mediaPlayer.setDataSource(ShowVideos.this, videoUri);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.start();                
            } catch (IOException e) {
                Log.e(CLASS_NAME, "Exception in ShowVideos#ItemClickListener.onItemClick(), position = "
                        + position + ", id = " + id );
                e.printStackTrace();
            }
            videoView.setOnLongClickListener(new GalleryVideoOnLongClickListener(videoUri));
        }
    }
    
    // GalleryImage OnLongClick Listener class
    //--------------------------------------------
    private class GalleryVideoOnLongClickListener implements OnLongClickListener {

        // Attributes
        private Uri videoUri;
        
        // Constructors
        public GalleryVideoOnLongClickListener(Uri imageUri) {
            this.videoUri = imageUri;
        }
        
        @Override
        public boolean onLongClick(View view) {
            
            DialogUtils.showOkCancelDialog(ShowVideos.this, "Do you really want to remove the video?",
                    new Runnable() {
                public void run() {
                    removePhoto(videoUri);
                    loadVideoUris();
                    ((BaseAdapter) gallery.getAdapter()).notifyDataSetChanged();
                    //videoView.setsetImageBitmap(null);
                }
            },
                    new Runnable() {
                public void run() {
                    // nothing ???
                }
            });
            
            return true;
        }
    }
    //-------------------------------------------------
    
    private void removePhoto(Uri imageUri) {
        removePhotoFromSdCard(imageUri);
        removePhotoFromDatabase(imageUri);
    }
    
    private void removePhotoFromSdCard(Uri imageUri) {
        ContentResolver contentResolver = getContentResolver();
        contentResolver.delete(imageUri, null, null);
    }
    
    private void removePhotoFromDatabase(Uri imageUri) {
        dbAdapter.open();
        dbAdapter.deleteVideoWithUri(imageUri.toString());
        dbAdapter.close();
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        // TODO Auto-generated method stub
        
    }

}
