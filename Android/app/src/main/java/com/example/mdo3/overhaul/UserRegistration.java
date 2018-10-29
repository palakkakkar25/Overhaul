package com.example.mdo3.overhaul;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Rohini on 4/2/2018.
 */

public class UserRegistration extends Activity {
    private EditText et_name, et_email, et_password, et_cpassword, et_phone, et_address;
    Button rgbtn;
    Button cancelbtn;

    String name,email,password,cpassword, phone, address;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        et_name = (EditText) findViewById(R.id.name);
        et_email = (EditText) findViewById(R.id.email);
        et_password = (EditText) findViewById(R.id.password);
        et_cpassword = (EditText) findViewById(R.id.confirm_password);
        et_phone = (EditText) findViewById(R.id.phone);
        et_address = (EditText) findViewById(R.id.address);
        rgbtn = (Button)findViewById(R.id.register_user);

        rgbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                register();
            }

        });
        cancelbtn = (Button)findViewById(R.id.cancel_registration);
        // Cancel the registration process and return to the home screen
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserRegistration.this,HomeScreen.class);
                UserRegistration.this.startActivity(intent);
            }
        });

    }

    // Save our information and continue to add a credit card
    public void register() {
        initialize();
        if(!validate()){
            Toast.makeText(this, "Signup Failed !", Toast.LENGTH_SHORT).show();
        }else {
            Intent intent = new Intent(this, credit_card_info.class);
            intent.putExtra("email",email);
            intent.putExtra("password",password);
            intent.putExtra("name",name);
            intent.putExtra("address", address);
            intent.putExtra("phone", phone);
            startActivity(intent);

        }
    }
    public void initialize(){
        name = et_name.getText().toString().trim();
        email = et_email.getText().toString().trim();
        password = et_password.getText().toString().trim();
        cpassword = et_cpassword.getText().toString().trim();
        phone = et_phone.getText().toString().trim();
        address = et_address.getText().toString().trim();
    }
    //validation check
    public boolean validate(){
        boolean valid = true;

        if(name.isEmpty() || password.isEmpty() || cpassword.isEmpty()||phone.isEmpty()||address.isEmpty()){
            valid = false;
        }
        if(email.isEmpty()){
            valid = false;
        }

        if(password.equals(cpassword)){
            valid = false;
        }

        return valid;
    }
}