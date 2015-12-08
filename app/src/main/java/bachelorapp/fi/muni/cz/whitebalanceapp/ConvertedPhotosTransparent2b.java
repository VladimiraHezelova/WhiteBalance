package bachelorapp.fi.muni.cz.whitebalanceapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by Vladimira Hezelova on 8. 12. 2015.
 */
public class ConvertedPhotosTransparent2b extends AppCompatActivity {
    private ImageView arrow2b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.converted_photos_layout_transparent2b);

        arrow2b = (ImageView) findViewById(R.id.arrow2b);
        arrow2b.setImageResource(R.drawable.arrow_img);
        arrow2b.bringToFront();

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.converted_photos_transparent2b);

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
