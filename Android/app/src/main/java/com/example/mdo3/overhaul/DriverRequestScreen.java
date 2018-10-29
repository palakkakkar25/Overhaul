package com.example.mdo3.overhaul;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DriverRequestScreen extends AppCompatActivity {

    private ServiceRequest sr;
    private Driver driver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_request_screen);
        sr = (ServiceRequest) getIntent().getSerializableExtra("serviceRequest");
        driver = (Driver) getIntent().getSerializableExtra("Driver");


        //Decline Button
        View.OnClickListener declineListen = new View.OnClickListener() {
            @Override
            // We reject the job and the job goes to the next person in the queue.
            public void onClick(View v) {
                Intent myIntent = new Intent(DriverRequestScreen.this, DriverMainScreen.class);
                DriverRequestScreen.this.startActivity(myIntent);
                finish();
            }
        };
        Button declineBtn = (Button) findViewById(R.id.button_decline);
        declineBtn.setOnClickListener(declineListen);

        //Accept Button
        View.OnClickListener acceptListen = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(DriverRequestScreen.this, DriverMainScreen.class);
                //TODO:Need to set sr.driverWhoCompleted = driverWhoCompleted
                //TODO:Need to change driverWhoCompleted on sr in database
                if(sr != null)
                {
                    DataAccess da = new DataAccess();
                    da.insertEventLogDriverArrived(sr.getIdCustomer(), sr.getId(), sr.getIdDriverWhoCompleted());
                }
                else {
                    System.out.println("Could not accept job. Service request is NULL!");
                }
                myIntent.putExtra("serviceRequest", sr);
                myIntent.putExtra("Driver", driver);
                myIntent.putExtra("isActive", true);
                DriverRequestScreen.this.startActivity(myIntent);
                finish();
            }
        };
        Button acceptBtn = (Button) findViewById(R.id.button_accept);
        acceptBtn.setOnClickListener(acceptListen);
        TextView jobDescriptionText = (TextView) this.findViewById(R.id.text_jobDescription);
        String jobDescription = createJobDescriptionText();
        jobDescriptionText.setText(jobDescription);

    }

    private String createJobDescriptionText() {
        float weight;
        String pickupLocation;
        String dropoffLocation;
        String description;
        if(sr != null) {
            weight = sr.getWeight();
            pickupLocation = sr.getStartLocation();
            dropoffLocation = sr.getEndLocation();
            description = sr.getDescription();
        }
        else {
            weight = 0;
            pickupLocation = " ";
            dropoffLocation = " ";
            description = "ERROR: No job request.";
        }



        String jobDescriptionText = "Weight: " + weight + " lbs\nPickup Location: " + pickupLocation
                + "\nDropoffLocation: " + dropoffLocation + "\n\nDescription: " + description + "\n";
        return jobDescriptionText;
    }
}
