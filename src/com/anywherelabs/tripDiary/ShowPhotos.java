package com.anywherelabs.tripDiary;

import com.anywherelabs.tripDiary.data.PhotoBean;
import com.anywherelabs.tripDiary.data.TripPointBean;
import com.anywherelabs.tripDiary.database.DbAdapter;
import com.anywherelabs.tripDiary.util.activity.ActivityUtils;
import com.anywherelabs.tripDiary.util.media.photo.PhotoUtils;
import com.anywherelabs.util.dialog.DialogUtils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ShowPhotos extends Activity {

    // Constants
    private static final String CLASS_NAME = ShowPhotos.class.toString();
    private static final String TITLE_SEPARATOR = " - ";
    
    
    // Attributes
    private TripPointBean tripPoint;
    private TextView titleText;
    private Gallery gallery;
    private ImageView photoView;
    private List<Uri> imageUris;
    private DbAdapter dbAdapter;

    
    // Methods
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(CLASS_NAME,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_photos);

        init(savedInstanceState);
        Log.d(CLASS_NAME,"Init done");
    }
    
    private void init(Bundle savedInstanceState) {
        dbAdapter = new DbAdapter(this);
        
        tripPoint = ActivityUtils.recoverTripPoint(savedInstanceState, this);
        assert tripPoint!=null : "TripPoint can NOT be null";
        
        titleText = (TextView) findViewById(R.id.showPhotos_Title);
        gallery = (Gallery) findViewById(R.id.showPhotos_Gallery);
        photoView = (ImageView) findViewById(R.id.showPhotos_Photo);
        
        titleText.setText(tripPoint.getName() + TITLE_SEPARATOR 
                + getString(R.string.showPhotos_Photos));
        
        initGallery();
    }
    
    private void initGallery() {
        loadImageUris();
        gallery.setAdapter(new ImageAdapter());
        gallery.setOnItemClickListener(new ItemClickListener());
    }
    
    private void loadImageUris() {
        List<PhotoBean> photos = PhotoUtils.getPhotosFromTripPointId(this, tripPoint.getId());
        imageUris = new ArrayList<Uri>();
        for (PhotoBean photo : photos) {
            imageUris.add(photo.getUri());
        }
    }
    
    // ImageAdapter class
    //--------------------------------------------
    private class ImageAdapter extends BaseAdapter {

        // Attributes
        private int itemBackground;
        
        // Constructor
        public ImageAdapter() {
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
            return imageUris.size();
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
            ImageView imageView = new ImageView(ShowPhotos.this);
            Uri imageUri = imageUris.get(position);
            imageView.setImageURI(imageUri);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(new Gallery.LayoutParams(150,120));
            imageView.setBackgroundResource(itemBackground);
            return imageView;
        }
        
    }
    
    // ItemClickListener
    //--------------------------------------------
    private class ItemClickListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView parentView, View view, int position, long id) {
            Uri imageUri = imageUris.get(position);
            photoView.setImageURI(imageUri);
            photoView.setOnLongClickListener(new GalleryImageOnLongClickListener(imageUri));
        }
    }
    
    // GalleryImage OnLongClick Listener class
    //--------------------------------------------
    private class GalleryImageOnLongClickListener implements OnLongClickListener {

        // Attributes
        private Uri imageUri;
        
        // Constructors
        public GalleryImageOnLongClickListener(Uri imageUri) {
            this.imageUri = imageUri;
        }
        
        @Override
        public boolean onLongClick(View view) {
            
            DialogUtils.showOkCancelDialog(ShowPhotos.this, "Do you really want to remove the photo?",
                    new Runnable() {
                public void run() {
                    removePhoto(imageUri);
                    loadImageUris();
                    ((BaseAdapter) gallery.getAdapter()).notifyDataSetChanged();
                    photoView.setImageBitmap(null);
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
        dbAdapter.deletePhotoWithUri(imageUri.toString());
        dbAdapter.close();
    }
    
}
