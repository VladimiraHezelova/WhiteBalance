package bachelorapp.fi.muni.cz.whitebalanceapp.convertedPhotosTransparent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import bachelorapp.fi.muni.cz.whitebalanceapp.R;

/**
 * Created by Vladimira Hezelova on 25. 11. 2015.
 */
public class ConvertedPhotosTransparent3 extends AppCompatActivity {

    /**
     * Priehladny navod zobrazeny pri prvom spusteni aplikacie
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.converted_photos_layout_transparent3);

        ImageView arrow5 = (ImageView) findViewById(R.id.arrow5);
        arrow5.setImageResource(R.drawable.arrow_img);
        arrow5.bringToFront();
        ImageView arrow6 = (ImageView) findViewById(R.id.arrow6);
        arrow6.setImageResource(R.drawable.arrow_img);
        arrow6.bringToFront();

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.converted_photos_transparent3);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intentTransparent4 = new Intent(getApplicationContext(), ConvertedPhotosTransparent4.class);
                startActivity(intentTransparent4);

            }
        });

    }
}
