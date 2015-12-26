package bachelorapp.fi.muni.cz.whitebalanceapp.mainActivityTransparent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import bachelorapp.fi.muni.cz.whitebalanceapp.R;

/**
 * Created by Vladimira Hezelova on 25. 11. 2015.
 */
public class MainActivityTransparent1 extends Activity {

    /**
     * Priehladny navod zobrazeny pri prvom spusteni aplikacie
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout_transparent1);

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.main_activity_transparent1);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intentTransparentB = new Intent(getApplicationContext(), MainActivityTransparent2.class);
                startActivity(intentTransparentB);
            }
        });


    }

}
