package bachelorapp.fi.muni.cz.whitebalanceapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by Vladimira Hezelova on 25. 11. 2015.
 */
public class ConvertedPhotosTransparent2 extends AppCompatActivity {

    private ImageView arrow3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.converted_photos_layout_transparent2);

        arrow3 = (ImageView) findViewById(R.id.arrow3);
        arrow3.setImageResource(R.drawable.arrow_img);
        arrow3.bringToFront();

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.converted_photos_transparent2);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intentTransparent3 = new Intent(getApplicationContext(), ConvertedPhotosTransparent3.class);
                startActivity(intentTransparent3);
            }
        });

    }

}
