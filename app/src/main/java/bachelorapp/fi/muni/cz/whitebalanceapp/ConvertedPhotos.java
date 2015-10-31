package bachelorapp.fi.muni.cz.whitebalanceapp;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.algorithms.grayWorld.ConversionGW;

import static android.graphics.Bitmap.createScaledBitmap;

/**
 * Created by Vladkaa on 25. 8. 2015.
 */
public class ConvertedPhotos extends ActionBarActivity {

    private static ImageButton originalImage;
    private static ImageButton convertedImage1;
    private static ImageButton convertedImage2;
    private static ImageButton convertedImage3;
    private static ImageButton convertedImage4;
    private static ImageButton convertedImage5;
    private static ImageView selectedImage;
    private static Bitmap selectedBitmap;
    private static Context instance;

    private static String picturePath;

    private ProgressBar bar;

    private Bitmap convertedBitmap1;
    private Bitmap bitmap;
    private Bitmap originalBitmap;
    private Bitmap originalBitmapTMP;

    private int height;
    private int width;

    private Bitmap histogramStretchedBitmap;


    // private String picturePath;


    public ConvertedPhotos() {
        instance = this;
       // convertedPhoto = (ImageView) findViewById(R.id.converted_photo);
        Log.e("log","ConvertedPhotosFragment");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.converted_photos_layout);


        bar = (ProgressBar) findViewById(R.id.progressBar);
        Log.e("log","create2");

        ActionBar actionBar = getSupportActionBar();
        /*
        actionBar.setLogo(R.drawable.icon_settings);
*/
        if (actionBar != null) {
            actionBar.setDisplayUseLogoEnabled(true);
        }
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
       // final String picturePath = intent.getStringExtra("picturePath");
        picturePath = intent.getStringExtra("picturePath");
        selectedImage = (ImageView) findViewById(R.id.selected_image);
        originalBitmapTMP = BitmapFactory.decodeFile(picturePath);
        // rozmery displayu
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int widthDisplay = size.x;
        int heightDisplay = size.y;

        Log.e("display width: ", Integer.toString(widthDisplay));
        Log.e("display height: ", Integer.toString(heightDisplay));

        int widthImage = originalBitmapTMP.getWidth();
        int heightImage = originalBitmapTMP.getHeight();
        Log.e("bitmap width: ", Integer.toString(widthImage));
        Log.e("bitmap height: ", Integer.toString(heightImage));


        height = heightDisplay - 380;
        Log.e("heightDisplay - 380", Double.toString(height));
        double ratio = (double)height / (double)heightImage;
        Log.e("ratio", Double.toString(ratio));
        width = (int)((double)widthImage * ratio);
        Log.e("width", Integer.toString(width));



       // BitmapFactory.Options bmOptions = new BitmapFactory.Options();
     //   Bitmap bitmapTMP = BitmapFactory.decodeFile(picturePath, bmOptions);
        bitmap = createScaledBitmap(originalBitmapTMP, width, height, false);

       // histogramStretchedBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        new ProgressTask().execute();




        convertedImage2 = (ImageButton) findViewById(R.id.converted_image2);
        convertedImage2.setImageBitmap(BitmapFactory.decodeFile("/storage/sdcard1/DCIM/MobileCamera/DSC_0006.JPEG"));
/*
        convertedImage3 = (ImageButton) findViewById(R.id.converted_image3);
        convertedImage3.setImageBitmap(BitmapFactory.decodeFile(picturePath));
*/
/*
        //prekrytie layoutu navodom
        Intent intentTransparent = new Intent(getApplicationContext(), ConvertedPhotosTransparent.class);
        intentTransparent.putExtra("convertedBitmap1", convertedBitmap1);
        startActivity(intentTransparent);
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_converted_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_download:
                writeImage(selectedBitmap);
                return true;
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                                    // Navigate up to the closest parent
                            .startActivities();
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
/*
    public void openSettings() {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("picturePath", picturePath);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
*/

    public static void writeImage(Bitmap image) {
        Date date = new Date();
        String sDate = new SimpleDateFormat("yyyyMMdd_hhmmss").format(date);

        Log.e("time", sDate);
        String destinationFilename = android.os.Environment.getExternalStorageDirectory().getPath()+ File.separatorChar +
                "DCIM" + File.separatorChar + "resources" + File.separatorChar + sDate + ".jpeg";

        Log.e("destinationFilename", destinationFilename);

        BufferedOutputStream bos = null;

        try {
            bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
            image.compress(Bitmap.CompressFormat.PNG, 100, bos);
            Toast.makeText(instance.getApplicationContext(), "Saved succesfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ProgressTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute(){
            bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
        //    convertedBitmap1 = SubsamplingWB.conversion(bitmap);
         //   convertedBitmap1 = HistogramStretching.conversion(bitmap);
            convertedBitmap1 = ConversionGW.convert(bitmap);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            bar.setVisibility(View.GONE);

            originalBitmap = bitmap;
            originalImage = (ImageButton) findViewById(R.id.original_image);
            originalImage.setImageBitmap(originalBitmap);

            selectedImage.setImageBitmap(originalBitmap);
            originalImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedImage.setImageBitmap(originalBitmap);
                }
            });
            convertedImage1 = (ImageButton) findViewById(R.id.converted_image1);
            convertedImage1.setImageBitmap(convertedBitmap1);

            convertedImage1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "convertedImage1", Toast.LENGTH_SHORT).show();
                    Log.e("convertedImage1", picturePath);
                    selectedImage.setImageBitmap(convertedBitmap1);
                    //  writeImage(convertedBitmap1);
                }
            });
        }
    }
}