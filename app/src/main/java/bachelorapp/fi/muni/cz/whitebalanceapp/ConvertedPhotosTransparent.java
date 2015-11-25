package bachelorapp.fi.muni.cz.whitebalanceapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

/**
 * Created by Vladimira Hezelova on 22. 10. 2015.
 */
public class ConvertedPhotosTransparent extends AppCompatActivity {


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

    private ImageView arrow1;
  //  private ImageView arrow2;

    private ImageButton nextButton;



    public ConvertedPhotosTransparent() {
        instance = this;
        // convertedPhoto = (ImageView) findViewById(R.id.converted_photo);
        Log.e("log","ConvertedPhotosTransparent");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.converted_photos_layout_transparent);

        arrow1 = (ImageView) findViewById(R.id.arrow1);
        arrow1.setImageResource(R.drawable.arrow_image);
        arrow1.bringToFront();
      //  arrow2 = (ImageView) findViewById(R.id.arrow2);

        nextButton = (ImageButton) findViewById(R.id.next);
        nextButton.setImageResource(R.drawable.ic_navigate_next_black_24dp);
/*
        Intent intent = getIntent();
        Bitmap convertedBitmap1 = (Bitmap)intent.getParcelableExtra("convertedBitmap1");

        convertedImage1 = (ImageButton) findViewById(R.id.converted_image1_transparent);
        convertedImage1.setImageBitmap(convertedBitmap1);
*/
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intentTransparent2 = new Intent(getApplicationContext(), ConvertedPhotosTransparent2.class);
                startActivity(intentTransparent2);
            }
        });
    }
}
