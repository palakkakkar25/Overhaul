package com.example.mdo3.overhaul;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class UpdateCustomerInformation extends AppCompatActivity
{

    private TextView username;
    private TextView name;
    private TextView phoneNumber;
    private TextView dateReg;

    private EditText ccNum;
    private EditText ccMonth;
    private EditText ccYear;
    private EditText ccSCode;
    private EditText ccBilling;

    private Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_customer_information);

        Intent intent = getIntent();
        customer = (Customer) intent.getSerializableExtra("Customer");

        username = (TextView) findViewById(R.id.uUserName);
        name = (TextView) findViewById(R.id.uName);
        phoneNumber = (TextView) findViewById(R.id.uPhone);
        dateReg = (TextView) findViewById(R.id.uDateReg);

        username.setText(customer.getUserName());
        name.setText(customer.getName());
        phoneNumber.setText(customer.getPhoneNumber());
        dateReg.setText(customer.getDateRegistered().toString());

        ccNum = (EditText) findViewById(R.id.u_enum);
        ccMonth = (EditText) findViewById(R.id.u_emonth);
        ccYear = (EditText) findViewById(R.id.u_eyear);
        ccSCode = (EditText) findViewById(R.id.u_eccv);
        ccBilling = (EditText) findViewById(R.id.u_ebilling);

        ccNum.setHint("CC Number: " + customer.getccNumber());
        ccMonth.setHint("Expiration Month: " + customer.getccMonth());
        ccYear.setHint("Expiration Year: " + customer.getCcYear());
        ccSCode.setHint("CVV Code: " + customer.getCcSCode());
        ccBilling.setHint(customer.getCcBilling());
    }

    public void submitBtn (View view)
    {
        // Pull the data from the xml and submit it to the database
        String num;
        String month;
        String year;
        String ccscode;

        System.out.println("DEBUG: Submit Button Pressed");

        num = ccNum.getText().toString();
        month = ccMonth.getText().toString();
        year = ccYear.getText().toString();
        ccscode = ccSCode.getText().toString();

        String x = (num != null && !num.isEmpty()) ? num : customer.getccNumber();
        String y = (month != null && !month.isEmpty()) ? month : customer.getccMonth();
        String z = (year != null && !year.isEmpty()) ? year : customer.getCcYear();
        String xx = (ccscode != null && !ccscode.isEmpty()) ? ccscode : customer.getCcSCode();

        DataAccess da = new DataAccess();

        // Update our payment information
        Boolean res = da.updateCustomerPaymentInfo(customer.getId(),
                x,
                y,
                z,
                xx);
        System.out.println(res);
        Customer result = da.getCustomerById(customer.getId());


        System.out.println("DEBUG: Done");
        System.out.println("DEBUG: Starting new activity");
        Intent intent = new Intent(this, ClientMainScreen.class);
        if(result != null)
            intent.putExtra("Customer", result);
        else
            intent.putExtra("Customer", customer);
        startActivity(intent);
    }

    // Cancel the update
    public void cancelBtn (View view)
    {
        Intent intent = new Intent (this, ClientMainScreen.class);
        intent.putExtra("Customer",customer);
        startActivity(intent);
    }
}
