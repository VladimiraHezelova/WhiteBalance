package bachelorapp.fi.muni.cz.whitebalanceapp;


import android.app.ActivityManager;
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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.algorithms.grayWorld.ConversionGW;
import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.algorithms.histogramStretching.HistogramStretching;
import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.algorithms.subsamplingWP.SubsamplingWB;
import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.algorithms.whitePatch.Conversion;
import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions.PixelData;

import static android.graphics.Bitmap.createScaledBitmap;

/**
 * Created by Vladimira Hezelova on 25. 8. 2015.
 */
public class ConvertedPhotos extends AppCompatActivity {

    private static Context instance;
    private static final String TAG = "ConvertedPhotos";
    private ProgressBar bar;

    private static ImageButton[] imageButtons;
    private static ImageButton selectedImage;

    private static String imagePath;

    private static Bitmap originalBitmap;
    private static int scaledHeight = 0;
    private static int scaledWidth = 0;
    private static Bitmap scaledBitmap;
    private static Bitmap[] convertedBitmaps;
    // index pre ukladanie obrazku
    private static int indexOfSelectedBitmap;

    private double[][] pixelDataOriginal;
    private double[][] pixelDataClone;



    public ConvertedPhotos() {
        instance = this;
        Log.i(TAG,"constructor");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.converted_photos_layout);


        imageButtons = new ImageButton[]{
                (ImageButton) findViewById(R.id.original_image),
                (ImageButton) findViewById(R.id.converted_image1),
                (ImageButton) findViewById(R.id.converted_image2),
                (ImageButton) findViewById(R.id.converted_image3),
                (ImageButton) findViewById(R.id.converted_image4)
        };
        selectedImage = (ImageButton) findViewById(R.id.selected_image);

        //  bar = (ProgressBar) findViewById(R.id.progressBar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        imagePath = intent.getStringExtra("imagePath");

        // free memory
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        int memoryClass = am.getMemoryClass();
        Log.i(TAG, "free memory = " + Integer.toString(memoryClass));

        originalBitmap = BitmapFactory.decodeFile(imagePath);

        changeDimensions();
        scaledBitmap = createScaledBitmap(originalBitmap, scaledWidth, scaledHeight, false);
        originalBitmap = null;
        System.gc();

        new ProgressTask().execute();
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
                // dopis..........................
                // writeImage(selectedBitmap);
                return true;
            case android.R.id.home:
                finish();
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                } else {
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // dopis.....................
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
         //   bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            long start = System.currentTimeMillis();

            pixelDataOriginal = new double[scaledWidth*scaledHeight][3];
            /*
            pixelDataOriginal = PixelData.getPixelData(scaledBitmap, pixelDataOriginal);
            pixelDataClone = new double[scaledWidth*scaledHeight][3];
            deepCopyPixelData();
*/
            /*
            Bitmap convertedBitmap1 = HistogramStretching.conversion(scaledWidth, scaledHeight, pixelDataClone);
            deepCopyPixelData();
            Bitmap convertedBitmap2 = ConversionGW.convert(scaledBitmap, pixelDataClone);
            deepCopyPixelData();
            Bitmap convertedBitmap4 = SubsamplingWB.conversion(scaledWidth, scaledHeight, pixelDataClone);
            deepCopyPixelData();
            */
            convertedBitmaps = new Bitmap[] {
                    null,
                    HistogramStretching.conversion(scaledWidth, scaledHeight, PixelData.getPixelData(scaledBitmap, pixelDataOriginal)),
                    ConversionGW.convert(scaledBitmap, PixelData.getPixelData(scaledBitmap, pixelDataOriginal)),
                    null,
                    SubsamplingWB.conversion(scaledWidth, scaledHeight, PixelData.getPixelData(scaledBitmap, pixelDataOriginal))
            };

            long end = System.currentTimeMillis();
            double time = (double) (end - start) / 1000;
            Log.i(TAG, "time of conversions = " + time + "seconds");
            //56.162 sec,56.188, 47.875, 45.121, 45.746, 45.529 .. 13.5
            // 35.594, 35.697
            //35 MB, 28MB, 35 MB .. 23 MB
            // 50,60 max 80%

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {

            //    bar.setVisibility(View.GONE);

            setBitmapInButton(0, scaledBitmap);
            setBitmapInButton(1);
            setBitmapInButton(2);
            setBitmapInButton(3, scaledBitmap);
            setBitmapInButton(4);

            selectedImage.setImageBitmap(scaledBitmap);
        }
    }

    public void changeDimensions() {
        // dimensions of display
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int widthDisplay = size.x;
        int heightDisplay = size.y;

        Log.i(TAG, "display width: " + Integer.toString(widthDisplay));
        Log.i(TAG, "display height: " + Integer.toString(heightDisplay));

        int widthImage = originalBitmap.getWidth();
        int heightImage = originalBitmap.getHeight();

        Log.i(TAG, "bitmap width: " + Integer.toString(widthImage));
        Log.i(TAG, "bitmap height: " + Integer.toString(heightImage));

        scaledHeight = heightDisplay - 380;
        double ratio = (double)scaledHeight / (double)heightImage;
        scaledWidth = (int)((double)widthImage * ratio);
        Log.i(TAG, "scaled width: " + Integer.toString(scaledWidth));
        Log.i(TAG, "scaled height: " + Integer.toString(scaledWidth));
        //pre mensie obrazky
       // scaledHeight = heightImage;
        //scaledWidth = widthImage;

    }

    public void setBitmapInButton(int index) {
        setBitmapInButton(index, convertedBitmaps[index]);
    }

    public void setBitmapInButton(final int index, final Bitmap bitmap) {
        imageButtons[index].setImageBitmap(bitmap);

        imageButtons[index].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedImage.setImageBitmap(bitmap);
                if (index == 3) {
                    whitePatch();
                }
            }
        });
    }

    public void whitePatch() {
        selectedImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // selected pixel of White
                    int selectedPixelX = (int) event.getX();
                    int selectedPixelY = (int) event.getY();

                    // enlarge of chosen pixel because of noise
                    int shiftedX;
                    int shiftedY;
                    int size = 9;
                    // kontrola zvacsenia vyberu bielych pixelov na okrajoch
                    if (selectedPixelX > 4) {
                        shiftedX = selectedPixelX - 4;
                    } else {
                        shiftedX = 1;
                    }
                    if (selectedPixelX > scaledBitmap.getWidth() - 5) {
                        shiftedX = scaledBitmap.getWidth() - 10;
                    }

                    if (selectedPixelY > 4) {
                        shiftedY = selectedPixelY - 4;
                    } else {
                        shiftedY = 1;
                    }
                    if (selectedPixelY > scaledBitmap.getHeight() - 5) {
                        shiftedY = scaledBitmap.getHeight() - 10;
                    }

                    Bitmap selectedWhite = Bitmap.createBitmap(scaledBitmap, shiftedX, shiftedY, size, size);
                    convertedBitmaps[3] = Conversion.convert(scaledWidth, scaledHeight, selectedWhite, PixelData.getPixelData(scaledBitmap, pixelDataOriginal));

                    selectedImage.setImageBitmap(convertedBitmaps[3]);
                    setBitmapInButton(3);

                    // selectedImage.setOnTouchListener(null);
                    return true;
                } else
                    return false;
            }
        });
    }

    public void deepCopyPixelData() {
        for (int i = 0; i < pixelDataOriginal.length; i++) {
            pixelDataClone[i] = Arrays.copyOf(pixelDataOriginal[i], pixelDataOriginal[i].length);
        }
    }
}


