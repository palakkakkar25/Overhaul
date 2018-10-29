package com.example.mdo3.overhaul;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

public class JobRequest extends AppCompatActivity {

    private final int PICK_IMAGE_REQUEST = 1;
    private final int RESULT_OK = 1;

    private int userId;
    private EditText title;
    private EditText description;
    private Timestamp datePosted;
    private CheckBox mLoadHelp;
    private CheckBox mUnloadHelp;
    private EditText mPickupLocation;
    private EditText mDestination;
    private EditText mWeight;
    private EditText mPrice;
    private byte[] picture = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_request_screen);
        title = (EditText) findViewById(R.id.editText3);
        description = (EditText) findViewById((R.id.editTextDescription));
        mLoadHelp = (CheckBox) findViewById(R.id.checkBoxLoad);
        mUnloadHelp = (CheckBox) findViewById(R.id.checkBoxUnload);
        mPickupLocation = (EditText) findViewById(R.id.editTextPickup);
        mDestination = (EditText) findViewById(R.id.editTextDestination);
        mWeight = (EditText) findViewById(R.id.editTextWeight);
        mPrice = (EditText) findViewById(R.id.editTextPrice);

        Intent myIntent = getIntent();
        Customer myCustomer = (Customer)myIntent.getSerializableExtra("Customer");

        //Upload Images Button
        View.OnClickListener uploadListen = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                // Show only images, filter out everything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        };


        Button uploadBtn = (Button) findViewById(R.id.button_upload);
        uploadBtn.setOnClickListener(uploadListen);

        //Send Request Button
        View.OnClickListener sendListen = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Just a placeholder so I can test that clicking on this button actually works.
                String sTitle = title.getText().toString();
                String sDescription = description.getText().toString();

                // Get load and unload checkboxes

                boolean loadHelp = mLoadHelp.isChecked();
                boolean unloadHelp = mUnloadHelp.isChecked();
                Date date = new Date();
                datePosted = new Timestamp(date.getTime());

                // Get Locations
                String sPickupLocation = mPickupLocation.getText().toString();
                String sDestination = mDestination.getText().toString();


                // Get price and weight floats
                String sWeight = mWeight.getText().toString();
                float weight = Float.parseFloat(sWeight);
                String sPrice = mPrice.getText().toString();
                float price = Float.parseFloat(sPrice);


                //insert a new service request into the database
                DataAccess da = new DataAccess();
                int requestInserted = -1;
                requestInserted = da.insertServiceRequest(myCustomer.getId(), sTitle, sDescription, weight, datePosted, price, loadHelp, unloadHelp, null, sPickupLocation, sDestination);
                ServiceRequest sr = new ServiceRequest(requestInserted, myCustomer.getId(), 0, sTitle, sDescription, weight, datePosted, null, price, loadHelp, unloadHelp, picture, false, true, sPickupLocation, sDestination);
                if(requestInserted == -1) {
                    System.out.println("INSERT FAILED ******************");
                    Intent intent = new Intent(JobRequest.this, ClientMainScreen.class);
                    intent.putExtra("Customer", myCustomer);
                    intent.putExtra("InsertError", true);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent myIntent = new Intent(JobRequest.this, WaitScreen.class);
                    myIntent.putExtra("serviceRequest", sr);
                    myIntent.putExtra("myCustomer", myCustomer);
                    myIntent.putExtra("serviceRequestId", requestInserted);
                    JobRequest.this.startActivity(myIntent);
                    finish();

                }
            }
        };
        Button sendBtn = (Button) findViewById(R.id.button_send);
        sendBtn.setOnClickListener(sendListen);

        //Cancel Button
        View.OnClickListener cancelListen = new View.OnClickListener() {
            @Override
            // Abandon the request and go back to the main screen
            public void onClick(View v) {
                Intent myIntent = new Intent(JobRequest.this, ClientMainScreen.class);
                myIntent.putExtra("Customer", myCustomer);
                JobRequest.this.startActivity(myIntent);
                finish();

            }
        };
        Button cancelBtn = (Button) findViewById(R.id.button_cancel);
        cancelBtn.setOnClickListener(cancelListen);

        mWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                {
                    String sWeight = mWeight.getText().toString();
                    if (!sWeight.isEmpty()) {
                        double weight = Double.parseDouble(sWeight);
                        double price = weight * 0.10f;
                        String sPrice = mPrice.getText().toString();
                        if (!sPrice.isEmpty()) {
                            double oldPrice = Double.parseDouble(sPrice);
                            price += oldPrice;
                        }
                        price = round(price, 2);
                        sPrice = String.valueOf(price);
                        mPrice.setText(sPrice);
                    }
                }
            }
        });

        mDestination.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                {
                    if (!mPickupLocation.getText().toString().isEmpty() && !mDestination.getText().toString().isEmpty())
                    {
                        LatLng pickupLL = convertAddressToLatLong(mPickupLocation.getText().toString());
                        LatLng destinationLL = convertAddressToLatLong(mDestination.getText().toString());
                        double distance = distance(pickupLL.latitude, pickupLL.longitude, destinationLL.latitude, destinationLL.longitude);
                        double mpg = 20.0f;
                        double gasPrice = 4.0f;
                        double price = (distance / mpg) * gasPrice;
                        String sPrice = mPrice.getText().toString();
                        if (!sPrice.isEmpty()) {
                            double oldPrice = Double.parseDouble(sPrice);
                            price += oldPrice;
                        }
                        price = round(price, 2);
                        sPrice = String.valueOf(price);
                        mPrice.setText(sPrice);
                    }
                }
            }
        });

        mLoadHelp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String sPrice = mPrice.getText().toString();
                double price = Double.parseDouble(sPrice);
                if (isChecked)
                    price += 5.0;
                else
                    price -= 5.0;
                price = round(price, 2);
                sPrice = String.valueOf(price);
                mPrice.setText(sPrice);
            }
        });

        mUnloadHelp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String sPrice = mPrice.getText().toString();
                double price = Double.parseDouble(sPrice);
                if (isChecked)
                    price += 5.0;
                else
                    price -= 5.0;
                price = round(price, 2);
                sPrice = String.valueOf(price);
                mPrice.setText(sPrice);
            }
        });
    }

    public LatLng convertAddressToLatLong(String address)
    {
        try{
            getLatLongAsync gll =  new getLatLongAsync(address);
            return gll.execute().get();
        } catch (Exception e) {System.out.println(e);}
        return null;
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2)
    {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return dist * 1.5;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    // Convert degrees to radians and radians to degrees. Used to convert map degrees to miles.
    public static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    public static double rad2deg(double rad)
    {
        return (rad * 180.0 / Math.PI);
    }

    // Round to two decimal places for displaying price
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    //Fetch the latitude and longitude from the given address from the google maps server
    // This must be done Async otherwise it will crash the app
    private class getLatLongAsync extends AsyncTask<Void, Void, LatLng>
    {
        private String address;

        public getLatLongAsync(String address)
        {
            this.address = address;
        }

        @Override
        protected LatLng doInBackground(Void... params)
        {
            Double lat = 0.0;
            Double lon = 0.0;
            // Go to google maps with the given address and our API key
            try {
                URL obj = new URL(
                        "https://maps.googleapis.com/maps/api/geocode/json?address=" + this.address
                                + "&key=AIzaSyBlVWChUhH82WTyxz1PDYEwNWdxFiaSBOw");
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("User-Agent", "application/json");
                int responseCode = con.getResponseCode();
                System.out.println("GET Response Code :: " + responseCode);
                if (responseCode == HttpURLConnection.HTTP_OK) { // success
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    // Fetch the latitude and longitude from the retrieved JSON
                    JSONObject json = new JSONObject(response.toString());
                    JSONArray results = json.getJSONArray("results");
                    JSONObject addressComponents = results.getJSONObject(0);
                    JSONObject geometry = addressComponents.getJSONObject("geometry");
                    JSONObject location = geometry.getJSONObject("location");

                    lat = location.getDouble("lat");
                    lon = location.getDouble("lng");

                } else {
                    System.out.println("GET request failed");
                }
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
            return new LatLng(lat, lon);
        }
    }

}
