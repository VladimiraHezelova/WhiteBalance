package bachelorapp.fi.muni.cz.whitebalanceapp;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 1;

    private ImageView buttonGallery;
    private TextView textGallery;
    private String imagePath;


    final String PREFS_NAME = "MyPrefsFile";
    private boolean firstStart = true;

    public static Activity mainActivity;
    private Camera camera = null;
    private int cameraWidth = 0;
    private int cameraHeight = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);


        buttonGallery = (ImageView) findViewById(R.id.button_gallery);
        Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_photo_library_black_48dp);
        buttonGallery.setImageBitmap(icon);
        textGallery = (TextView) findViewById(R.id.button_text);

        mainActivity = this;

        // zistuje ci je aplikacia spustena prvykrat

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if(settings.getBoolean("my_first_time", true)) {
            Log.e("message", "first time start of app");
        } else {
            Log.e("message", "this is not first time start of app");
        }
        if(settings.getBoolean("my_first_time", true)) {
            //the app is being launched for first time, do something
            Log.d("Comments", "First time");

            // first time task

            // record the fact that the app has been started at least once
         //   settings.edit().putBoolean("my_first_time", false).commit();
        }


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        Intent intent = getIntent();
        // final String picturePath = intent.getStringExtra("picturePath");
        imagePath = intent.getStringExtra("imagePath");

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.container);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View view) {
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                }
        });
        if(settings.getBoolean("my_first_time", true)) {
            Intent intentTransparentA = new Intent(getApplicationContext(), MainActivityTransparent1.class);
            startActivity(intentTransparentA);
        }
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_main_layout, container, false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imagePath = cursor.getString(columnIndex);
            cursor.close();
            if(isImageTooLarge()) {
                Log.e("IAMGE", "TOO LARGE");
                Toast.makeText(getApplicationContext(), R.string.too_large_image, Toast.LENGTH_SHORT).show();

            } else {
                Log.e("IAMGE", "has fine size");

                Log.e("path of sourceUri", selectedImage.getPath());

                Intent intent = new Intent(getApplicationContext(), ConvertedPhotos.class);
                intent.putExtra("imagePath", imagePath);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }

        }

    }

    public boolean isImageTooLarge() {

        try {
            camera = Camera.open(0);
            android.hardware.Camera.Parameters parameters = camera.getParameters();
            List<Camera.Size> params = parameters.getSupportedPictureSizes();
            Camera.Size a = params.get(0);
            cameraWidth = a.width;
            cameraHeight = a.height;
            camera.release();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }


        Log.e("height for CAMERA", Integer.toString(cameraHeight));
        Log.e("width for CAMERA", Integer.toString(cameraWidth));

        int imageWidth;
        int imageHeight;
        try {
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inJustDecodeBounds = true;
            InputStream inputStream = new FileInputStream(imagePath);
            BitmapFactory.decodeStream(inputStream, null, bitmapOptions);
            imageWidth = bitmapOptions.outWidth;
            imageHeight = bitmapOptions.outHeight;
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                return true;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return true;
        }


        if(cameraWidth*cameraHeight >= (imageWidth*imageHeight - 100)) {
            Log.e("MainAcitivty","IMAGE has good size");
            return false;
        } else {
            Log.e("MainAcitivty","TOO LARGE IMAGE");
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

}
