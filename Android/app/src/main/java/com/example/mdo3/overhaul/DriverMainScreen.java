package com.example.mdo3.overhaul;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.Switch;
import android.net.Uri;

/**
 * Created by Wendelyn on 3/25/2018.
 */

public class DriverMainScreen extends Activity{
    // Set this variable based on whether or not a request is active, and thus whether or not to show the driver map.
    // Ideally this should be set whenever the user moves to this screen, thus the setting for this variable
    // should be within OnClickListeners that send the user here.

    private ServiceRequest sr;
    private Driver myDriver;
    public boolean requestActive;
    // Note that primitive booleans (lowercase B) will be false if left uninitialized.

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main_screen);
        Intent myIntent = getIntent();
        myDriver = (Driver)myIntent.getSerializableExtra("Driver");
        sr = (ServiceRequest) myIntent.getSerializableExtra("serviceRequest");

        requestActive = myIntent.getBooleanExtra("isActive", false);

        //Logout Button
        View.OnClickListener logoutListen = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataAccess da = new DataAccess();
                boolean updateStatus = da.updateDriverMainInfo(myDriver.getId(), myDriver.getName(), myDriver.getPhoneNumber(),myDriver.getDriverLicenseNumber(), myDriver.getDateRegistered(), false);
                if(!updateStatus)
                {
                    System.out.println( "isActive not updated to false" );
                }
                finish();
                Intent myIntent = new Intent(DriverMainScreen.this, HomeScreen.class);
                DriverMainScreen.this.startActivity(myIntent);
            }
        };
        Button logoutBtn = (Button) findViewById(R.id.button_logout);
        logoutBtn.setOnClickListener(logoutListen);

        Switch activitySwh = (Switch) findViewById(R.id.switch_active);
        Button editVehicleBtn = (Button) findViewById(R.id.button_edit_vehicle);
        Button pickUpBtn = (Button) findViewById(R.id.button_pickup);
        Button dropOffBtn = (Button) findViewById(R.id.button_dropoff);
        Button queryBtn = (Button) findViewById(R.id.button_queryDB);

        if (requestActive){
            // Hide regular driver settings
            activitySwh.setVisibility(View.INVISIBLE);
            editVehicleBtn.setVisibility(View.INVISIBLE);

            // Make sure the buttons are visible, and that they send the user to Google Maps
            // for the pick-up or drop-off.
            pickUpBtn.setVisibility(View.VISIBLE);
            View.OnClickListener pickUpListen = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Go to Google Maps with directions to pick-up address.
                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + Uri.encode(sr.getStartLocation()));
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
            };
            pickUpBtn.setOnClickListener(pickUpListen);

            dropOffBtn.setVisibility(View.VISIBLE);
            View.OnClickListener dropOffListen = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Go to Google Maps with drop-off address.
                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + Uri.encode(sr.getEndLocation()));
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
            };
            dropOffBtn.setOnClickListener(dropOffListen);


        } else {
            // Hide the buttons and don't give them an OnClick action.
            pickUpBtn.setVisibility(View.INVISIBLE);
            dropOffBtn.setVisibility(View.INVISIBLE);

            // Make sure the buttons are visible
            activitySwh.setVisibility(View.VISIBLE);
            activitySwh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    DataAccess da = new DataAccess();
                    boolean updateStatus;
                    if(isChecked)
                    {
                        activitySwh.setText("Active");
                        updateStatus = da.updateDriverMainInfo(myDriver.getId(), myDriver.getName(), myDriver.getPhoneNumber(),myDriver.getDriverLicenseNumber(), myDriver.getDateRegistered(), true);
                        if(!updateStatus)
                        {
                            System.out.println("isActive not updated to tr");
                        }
                    }
                    else
                    {
                        activitySwh.setText("Inactive");
                        updateStatus = da.updateDriverMainInfo(myDriver.getId(), myDriver.getName(), myDriver.getPhoneNumber(),myDriver.getDriverLicenseNumber(), myDriver.getDateRegistered(), false);
                        if(!updateStatus)
                        {
                            System.out.println( "isActive not updated to false" );
                        }
                    }

                }
            });

            editVehicleBtn.setVisibility(View.VISIBLE);
            View.OnClickListener vehicleListen = new View.OnClickListener() {
                @Override
                public void onClick(View v)             {
                    //Edit Driver Vehicle Information
                    //passing driver previous information
                    Intent intent = new Intent(DriverMainScreen.this, DriverVehicleUpdateActivity.class);
                    intent.putExtra("Driver", myDriver);
                    startActivity(intent);
                }
            };
            editVehicleBtn.setOnClickListener(vehicleListen);
        }

        View.OnClickListener queryListen = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataAccess da = new DataAccess();
                ServiceRequest srTemp = da.waitForRequest(myDriver.getId());

                if(srTemp != null) {
                    Intent myIntent = new Intent(DriverMainScreen.this, DriverRequestScreen.class);
                    myIntent.putExtra("serviceRequest", srTemp);
                    myIntent.putExtra("Driver", myDriver);
                    DriverMainScreen.this.startActivity(myIntent);
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(),"No requests found.",Toast.LENGTH_SHORT).show();
                }
            }
        };
        queryBtn.setOnClickListener(queryListen);
    }
}
