package com.example.mdo3.overhaul;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Arrays;


public class DriverDetails extends AppCompatActivity {

    private ServiceRequest sr;
    private Driver driver;

    private TextView tv_name;
    private TextView tv_rating;
    private ImageView iv_picture = null;
    private byte[] picture = new byte[0];
    private Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_driver_details);
        Intent myIntent = getIntent();
        sr = (ServiceRequest) myIntent.getSerializableExtra("serviceRequest");
        driver = (Driver) myIntent.getSerializableExtra("Driver");

        tv_name = (TextView) findViewById(R.id.viewDriverName);
        tv_rating = (TextView) findViewById(R.id.viewDriverRating);
        iv_picture = (ImageView) findViewById(R.id.viewDriverPicture);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initDetails();

        // Go back to the wait screen
        backBtn = (Button) findViewById(R.id.backButton);
        OnClickListener backButtonListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(DriverDetails.this, WaitScreen.class);
                intent.putExtra("serviceRequest", sr);
                startActivity(intent);
            }
        };
        backBtn.setOnClickListener(backButtonListener);

    }

    // Load the driver's details
    public void initDetails(){
        DataAccess da = new DataAccess();
        if (driver == null)
            driver = da.getDriverById(sr.getIdDriverWhoCompleted());
        picture = driver.getPicture();
        tv_name.setText(driver.getName());
        tv_rating.setText(Double.toString(driver.getAvgRating()));
        Bitmap bmp;
        try {
            bmp = BitmapFactory.decodeByteArray(picture,0,picture.length);
            iv_picture.setImageBitmap(bmp);
            // Create a 100x100 blank picture
            byte[] blankPicture = new byte[10000];
            for (int i=0; i<blankPicture.length; i++){
                blankPicture[i] = 0;
            }
            bmp = BitmapFactory.decodeByteArray(blankPicture,0,blankPicture.length);
            iv_picture.setImageBitmap(bmp);
        }
        catch (Exception e) {
            System.out.println("No picture was found: " + e.getMessage());
        }
    }
}
