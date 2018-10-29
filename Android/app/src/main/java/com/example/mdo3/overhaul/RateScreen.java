package com.example.mdo3.overhaul;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

public class RateScreen extends AppCompatActivity {
    private Driver assignedDriver;
    private ServiceRequest sr;
    private Customer myCustomer;
    private RatingBar ratingObject;
    private Button cancelBtn;
    private Button submitBtn;
    private float mRating;
    private EditText commentsObject;
    private String mComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sr = (ServiceRequest) getIntent().getSerializableExtra("serviceRequest");
        myCustomer = (Customer) getIntent().getSerializableExtra("myCustomer");
        assignedDriver = (Driver) getIntent().getSerializableExtra("driver");
        setContentView(R.layout.content_rate_screen);

        ratingObject = (RatingBar) findViewById(R.id.ratingBar);
        commentsObject = (EditText) findViewById(R.id.comments);
        cancelBtn = (Button) findViewById(R.id.cancelButton);
        submitBtn = (Button) findViewById(R.id.submitButton);

        View.OnClickListener cancelListen = new View.OnClickListener() {
            @Override
            // Need to eventually have it actually save data to the database
            public void onClick(View v) {
                // Links back to client main screen
                Intent intent = new Intent(RateScreen.this, ClientMainScreen.class);
                intent.putExtra("Customer", myCustomer);
                startActivity(intent);
                finish();

            }
        };
        cancelBtn.setOnClickListener(cancelListen);

        View.OnClickListener submitListen = new View.OnClickListener() {
            @Override
            // Need to eventually have it actually save data to the database
            public void onClick(View v) {
                // Links back to client main screen
                mRating = ratingObject.getRating();
                mComments = commentsObject.getText().toString();
                int rating = Math.round(mRating);
                float avgRating = assignedDriver.getAvgRating();
                int numRaters = assignedDriver.getNumRating();
                float newAvg = ((avgRating * numRaters) + mRating) / (++numRaters);

                DataAccess da = new DataAccess();
                da.insertDriverRating(assignedDriver.getId(), myCustomer.getId(), sr.getId(), rating, mComments, newAvg, numRaters);

                // Links back to client main screen
                Intent intent = new Intent(RateScreen.this, ClientMainScreen.class);
                intent.putExtra("Customer", myCustomer);
                startActivity(intent);
                finish();

            }
        };
        submitBtn.setOnClickListener(submitListen);
    }
}
