package bachelorapp.fi.muni.cz.whitebalanceapp;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import bachelorapp.fi.muni.cz.whitebalanceapp.mainActivityTransparent.MainActivityTransparent1;

/**
 * Prva obrazovka (uvodna aktivita), ktora obsahuje nazov aplikacie a vstup do galerie
 */
public class MainActivity extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 1;

    final String PREFS_NAME = "MyPrefsFile";

    public static Activity mainActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);

        // Zistuje ci bol otvoreny obrazok prilis velky a ma vypisat na obrazovku uzivatelovi aby skusil mensi,
        // moze nastat az po druhotnom nacitani MainAcitivity, po vybere nejakeho obrazku z galerie a nedostatku
        // pamate pri volani nejakej metody v ConevrtedPhotos
        Intent intent = getIntent();
        try {
            if(intent.getStringExtra("tooLargePicture").equals("tooLargePicture")) {
                Toast.makeText(getApplicationContext(), R.string.too_large_image, Toast.LENGTH_SHORT).show();
            }
        } catch (NullPointerException e) {
            Log.i("MainActivity","Intent doesnt have extra string tooLargePicture, because its not about to fall");
        }

        ImageView buttonGallery = (ImageView) findViewById(R.id.button_gallery);
        Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_photo_library_black_48dp);
        buttonGallery.setImageBitmap(icon);

        mainActivity = this;

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        // nastavi sa cely layout klikatelnym,
        // pri kliknuti sa vola onActivityResult()
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

        // zistuje ci je aplikacia spustena prvykrat a teda ci sa ma spustit s navodom
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if(settings.getBoolean("my_first_time", true)) {
            Intent intentTransparentA = new Intent(getApplicationContext(), MainActivityTransparent1.class);
            startActivity(intentTransparentA);
        }
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

    /**
     * Spusti prehliadac obrazkov, uzivatel si zvoli nejaky pre vyvazenie bielej, do imagePath sa vlozi
     * cesta k vybranemu obrazku, vytvori sa Intent druhej aktivity (obrazovky) ConvertedPhotos a cesta
     * k obrazku sa preda ako extra string
     */
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
            String imagePath = cursor.getString(columnIndex);
            cursor.close();

            Intent intent = new Intent(getApplicationContext(), ConvertedPhotos.class);
            intent.putExtra("imagePath", imagePath);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
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
