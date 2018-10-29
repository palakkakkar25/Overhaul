package com.example.mdo3.overhaul;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Timestamp;
import java.util.Date;


public class credit_card_info extends AppCompatActivity
{

    private String[] items;
    private EditText view;
    private String ccNumber;
    private String ccDate;
    private String ccSCode;
    private String ccType;

    private EditText mccNumber;
    private EditText mccDate;
    private EditText mCCSCode;

    private Spinner dropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card_info);

        items = createSpinner();
        mccNumber  = (EditText) findViewById(R.id.cc_number);
        mccDate = (EditText) findViewById(R.id.cc_date);
        mCCSCode = (EditText) findViewById(R.id.cc_SCode);

        //Load credit card types into the spinner on activity_credit_card_info
        //get strings from strings.xml
        dropdown = (Spinner) findViewById(R.id.cc_types);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.cc_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private String[] createSpinner()
    {
        String[] types = getResources().getString(R.string.cc_types).split(",");
        return types;
    }

    public void submitBtn(View view)
    {
        ccNumber =  mccNumber.getText().toString();
        ccSCode =   mCCSCode.getText().toString();
        ccDate =    mccDate.getText().toString();
        ccType =    dropdown.getSelectedItem().toString();

        // String UserName, String PassWord, String Name, String PhoneNumber, Timestamp DateRegistered,
        // String CardNumber, String BillingAddress,
        // String ExpMonth, String ExpYear, String CVV, String BillingName)
        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        Intent pastIntent = getIntent();
        DataAccess da = new DataAccess();
        // Register the customer in the database
        Boolean result = da.insertCustomer(pastIntent.getStringExtra("email"),
                pastIntent.getStringExtra("password"),
                pastIntent.getStringExtra("name"),
                pastIntent.getStringExtra("phone"),
                ts,
                ccNumber,
                pastIntent.getStringExtra("address"),
                ccDate.substring(0,2),
                ccDate.substring(3,5),
                ccSCode,
                pastIntent.getStringExtra("name"));

        // Immediately log them ins
        Customer customer = da.checkCustomerLogin(pastIntent.getStringExtra("email"), pastIntent.getStringExtra("password"));
        if(customer == null)
            System.out.println("*************** Error Getting Customer");

        System.out.println("DEBUG: " + ccNumber);
        System.out.println("DEBUG: " + ccDate);
        System.out.println("DEBUG: " + ccSCode);
        System.out.println("DEBUG: " + ccType);

        if(result) {
            Toast.makeText(this, "Signup successful !", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ClientMainScreen.class);
            intent.putExtra("Customer", customer);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Signup failed!", Toast.LENGTH_SHORT).show();
        }
    }
    public void cancelBtn(View view)
    {
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
        return true;
    }
}
