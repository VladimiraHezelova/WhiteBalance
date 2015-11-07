package bachelorapp.fi.muni.cz.whitebalanceapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

/**
 * Created by Vladimira Hezelova on 22. 10. 2015.
 */
public class ConvertedPhotosTransparent extends ActionBarActivity {


    private static ImageButton originalImage;
    private static ImageButton convertedImage1;
    private static ImageButton convertedImage2;
    private static ImageButton convertedImage3;
    private static ImageButton convertedImage4;
    private static ImageButton convertedImage5;
    private static ImageView selectedImage;
    private static Bitmap selectedBitmap;
    private static Context instance;

    private static String imagePath;

    private ProgressBar bar;

    private Bitmap convertedBitmap1;
    private Bitmap bitmap;



    public ConvertedPhotosTransparent() {
        instance = this;
        // convertedPhoto = (ImageView) findViewById(R.id.converted_photo);
        Log.e("log","ConvertedPhotosFragment");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.converted_photos_layout_transparent);

        Intent intent = getIntent();
        Bitmap convertedBitmap1 = (Bitmap)intent.getParcelableExtra("convertedBitmap1");

        convertedImage1 = (ImageButton) findViewById(R.id.converted_image1_transparent);
        convertedImage1.setImageBitmap(convertedBitmap1);



    }
}
