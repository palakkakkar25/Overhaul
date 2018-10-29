package com.example.mdo3.overhaul;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Rohini on 4/2/2018.
 */

public class DriverRegistration extends Activity {
    private EditText et_name, et_email, et_password, et_cpassword, et_phone, et_address;
    private EditText et_vehicleCompany, et_vehicleModel, et_vehicleYear, et_licenseNumber, et_loadCapacity;
    Button rgbtn;
    Button cancelbtn;
    String name,email,password,cpassword,address,phone, vehicleCompany,vehicleModel, vehicleYear, licenseNumber, loadCapacity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_driver);
        et_name = (EditText) findViewById(R.id.name);
        et_email = (EditText) findViewById(R.id.email);
        et_password = (EditText) findViewById(R.id.password);
        et_cpassword = (EditText) findViewById(R.id.confirm_password);
        et_phone = (EditText) findViewById(R.id.phone);
        et_address = (EditText) findViewById(R.id.address);
        et_vehicleCompany = (EditText) findViewById(R.id.vehicle_company);
        et_vehicleModel = (EditText) findViewById(R.id.vehicle_model);
        et_vehicleYear = (EditText) findViewById(R.id.vehicle_year);
        et_licenseNumber = (EditText) findViewById(R.id.license_plate_number);
        et_loadCapacity = (EditText) findViewById(R.id.load_capacity);

        rgbtn = (Button)findViewById(R.id.register_user);
        rgbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                register();
            }

        });
        cancelbtn = (Button)findViewById(R.id.cancel_registration);
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                Intent intent = new Intent(DriverRegistration.this, HomeScreen.class);
                DriverRegistration.this.startActivity(intent);
            }
        });
    }

    public void register()
    {
        initialize();
        //If the data is invalid throw error

        if(!validate())
        {
            Toast.makeText(this, "Signup Failed !", Toast.LENGTH_SHORT).show();
        }else {
            //create a new record in DB
            Intent intent = new Intent(this, driver_acc_info.class);
            intent.putExtra("email",email);
            intent.putExtra("password",password);
            intent.putExtra("name",name);
            intent.putExtra("address", address);
            intent.putExtra("phone", phone);
            intent.putExtra("VehicleCompany", vehicleCompany);
            intent.putExtra("vehicleModel", vehicleModel);
            intent.putExtra("vehicleYear", vehicleYear);
            intent.putExtra("licenseNumber", licenseNumber);
            intent.putExtra("loadCapacity", loadCapacity);
            startActivity(intent);
        }
    }

    public void initialize(){
        name = et_name.getText().toString().trim();
        email = et_email.getText().toString().trim();
        password = et_password.getText().toString().trim();
        cpassword = et_cpassword.getText().toString().trim();
        address = et_address.getText().toString().trim();
        vehicleCompany = et_vehicleCompany.getText().toString().trim();
        vehicleModel = et_vehicleModel.getText().toString().trim();
        vehicleYear = et_vehicleYear.getText().toString().trim();
        licenseNumber = et_licenseNumber.getText().toString().trim();
        loadCapacity = et_loadCapacity.getText().toString().trim();
        phone = et_phone.getText().toString().trim();

    }

    //validation check
    public boolean validate(){
        boolean valid = true;

        if(name.isEmpty() || password.isEmpty() || cpassword.isEmpty()){
            valid = false;
        }
        //TODO: something is wrong with this if statement.
        /*
        if(email.isEmpty() || Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            valid = false;
        }

        if(vehicle_name.isEmpty()||vehicle_number.isEmpty()){
        if(email.isEmpty()){
            valid = false;
        }
        if(vehicleCompany.isEmpty()||vehicleModel.isEmpty() || vehicleYear.isEmpty() || licenseNumber.isEmpty() || loadCapacity.isEmpty()){
            valid = false;
        }
        */
        if(password.equals(cpassword))
        {
            valid = false;
        }
        return valid;
    }
}
