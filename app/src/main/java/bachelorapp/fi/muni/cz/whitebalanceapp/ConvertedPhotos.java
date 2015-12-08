package bachelorapp.fi.muni.cz.whitebalanceapp;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.Filter;
import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.algorithms.grayWorld.GrayWorld;
import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.algorithms.histogramStretching.HistogramStretching;
import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.algorithms.improvedWP.ImprovedWP;
import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.algorithms.whitePatch.WhitePatch;

import static android.graphics.Bitmap.createScaledBitmap;

/**
 * Created by Vladimira Hezelova on 25. 8. 2015.
 */
public class ConvertedPhotos extends AppCompatActivity {

    private Filter filter;
    private Context instance;
    private final String TAG = "ConvertedPhotos";
    private ProgressBar waitingCircle;

    private ImageButton[] imageButtons;
    private ImageButton selectedImage;

    private String imagePath;
    private Bitmap originalBitmap;
    private int scaledHeight = 0;
    private int scaledWidth = 0;
    private Bitmap scaledBitmap;
    private Bitmap[] convertedBitmaps;

    // for coordinates for WhitePatch
    // enlarge of chosen pixel because of noise
    private int shiftedX;
    private int shiftedY;
    private int size = 9;
    private Bitmap selectedWhite;

    private ImageButton iconWP;
    private boolean convertedWP;
    private TextView textWP;

    final String PREFS_NAME = "MyPrefsFile";

    public ConvertedPhotos() {
        instance = this;
        Log.i(TAG,"constructor");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.converted_photos_layout);

        MainActivity.mainActivity.finish();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.actionbar);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        waitingCircle = (ProgressBar) findViewById(R.id.progressBar);

        imageButtons = new ImageButton[]{
                (ImageButton) findViewById(R.id.original_image),
                (ImageButton) findViewById(R.id.converted_image1),
                (ImageButton) findViewById(R.id.converted_image2),
                (ImageButton) findViewById(R.id.converted_image3),
                (ImageButton) findViewById(R.id.converted_image4)
        };
        selectedImage = (ImageButton) findViewById(R.id.selected_image);
        setWPThumbNail();
        filter = Filter.ORIGINAL_IMAGE;

        Intent intent = getIntent();
        imagePath = intent.getStringExtra("imagePath");
        originalBitmap = BitmapFactory.decodeFile(imagePath);
        changeDimensions();
        scaledBitmap = createScaledBitmap(originalBitmap, scaledWidth, scaledHeight, false);

        // free memory
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        int memoryClass = am.getMemoryClass();
        Log.i(TAG, "free memory = " + Integer.toString(memoryClass));
/*
        if(!originalBitmap.isRecycled() && !originalBitmap.sameAs(scaledBitmap)) {
            originalBitmap.recycle();
        }
*/
        new ProgressTask().execute();
    }

    private void setWPThumbNail() {
        convertedWP = false;
        iconWP = (ImageButton) findViewById(R.id.icon_wp);
        Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.finger_icon);
        iconWP.setImageBitmap(icon);
        iconWP.setVisibility(View.GONE);
        textWP = (TextView) findViewById(R.id.text_wp);
        textWP.setVisibility(View.GONE);
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

            case R.id.action_WP:
                if(convertedWP) {
                    Toast.makeText(instance.getApplicationContext(), R.string.new_selected_white, Toast.LENGTH_SHORT).show();

                    filter = Filter.WHITE_PATCH;
                    imageButtons[Filter.WHITE_PATCH.ordinal()].setImageBitmap(scaledBitmap);
                    iconWP.setVisibility(View.VISIBLE);
                    textWP.setVisibility(View.VISIBLE);

                    selectedImage.setImageBitmap(scaledBitmap);
                    convertedWP = false;
                    selectedImage.setClickable(true);
                    iconWP.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            filter = Filter.WHITE_PATCH;
                            if(!convertedWP) {
                                imageButtons[Filter.WHITE_PATCH.ordinal()].setImageBitmap(scaledBitmap);
                                textWP.setVisibility(View.VISIBLE);
                                selectedImage.setImageBitmap(scaledBitmap);
                                selectedImage.setClickable(true);
                            } else {
                                imageButtons[Filter.WHITE_PATCH.ordinal()].setImageBitmap(convertedBitmaps[Filter.WHITE_PATCH.ordinal()]);
                                selectedImage.setImageBitmap(convertedBitmaps[Filter.WHITE_PATCH.ordinal()]);
                            }
                        }
                    });
                    imageButtons[Filter.WHITE_PATCH.ordinal()].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            filter = Filter.WHITE_PATCH;
                            if(!convertedWP) {
                                imageButtons[Filter.WHITE_PATCH.ordinal()].setImageBitmap(scaledBitmap);
                                textWP.setVisibility(View.VISIBLE);
                                selectedImage.setImageBitmap(scaledBitmap);
                                selectedImage.setClickable(true);
                            } else {
                                imageButtons[Filter.WHITE_PATCH.ordinal()].setImageBitmap(convertedBitmaps[Filter.WHITE_PATCH.ordinal()]);
                                selectedImage.setImageBitmap(convertedBitmaps[Filter.WHITE_PATCH.ordinal()]);
                            }
                        }
                    });

                } else {
                    selectedImage.setImageBitmap(convertedBitmaps[Filter.WHITE_PATCH.ordinal()]);
                }
                return true;

            case R.id.action_download:
                if(filter == Filter.ORIGINAL_IMAGE) {
                    Toast.makeText(instance.getApplicationContext(), R.string.save_message1, Toast.LENGTH_SHORT).show();
                } else {
                    System.gc();
                    new writeImage().execute();
                }
                return true;

            case android.R.id.home:
                finish();
                Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainActivityIntent);
                recycleBitmaps();
                /*
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                } else {
                    NavUtils.navigateUpTo(this, upIntent);
                }
                */
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void recycleBitmaps() {
        if((originalBitmap != null) && (!originalBitmap.isRecycled())) {
            originalBitmap.recycle();
        }
        if((scaledBitmap != null) && (!scaledBitmap.isRecycled())) {
            scaledBitmap.recycle();
        }
        for(int i = 0; i < convertedBitmaps.length; i++) {
            if((convertedBitmaps[i] != null) && (!convertedBitmaps[i].isRecycled())) {
                convertedBitmaps[i].recycle();
            }
        }
        if((selectedWhite!= null) && (!selectedWhite.isRecycled())) {
            selectedWhite.recycle();
        }
    }


    private class writeImage extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), R.string.save_message3, Toast.LENGTH_SHORT).show();
            waitingCircle.setVisibility(View.VISIBLE);
            waitingCircle.bringToFront();
        }

        @Override
        protected Void doInBackground(Void... params) {
            long start = System.currentTimeMillis();

            if(isExternalStorageWritable()) {
                if(filter == Filter.HISTOGRAM_STRETCHING) {
                    HistogramStretching histogramStretchingS = new HistogramStretching(originalBitmap);
                    Bitmap convertedBitmap = histogramStretchingS.getConvertedBitmap();
                    saveImage(convertedBitmap);
                } else if(filter == Filter.GRAY_WORLD) {
                    GrayWorld grayWorldS = new GrayWorld(originalBitmap);
                    Bitmap convertedBitmap = grayWorldS.getConvertedBitmap();
                    saveImage(convertedBitmap);
                } else if(filter == Filter.WHITE_PATCH) {
                    WhitePatch whitePatchS = new WhitePatch(originalBitmap, selectedWhite);
                    Bitmap convertedBitmap = whitePatchS.getConvertedBitmap();
                    saveImage(convertedBitmap);
                } else if(filter == Filter.IMPROVED_WP) {
                    ImprovedWP improvedWPS = new ImprovedWP(originalBitmap);
                    Bitmap convertedBitmap = improvedWPS.getConvertedBitmap();
                    saveImage(convertedBitmap);
                } else {
                    Toast.makeText(instance.getApplicationContext(), R.string.save_message1, Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e(TAG, "External storage is not writable");
            }

            long end = System.currentTimeMillis();
            double time = (double) (end - start) / 1000;
            Log.e(TAG, "time of algorithm's conversion = " + time + "seconds");

        /*
        if((indexOfSelectedBitmap == 3) && convertedBitmaps[indexOfSelectedBitmap] == null) {
            Toast.makeText(instance.getApplicationContext(), R.string.save_message1, Toast.LENGTH_SHORT).show();
        } else if(convertedBitmaps[indexOfSelectedBitmap].sameAs(originalBitmap)) {
            Toast.makeText(instance.getApplicationContext(), R.string.save_message1, Toast.LENGTH_SHORT).show();
        } else {
            String destinationFilename = getFilename();
            Log.i("destinationFilename", destinationFilename);

            BufferedOutputStream bos = null;
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(destinationFilename, false);
                bos = new BufferedOutputStream(fos);

                Log.e("out before", Integer.toString(convertedBitmaps[indexOfSelectedBitmap].getRowBytes() * convertedBitmaps[indexOfSelectedBitmap].getHeight()));
                convertedBitmaps[indexOfSelectedBitmap].compress(Bitmap.CompressFormat.JPEG, 50, bos);
                Log.e("out after", Integer.toString(convertedBitmaps[indexOfSelectedBitmap].getRowBytes() * convertedBitmaps[indexOfSelectedBitmap].getHeight()));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Intent mediaScanIntent = new Intent(
                            Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri contentUri = Uri.fromFile(new File(destinationFilename));
                    mediaScanIntent.setData(contentUri);
                    this.sendBroadcast(mediaScanIntent);
                } else {
                    sendBroadcast(new Intent(
                            Intent.ACTION_MEDIA_MOUNTED,
                            Uri.parse("file://"
                                    + Environment.getExternalStorageDirectory())));
                }

                Toast.makeText(instance.getApplicationContext(),R.string.save_message2, Toast.LENGTH_SHORT).show();
                //  convertedBitmaps[indexOfSelectedBitmap].recycle();
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
*/
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            waitingCircle.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), R.string.save_message2, Toast.LENGTH_SHORT).show();
        }
    }

    public void saveImage(Bitmap convertedBitmap) {
        String destinationFilename = getFilename();
        Log.i("destinationFilename", destinationFilename);

        BufferedOutputStream bos = null;
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(destinationFilename, false);
            bos = new BufferedOutputStream(fos);

            Log.e("out before compress", Integer.toString(convertedBitmap.getRowBytes() * convertedBitmap.getHeight()));
            convertedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            Log.e("out after compress", Integer.toString(convertedBitmap.getRowBytes() * convertedBitmap.getHeight()));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Intent mediaScanIntent = new Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(new File(destinationFilename));
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
            } else {
                sendBroadcast(new Intent(
                        Intent.ACTION_MEDIA_MOUNTED,
                        Uri.parse("file://"
                                + Environment.getExternalStorageDirectory())));
            }
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
            waitingCircle.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            long start = System.currentTimeMillis();

            HistogramStretching histogramStretching = new HistogramStretching(scaledBitmap);
            GrayWorld grayWorld = new GrayWorld(scaledBitmap);
            ImprovedWP improvedWP = new ImprovedWP(scaledBitmap);

            convertedBitmaps = new Bitmap[] {
                    null,
                    histogramStretching.getConvertedBitmap(),
                    grayWorld.getConvertedBitmap(),
                    null,
                    improvedWP.getConvertedBitmap()
            };

            long end = System.currentTimeMillis();
            double time = (double) (end - start) / 1000;
            Log.i(TAG, "time of conversions = " + time + "seconds");

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            waitingCircle.setVisibility(View.GONE);

            setBitmapInButton(Filter.ORIGINAL_IMAGE, scaledBitmap);
            setBitmapInButton(Filter.HISTOGRAM_STRETCHING);
            setBitmapInButton(Filter.GRAY_WORLD);
            setBitmapInButton(Filter.WHITE_PATCH, scaledBitmap);
            setBitmapInButton(Filter.IMPROVED_WP);

            selectedImage.setImageBitmap(scaledBitmap);

            // find out if app is starting first time
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            if(settings.getBoolean("my_first_time", true)) {
                Log.i(TAG, "First time start of app");
                Intent intentTransparent = new Intent(getApplicationContext(), ConvertedPhotosTransparent1.class);
                startActivity(intentTransparent);
                settings.edit().putBoolean("my_first_time", false).commit();
            }
        }
    }

    public void changeDimensions() {
        // dimensions of display
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int widthDisplay = size.x;
        int heightDisplay = size.y;
        int widthDisplayDp = pxToDp(widthDisplay);
        int heightDisplayDp = pxToDp(heightDisplay);

        Log.i(TAG, "display width in px: " + Integer.toString(widthDisplay));
        Log.i(TAG, "display height in px: " + Integer.toString(heightDisplay));
        Log.i(TAG, "display width in dp: " + Integer.toString(widthDisplayDp));
        Log.i(TAG, "display height in dp: " + Integer.toString(heightDisplayDp));

        int widthImage = originalBitmap.getWidth();
        int widthImageDp = pxToDp(widthImage);
        int heightImage = originalBitmap.getHeight();
        int heightImageDp = pxToDp(heightImage);

        Log.i(TAG, "bitmap width in px: " + Integer.toString(widthImage));
        Log.i(TAG, "bitmap height in px: " + Integer.toString(heightImage));
        Log.i(TAG, "bitmap width in dp: " + Integer.toString(widthImageDp));
        Log.i(TAG, "bitmap height in dp: " + Integer.toString(heightImageDp));

        //-200 nepada
        if(heightDisplay - 600 >= heightImage && widthDisplay >= widthImage) {
            scaledHeight = heightImage;
            scaledWidth = widthImage;
        } else {
            scaledHeight = heightDisplay - 600;
            double ratio = (double)scaledHeight / (double)heightImage;
            scaledWidth = (int)((double)widthImage * ratio);
        }
        Log.i(TAG, "scaled width: " + Integer.toString(scaledWidth));
        Log.i(TAG, "scaled height: " + Integer.toString(scaledHeight));
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.ydpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.ydpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    public void setBitmapInButton(Filter selectedFilter) {
        setBitmapInButton(selectedFilter, convertedBitmaps[selectedFilter.ordinal()]);
    }

    public void setBitmapInButton(final Filter selectedFilter, final Bitmap bitmap) {
        imageButtons[selectedFilter.ordinal()].setImageBitmap(bitmap);

        if(selectedFilter == Filter.WHITE_PATCH) {
            selectedImage.setImageBitmap(bitmap);
            if(!convertedWP) {
                iconWP.setVisibility(View.VISIBLE);
            }
            iconWP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    filter = selectedFilter;
                    if(!convertedWP) {
                        imageButtons[selectedFilter.ordinal()].setImageBitmap(bitmap);
                        textWP.setVisibility(View.VISIBLE);
                        selectWhite();
                    } else {
                        selectedImage.setImageBitmap(convertedBitmaps[Filter.WHITE_PATCH.ordinal()]);
                    }
                }
            });
            imageButtons[Filter.WHITE_PATCH.ordinal()].setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      filter = selectedFilter;
                      if(!convertedWP) {
                          imageButtons[selectedFilter.ordinal()].setImageBitmap(bitmap);
                          textWP.setVisibility(View.VISIBLE);
                          selectWhite();
                      } else {
                          selectedImage.setImageBitmap(convertedBitmaps[Filter.WHITE_PATCH.ordinal()]);
                      }
                  }
            });
        } else {
            imageButtons[selectedFilter.ordinal()].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textWP.setVisibility(View.GONE);
                    filter = selectedFilter;
                    selectedImage.setImageBitmap(bitmap);
                    selectedImage.setClickable(false);
                }
            });
        }
    }

    public void selectWhite() {
        selectedImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    iconWP.setVisibility(View.GONE);
                    textWP.setVisibility(View.GONE);
                    convertedWP = true;

                    // selected pixel of White
                    int selectedPixelX = (int) event.getX();
                    int selectedPixelY = (int) event.getY();

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
                    selectedWhite = Bitmap.createBitmap(scaledBitmap, shiftedX, shiftedY, size, size);
                //    selectedImage.setOnTouchListener(null);
                    selectedImage.setClickable(false);
                    new ProgressTask2().execute();
                    return true;
                } else
                    return false;
            }
        });
    }

    private class ProgressTask2 extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            waitingCircle.setVisibility(View.VISIBLE);
            waitingCircle.bringToFront();
        }

        @Override
        protected Void doInBackground(Void... params) {
            WhitePatch whitePatch = new WhitePatch(scaledBitmap, selectedWhite);
            convertedBitmaps[3] = whitePatch.getConvertedBitmap();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            waitingCircle.setVisibility(View.GONE);
            selectedImage.setImageBitmap(convertedBitmaps[3]);
            setBitmapInButton(Filter.WHITE_PATCH);
        }
    }

    public String getFilename() {
        Date date = new Date();
        String sDate = new SimpleDateFormat("yyyyMMdd_hhmmss").format(date);

        String path = new File(imagePath).getParent();

        String fileNameWithExtension = new File(imagePath).getName();
        String fileName = sDate;
        String extension = "jpeg";
        int pos = fileNameWithExtension.lastIndexOf(".");
        if (pos > 0) {
            fileName = fileNameWithExtension.substring(0, pos);
            extension = fileNameWithExtension.substring(pos);
        }
        String destinationFilename = path + File.separatorChar + fileName + sDate + extension;
        Log.i("destinationFilename", destinationFilename);
        return destinationFilename;

    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

}



