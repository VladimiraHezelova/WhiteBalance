package bachelorapp.fi.muni.cz.whitebalanceapp;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Vladkaa on 25. 8. 2015.
 */
public class ConvertedPhotosFragment extends ActionBarActivity {

    private static ImageButton originalImage;
    private static ImageButton convertedImage1;
    private static ImageButton convertedImage2;
    private static ImageButton convertedImage3;
    private static ImageButton convertedImage4;
    private static ImageButton convertedImage5;
    private static ImageView selectedImage;
    private static Bitmap selectedBitmap;
    private static Context instance;

   // private String picturePath;


    public ConvertedPhotosFragment() {
        instance = this;
       // convertedPhoto = (ImageView) findViewById(R.id.converted_photo);
        Log.e("log","ConvertedPhotosFragment");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_converted_photos);
        Log.e("log","create2");

        ActionBar actionBar = getSupportActionBar();
        /*
        actionBar.setLogo(R.drawable.icon_settings);
*/
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        final String picturePath = intent.getStringExtra("picturePath");
        selectedImage = (ImageView) findViewById(R.id.selected_image);
        final Bitmap originalBitmap = BitmapFactory.decodeFile(picturePath);
        selectedImage.setImageBitmap(originalBitmap);

        originalImage = (ImageButton) findViewById(R.id.original_image);
        originalImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        originalImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedBitmap = originalBitmap;
                selectedImage.setImageBitmap(selectedBitmap);
            }
        });

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(picturePath, bmOptions);
        final Bitmap convertedBitmap1 = SubsamplingWB.conversion(bitmap);

        convertedImage1 = (ImageButton) findViewById(R.id.converted_image1);
        convertedImage1.setImageBitmap(convertedBitmap1);

        convertedImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "convertedImage1", Toast.LENGTH_SHORT).show();
                Log.e("convertedImage1", picturePath);
                selectedBitmap = convertedBitmap1;
                selectedImage.setImageBitmap(selectedBitmap);
              //  writeImage(convertedBitmap1);
            }
        });


        convertedImage2 = (ImageButton) findViewById(R.id.converted_image2);
        convertedImage2.setImageBitmap(BitmapFactory.decodeFile("/storage/sdcard1/DCIM/MobileCamera/DSC_0006.JPEG"));
/*
        convertedImage3 = (ImageButton) findViewById(R.id.converted_image3);
        convertedImage3.setImageBitmap(BitmapFactory.decodeFile(picturePath));
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
            case R.id.action_settings:
                openSettings();
                return true;
            case R.id.action_download:
                writeImage(selectedBitmap);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openSettings() {
        Toast.makeText(getApplicationContext(), "settings", Toast.LENGTH_SHORT).show();
    }


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
}