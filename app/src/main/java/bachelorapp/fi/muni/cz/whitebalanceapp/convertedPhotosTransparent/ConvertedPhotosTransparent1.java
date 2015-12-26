package bachelorapp.fi.muni.cz.whitebalanceapp.convertedPhotosTransparent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import bachelorapp.fi.muni.cz.whitebalanceapp.mainActivityTransparent.MainActivityTransparent2;
import bachelorapp.fi.muni.cz.whitebalanceapp.R;

/**
 * Created by Vladimira Hezelova on 22. 10. 2015.
 */
public class ConvertedPhotosTransparent1 extends AppCompatActivity {

    /**
     * Priehladny navod zobrazeny pri prvom spusteni aplikacie
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.converted_photos_layout_transparent1);

        MainActivityTransparent2.mainActivityTransparent2.finish();

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.converted_photos_transparent1);

        ImageView arrow1 = (ImageView) findViewById(R.id.arrow1);
        arrow1.setImageResource(R.drawable.arrow_img);
        arrow1.bringToFront();

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intentTransparent2 = new Intent(getApplicationContext(), ConvertedPhotosTransparent2.class);
                startActivity(intentTransparent2);
            }
        });
    }
}
