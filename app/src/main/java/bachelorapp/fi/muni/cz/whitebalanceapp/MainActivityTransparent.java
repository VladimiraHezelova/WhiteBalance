package bachelorapp.fi.muni.cz.whitebalanceapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by Vladimira Hezelova on 22. 10. 2015.
 */
public class MainActivityTransparent extends Activity {

    private static int RESULT_LOAD_IMAGE = 1;

    private String imagePath;
    private Button buttonGalleryB;

    public static Activity mainActivityTransparent2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout_transparent);

        mainActivityTransparent2 = this;

        Intent intent = getIntent();
        imagePath = intent.getStringExtra("imagePath");
        buttonGalleryB = (Button) findViewById(R.id.button_galleryB);
        buttonGalleryB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);

            }
        });

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

            Log.e("path of sourceUri", selectedImage.getPath());

            Intent intent = new Intent(getApplicationContext(), ConvertedPhotos.class);
            intent.putExtra("imagePath", imagePath);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        }

    }
}
