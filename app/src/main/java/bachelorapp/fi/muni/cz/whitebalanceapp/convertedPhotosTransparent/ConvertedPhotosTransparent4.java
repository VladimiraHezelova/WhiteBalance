package bachelorapp.fi.muni.cz.whitebalanceapp.convertedPhotosTransparent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import bachelorapp.fi.muni.cz.whitebalanceapp.R;

/**
 * Created by Vladimira Hezelova on 25. 11. 2015.
 */
public class ConvertedPhotosTransparent4 extends AppCompatActivity {

    /**
     * Priehladny navod zobrazeny pri prvom spusteni aplikacie
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.converted_photos_layout_transparent4);

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.converted_photos_transparent4);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

}
