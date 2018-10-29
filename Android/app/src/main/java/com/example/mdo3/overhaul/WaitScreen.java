package com.example.mdo3.overhaul;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Queue;
import android.view.View.OnClickListener;


// Still need to set driver searching functionality (tentative)
public class WaitScreen extends AppCompatActivity {
    private Driver assignedDriver = null;
    private ServiceRequest sr;
    private Customer myCustomer;
    private int serviceRequestId;
    private Button cancelSearchBtn;
    private Button viewDriverBtn;
    private ImageButton queryBtn;
    private Button completeBtn;
    private TextView waitText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sr = (ServiceRequest) getIntent().getSerializableExtra("serviceRequest");
        myCustomer = (Customer) getIntent().getSerializableExtra("myCustomer");
        serviceRequestId = (int) getIntent().getIntExtra("serviceRequestId", -1);
        setContentView(R.layout.content_wait_screen);
        cancelSearchBtn = (Button) findViewById(R.id.cancelButton);
        viewDriverBtn = (Button) findViewById(R.id.viewDriverButton);
        queryBtn = (ImageButton) findViewById(R.id.button_queryDB);
        completeBtn = (Button) findViewById(R.id.completeButton);
        waitText = (TextView) findViewById(R.id.waitText);

        //Create a queue of available drivers, take driver at the head of the queue
        Queue<Driver> driverQueue = findDrivers();
        assignedDriver = driverQueue.poll();
        DataAccess da = new DataAccess();
        if (assignedDriver != null)
            da.insertEventLogServiceRequest(myCustomer.getId(), serviceRequestId, assignedDriver.getId());

        //If there are no available drivers, cancel the request and return
        if(assignedDriver == null)
        {
            Toast.makeText(WaitScreen.this, "No available drivers!", Toast.LENGTH_SHORT).show();
            CancelSearch();
        }

        //If the driver cannot be assigned, cancel the request and return
        if(!assignDriver()) {
            System.out.println("FAILED TO ASSIGN DRIVER!");
            CancelSearch();
        }


        queryBtn.setVisibility(View.VISIBLE);
        cancelSearchBtn.setVisibility(View.VISIBLE);
        viewDriverBtn.setVisibility(View.INVISIBLE);
        completeBtn.setVisibility(View.INVISIBLE);
        waitText.setText("Please Wait While We Find You A Driver!");


        OnClickListener cancelListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Links back to client main screen
                Intent intent = new Intent(WaitScreen.this, ClientMainScreen.class);
                startActivity(intent);
                finish();

            }
        };
        cancelSearchBtn.setOnClickListener(cancelListener);

        OnClickListener viewDriverListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Links to Driver Details screen
                Intent intent = new Intent(WaitScreen.this, DriverDetails.class);
                intent.putExtra("serviceRequest", sr);
                intent.putExtra("Driver", assignedDriver);
                startActivity(intent);
                finish();
            }
        };
        viewDriverBtn.setOnClickListener(viewDriverListener);
        //Query Button
        View.OnClickListener queryListen = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataAccess da = new DataAccess();
                assignedDriver = da.checkIfDriverAccepted(sr.getId(), sr.getIdCustomer());
                if (assignedDriver == null)
                {
                    queryBtn.setVisibility(View.VISIBLE);
                    cancelSearchBtn.setVisibility(View.VISIBLE);
                    viewDriverBtn.setVisibility(View.INVISIBLE);
                    completeBtn.setVisibility(View.INVISIBLE);
                    waitText.setText("Please Wait While We Find You A Driver!");
                }
                else
                {
                    queryBtn.setVisibility(View.INVISIBLE);
                    cancelSearchBtn.setVisibility(View.INVISIBLE);
                    viewDriverBtn.setVisibility(View.VISIBLE);
                    completeBtn.setVisibility(View.VISIBLE);
                    waitText.setText("Driver Found!");
                }
            }
        };
        queryBtn.setOnClickListener(queryListen);

        //Complete Button
        View.OnClickListener completeListen = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataAccess da = new DataAccess();
                da.updateServiceRequestCompleted(sr.getId(), true);
                Intent myIntent = new Intent(WaitScreen.this, RateScreen.class);
                myIntent.putExtra("serviceRequest", sr);
                myIntent.putExtra("myCustomer", myCustomer);
                myIntent.putExtra("driver", assignedDriver);
                WaitScreen.this.startActivity(myIntent);
                finish();

            }
        };
        completeBtn.setOnClickListener(completeListen);
    }

    // Find an available driver, assign to 'assignedDriver'
    private Queue<Driver> findDrivers() {
        DataAccess da = new DataAccess();
        Queue<Driver> driverQueue  = da.getPossibleDrivers();

        return driverQueue;
    }

    //Add 'assignedDriver' to the service request
    private boolean assignDriver() {
        return true;
    }
    // Cancels Driver Search
    public void CancelSearch()
    {
        Intent intent = new Intent(WaitScreen.this, ClientMainScreen.class);
        startActivity(intent);
        finish();

        //TODO: indicate in database that the service request was not completed
    }
}
