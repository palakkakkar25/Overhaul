package com.example.mdo3.overhaul;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DriverVehicleUpdateActivity extends AppCompatActivity {


    private TextView username;
    private TextView name;
    private TextView phoneNumber;
    private TextView dateReg;

    private EditText vMake;
    private EditText vModel;
    private EditText vYear;
    private EditText vTag;
    private EditText vLoad;

    private Driver driver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_vehicle_update);

        Intent intent = getIntent();
        driver = (Driver) intent.getSerializableExtra("Driver");

        OnClickListener sbmt_button = new OnClickListener(){
            public void onClick(View v)
            {
                submitBtn(v);
            }
        };
        Button updateacct = (Button) findViewById(R.id.updateacct);
        updateacct.setOnClickListener(sbmt_button);
    }
    public void submitBtn (View view)
    {
        //Peel all the info from the xml and send it to the database
        String make;
        String model;
        String year;
        String tag;
        String load;
        byte[] picture = new byte[0];

        System.out.println("DEBUG: Submit Button Pressed");

        make = vMake.getText().toString();
        model = vModel.getText().toString();
        year = vYear.getText().toString();
        tag = vTag.getText().toString();
        load = vLoad.getText().toString();

        // Error check the inputs, if they are wrong, don't update
        String x = (make != null && !make.isEmpty()) ? make : driver.getVehicle().getMake();
        String y = (model != null && !model.isEmpty()) ? model : driver.getVehicle().getModel();
        int z = (year != null && !year.isEmpty()) ? Integer.parseInt(year) : driver.getVehicle().getYear();
        String xx = (tag != null && !tag.isEmpty()) ? tag : driver.getVehicle().getLicensePlate();
        float yy = (load != null && !load.isEmpty()) ? Float.parseFloat(load) : driver.getVehicle().getLoadCapacity();

        DataAccess da = new DataAccess();

        Vehicle myVehicle = new Vehicle(driver.getId(), x, y, z, xx, yy, picture);
        boolean res = da.updateDriverVehicleInfo(myVehicle);
        System.out.println(res);
        Driver result = da.getDriverById(driver.getId());


        System.out.println("DEBUG: Done");
        System.out.println("DEBUG: Starting new activity");
        Intent intent = new Intent(DriverVehicleUpdateActivity.this, DriverMainScreen.class);
        if(result != null)
            intent.putExtra("Driver", result);
        else
            intent.putExtra("Driver", driver);
        startActivity(intent);
    }

    // Go back to the driver main screen
    public void cancelBtn (View view)
    {
        Intent intent = new Intent (this, DriverMainScreen.class);
        intent.putExtra("Driver",driver);
        startActivity(intent);
    }
}
