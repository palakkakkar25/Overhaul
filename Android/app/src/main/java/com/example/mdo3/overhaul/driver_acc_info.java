package com.example.mdo3.overhaul;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

public class driver_acc_info extends AppCompatActivity
{

    private EditText mAccNum;
    private EditText mAccNum2;
    private EditText mAccRout;
    private EditText mAccBank;

    private String AccNum;
    private String AccNum2;
    private String AccRout;
    private String AccBank;

    private String mUser;
    private String mLicense;
    private String mMake;
    private String mModel;
    private int mYear;
    private String mLicensePlate;
    private float mLoad;
    private byte[] mPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_acc_info);

        mAccBank = (EditText) findViewById(R.id.acc_bank);
        mAccRout = (EditText) findViewById(R.id.acc_routing);
        mAccNum2 = (EditText) findViewById(R.id.acc_number2);
        mAccNum = (EditText) findViewById(R.id.acc_number);

        /*mUser = (EditText) findViewById(R.id.acc_user);
        mLicense = (EditText) findViewById(R.id.acc_license);
        mMake = (EditText) findViewById(R.id.acc_make);
        mModel = (EditText) findViewById(R.id.acc_model);
        mYear = (EditText) findViewById(R.id.acc_year);
        mLicensePlate = (EditText) findViewById(R.id.acc_license_plate);
        mLoad = (EditText) findViewById(R.id.acc_load);*/


        //Create back arrow on the tool bar to send back to homescreen
        Toolbar toolbar = (Toolbar) findViewById(R.id.acc_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void submitBtn(View view)
    {
        // The vehicles are not ready to be added but when they are uncomment this code and it will pull the information

        /*String user = mUser.getText().toString();
        String license = mLicense.getText().toString();
        String make = mMake.getText().toString();
        String model = mModel.getText().toString();
        String sYear = mYear.getText().toString();
        String licensePlate = mLicensePlate.getText().toString();
        String sLoad = mLoad.getText().toString();

        int year = Integer.valueOf(sYear);
        float load = Float.parseFloat(sLoad);*/

        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());

        AccNum = mAccNum.getText().toString();
        AccNum2 = mAccNum2.getText().toString();
        AccRout = mAccRout.getText().toString();
        AccBank = mAccBank.getText().toString();

        if(!AccNum.equalsIgnoreCase(AccNum2))
        {
            mAccNum2.setError("Account number doesn't match");
        }

        //(String UserName, String PassWord, String Name, String PhoneNumber, String DriverLicenseNumber, Timestamp DateRegistered,
        //String CarMake, String CarModel, int CarYear, String LicensePlateNumber, float LoadCapacity,
        //String BankAccountNumber, String RoutingNumber, String BillingName)
        DataAccess da = new DataAccess();
        Intent pastIntent = getIntent();
        // Insert the Driver into the database
        Boolean result = da.insertDriver(pastIntent.getStringExtra("email"),
                pastIntent.getStringExtra("password"),
                pastIntent.getStringExtra("name"),
                pastIntent.getStringExtra("phone"),
                pastIntent.getStringExtra("licenseNumber"),
                ts,
                pastIntent.getStringExtra("VehicleCompany"),
                pastIntent.getStringExtra("vehicleModel"),
                Integer.parseInt(pastIntent.getStringExtra("vehicleYear")),
                pastIntent.getStringExtra("licenseNumber"),
                Integer.parseInt(pastIntent.getStringExtra("loadCapacity")),
                AccNum,
                AccRout,
                pastIntent.getStringExtra("name"));

        Driver driver = da.checkDriverLogin(pastIntent.getStringExtra("email"), pastIntent.getStringExtra("password"));


        // Insert the vehicle into the database

        /*Vehicle myVehicle = new Vehicle(driver.getId(), make, model, year, licensePlate, load, mPicture);
        boolean success = da.insertDriverVehicle(myVehicle);

        if (success)
            Toast.makeText(this, "Vehicle insert successful !", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Vehicle insert failed !", Toast.LENGTH_SHORT).show();*/


        System.out.println("DEBUG : "  + AccNum);
        System.out.println("DEBUG : "  + AccNum2);
        System.out.println("DEBUG : "  + AccRout);
        System.out.println("DEBUG : "  + AccBank);


        if(result) {
            Toast.makeText(this, "Signup successful !", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, DriverMainScreen.class);
            intent.putExtra("Driver", driver);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Signup failed !", Toast.LENGTH_SHORT).show();

        }
    }

    // Cancel registration
    public void cancelBtn(View view)
    {
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
    }

    // Go back to the first screen of registration
    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
        return true;
    }
}
