package com.wegot.fuyan.fyp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreateRequestActivity extends AppCompatActivity {
    List<Address> fullAddress;
    double latitude, longtitude;

    final int ONE_MINUTE_IN_MILLIS = 60000;
    String productName, requestRequirement, addressLine, requestDurationS, postalCodeS,priceS,
    startTime, endTime, err, username, password, authString;
    int postalCode, requestDuration, requestorId;
    double price;
    EditText etProductName, etRequestRequirement, etPostalCode, etAddressLine, etRequestDuration, etPrice;
    Button getAddressBtn, createRequestBtn;
    Geocoder geocoder;
    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);



        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        requestorId = pref.getInt("id",0);
        username = pref.getString("username", null);
        password = pref.getString("password", null);

        etProductName = (EditText)findViewById(R.id.product_name_txt);
        etRequestRequirement = (EditText)findViewById(R.id.product_requirement_txt);
        etPostalCode = (EditText)findViewById(R.id.postal_code_txt);
        etAddressLine = (EditText)findViewById(R.id.address_line_txt);
        etRequestDuration = (EditText)findViewById(R.id.request_duration_txt);
        etPrice = (EditText)findViewById(R.id.request_price_txt);
        getAddressBtn = (Button)findViewById(R.id.get_address_btn);
        createRequestBtn = (Button)findViewById(R.id.create_request_btn);
        geocoder = new Geocoder(this);
/*
        productName = etProductName.getText().toString();
        requestRequirement = etRequestRequirement.getText().toString();
        addressLine = etAddressLine.getText().toString();
        requestDurationS = etRequestDuration.getText().toString();
        requestDuration = Integer.parseInt(requestDurationS);


*/
        getAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postalCodeS = etPostalCode.getText().toString();

                if(postalCodeS != null && postalCodeS.trim().length() >0) {
                    postalCode = Integer.parseInt(postalCodeS);

                    String zip = postalCodeS;
                    try {
                        List<Address> addresses = geocoder.getFromLocationName(zip, 1);
                        if (addresses != null && !addresses.isEmpty()) {
                            Address address = addresses.get(0);
                            // Use the address as needed
                            latitude = address.getLatitude();
                            longtitude = address.getLongitude();

                            String message = String.format("Latitude: %f, Longitude: %f",
                                    latitude, longtitude);

                            Log.d("Result: ", message);
                            fullAddress = geocoder.getFromLocation(latitude, longtitude, 1);

                            if (fullAddress != null && !fullAddress.isEmpty()) {

                                String completeAddress = fullAddress.get(0).getAddressLine(0);
                                String city = fullAddress.get(0).getLocality();
                                String state = fullAddress.get(0).getAdminArea();
                                String country = fullAddress.get(0).getCountryName();
                                String postalCode = fullAddress.get(0).getPostalCode();
                                String knownName = fullAddress.get(0).getFeatureName();

                                //Log.d("Address line: ", completeAddress);
                                //Log.d("City: ", city);
                                //Log.d("State: ", state);
                                //Log.d("Country: ", country);
                                //Log.d("Postal Code: ", postalCode);

                                String addressLineResult = completeAddress + ", " + country;
                                etAddressLine.setText(addressLineResult);
                            } else {

                                etPostalCode.setError("Invalid Postal!");
                                //Toast.makeText(CreateRequestActivity.this, "Unable to process Lat & Long", Toast.LENGTH_LONG).show();
                            }


                        } else {
                            // Display appropriate message when Geocoder services are not available
                            etPostalCode.setError("Invalid Postal!");
                            //Toast.makeText(CreateRequestActivity.this, "Unable to geocode zipcode", Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }
        });

        createRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productName = etProductName.getText().toString();
                requestRequirement = etRequestRequirement.getText().toString();
                postalCodeS = etPostalCode.getText().toString();
                addressLine = etAddressLine.getText().toString();
                requestDurationS = etRequestDuration.getText().toString();
                priceS = etPrice.getText().toString();
                //requestDuration = Integer.parseInt(requestDurationS);

                if(productName != null && productName.trim().length()>0){

                    if(requestRequirement != null && requestRequirement.trim().length() >0){

                        if(postalCodeS != null && postalCodeS.trim().length() >0){

                            postalCode = Integer.parseInt(postalCodeS);

                            if(addressLine != null && addressLine.trim().length()>0){

                                if(requestDurationS != null && requestDurationS.trim().length() > 0){
                                    requestDuration = Integer.parseInt(requestDurationS);

                                    if(priceS!=null && priceS.trim().length()>0){
                                        price = Double.parseDouble(priceS);
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        Date now = new Date();
                                        startTime = sdf.format(now);

                                        Log.d("Start Time: ", startTime);

                                        Calendar date = Calendar.getInstance();
                                        long t= date.getTimeInMillis();
                                        Date afterAddingTenMins=new Date(t + (requestDuration * ONE_MINUTE_IN_MILLIS));
                                        endTime = sdf.format(afterAddingTenMins);

                                        Log.d("End Time: ", endTime);

                                        authString  = username + ":" + password;
                                        new createRequest().execute(authString);


                                    }else{
                                        etPrice.setError("Price required!");
                                    }
                                }else{
                                    etRequestDuration.setError("Duration required!");
                                }

                            }else{
                                etAddressLine.setError("Address required!");
                            }

                        }else{
                            etPostalCode.setError("Postal code required!");
                        }

                    }else{
                        etRequestRequirement.setError("Specify requirements!");
                    }

                }else{
                    etProductName.setError ("Product name required!");
                }


            }
        });



    }

    private class createRequest extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(String... params) {


            //authString = "admin:password";
            final String basicAuth = "Basic " + Base64.encodeToString(params[0].getBytes(), Base64.NO_WRAP);

            boolean success = false;
            String url = "http://weget-2015is203g2t2.rhcloud.com/webservice/request/";
            JSONObject jsoin = null;

            try {

                    jsoin = new JSONObject();
                    jsoin.put("requestorId", requestorId);
                    jsoin.put("productName", productName);
                    jsoin.put("requirement", requestRequirement);
                    jsoin.put("location", addressLine);
                    jsoin.put("postal", postalCode);
                    jsoin.put("startTime", startTime);
                    jsoin.put("duration", requestDuration);
                    jsoin.put("endTime", endTime);
                    jsoin.put("price", price);
                    jsoin.put("status", "active");




            } catch(JSONException e) {
                e.printStackTrace();
                err = e.getMessage();
            }
            Log.d("JSON String: ", jsoin.toString());
            String rst = UtilHttp.doHttpPostBasicAuthentication(mContext, url, jsoin.toString()+ basicAuth);
            if (rst == null) {
                err = UtilHttp.err;
            } else {
                success = true;

            }
            return success;


        }
        @Override
        protected void onPostExecute(Boolean result) {

            if(result) {
                Toast.makeText(getBaseContext(), "Request created!", Toast.LENGTH_LONG).show();
                Intent i = new Intent(CreateRequestActivity.this, HomeActivity.class);
                startActivity(i);
            }else{
                Toast.makeText(getBaseContext(), err, Toast.LENGTH_LONG).show();
            }

        }
    }
}
