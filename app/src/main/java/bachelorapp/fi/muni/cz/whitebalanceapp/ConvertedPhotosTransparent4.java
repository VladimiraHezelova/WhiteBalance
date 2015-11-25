package bachelorapp.fi.muni.cz.whitebalanceapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by Vladimira Hezelova on 25. 11. 2015.
 */
public class ConvertedPhotosTransparent4 extends AppCompatActivity {

    private ImageButton nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.converted_photos_layout_transparent4);


        nextButton = (ImageButton) findViewById(R.id.next7);
        nextButton.setImageResource(R.drawable.ic_navigate_next_black_24dp);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

}
