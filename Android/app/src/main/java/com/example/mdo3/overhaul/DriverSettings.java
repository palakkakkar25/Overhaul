package com.example.mdo3.overhaul;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DriverSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_settings);

        //Save Button
        View.OnClickListener saveListen = new View.OnClickListener() {
            @Override
            // TODO: Peel the driver's information from the xml and send it to the database
            public void onClick(View v) {
                DataAccess da = new DataAccess();
                /*da.updateDriverMainInfo();
                da.updateDriverPaymentInfo();*/
                Intent myIntent = new Intent(DriverSettings.this, DriverMainScreen.class);
                DriverSettings.this.startActivity(myIntent);
                finish();
            }
        };
        Button saveBtn = (Button) findViewById(R.id.button_save);
        saveBtn.setOnClickListener(saveListen);
    }

}
