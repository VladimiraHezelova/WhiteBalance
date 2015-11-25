package bachelorapp.fi.muni.cz.whitebalanceapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by Vladimira Hezelova on 25. 11. 2015.
 */
public class MainActivityTransparent1 extends Activity {

    private ImageButton nextButtonA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout_transparent1);


        nextButtonA = (ImageButton) findViewById(R.id.nextA);
        nextButtonA.setImageResource(R.drawable.ic_navigate_next_black_24dp);

        nextButtonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intentTransparentB = new Intent(getApplicationContext(), MainActivityTransparent.class);
                startActivity(intentTransparentB);
            }
        });


    }

}
