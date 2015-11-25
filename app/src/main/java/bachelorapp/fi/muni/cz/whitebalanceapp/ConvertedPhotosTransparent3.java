package bachelorapp.fi.muni.cz.whitebalanceapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * Created by Vladimira Hezelova on 25. 11. 2015.
 */
public class ConvertedPhotosTransparent3 extends AppCompatActivity {
    private ImageView arrow5;
    private ImageView arrow6;
    private ImageButton nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.converted_photos_layout_transparent3);

        arrow5 = (ImageView) findViewById(R.id.arrow5);
        arrow5.setImageResource(R.drawable.arrow_image);
        arrow5.bringToFront();
        arrow6 = (ImageView) findViewById(R.id.arrow6);
        arrow6.setImageResource(R.drawable.arrow_image);
        arrow6.bringToFront();

        nextButton = (ImageButton) findViewById(R.id.next6);
        nextButton.setImageResource(R.drawable.ic_navigate_next_black_24dp);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intentTransparent4 = new Intent(getApplicationContext(), ConvertedPhotosTransparent4.class);
                startActivity(intentTransparent4);

            }
        });

    }
}
