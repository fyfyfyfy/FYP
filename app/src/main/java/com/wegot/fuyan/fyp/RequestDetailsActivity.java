package com.wegot.fuyan.fyp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class RequestDetailsActivity extends AppCompatActivity {


    int requestorId, postal, duration;
    String productName, requirement, location, startTime, endTime, status, err, requestorIdS, requestorName, authString,
    username, password;
    double price;
    Context mContext;

    TextView requestorTV, productNameTV, requirementTV, locationTV, postalTV, startTimeTV, endTimeTV, durationTV,
    priceTV, statusTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);

        Request request = (Request) getIntent().getSerializableExtra("selected_request");

        requestorId = request.getRequestorId();
        requestorIdS = String.valueOf(requestorId);
        productName = request.getProductName();
        requirement = request.getRequirement();
        location = request.getLocation();
        postal = request.getPostal();
        startTime = request.getStartTime();
        endTime = request.getEndTime();
        duration = request.getDuration();
        price = request.getPrice();
        status = request.getStatus();

        requestorTV = (TextView)findViewById(R.id.requestor_tv);
        productNameTV = (TextView)findViewById(R.id.product_tv);
        requirementTV = (TextView)findViewById(R.id.requirement_tv);
        locationTV = (TextView)findViewById(R.id.location_tv);
        postalTV = (TextView)findViewById(R.id.postal_tv);
        startTimeTV = (TextView)findViewById(R.id.start_time_tv);
        endTimeTV = (TextView)findViewById(R.id.end_time_tv);
        durationTV = (TextView)findViewById(R.id.duration_tv);
        priceTV = (TextView)findViewById(R.id.price_tv);
        statusTV = (TextView)findViewById(R.id.status_tv);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        username = pref.getString("username", null);
        password = pref.getString("password", null);
        authString  = username + ":" + password;

        new getRequestor().execute(authString + "," + requestorIdS);

        //requestorTV.setText("Requestor: " + requestorId);



    }

    private class getRequestor extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(String... params) {

            String auth = params[0].substring(0, params[0].indexOf(','));
            String rId = params[0].substring(params[0].indexOf(',') + 1);

            Log.d("auth: ", auth);
            Log.d("rID: ", rId);
            final String basicAuth = "Basic " + Base64.encodeToString(auth.getBytes(), Base64.NO_WRAP);

            boolean success = false;
            String url = "http://weget-2015is203g2t2.rhcloud.com/webservice/account/"+ rId + "/";

            String rst = UtilHttp.doHttpGetBasicAuthentication(mContext, url, basicAuth);
            if (rst == null) {
                err = UtilHttp.err;
                success = false;
            } else {

                try {
                    JSONObject jso = new JSONObject(rst);
                    requestorName = jso.getString("username");



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                success = true;
            }
            return success;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            if(result){

                requestorTV.setText("Requestor: " + requestorName );
                productNameTV.setText("Product: "+ productName);
                requirementTV.setText("Requirement: "+ requirement);
                locationTV.setText("Location: " + location);
                postalTV.setText("Postal: " +postal);
                startTimeTV.setText("Start Time: " + startTime);
                endTimeTV.setText("End Time: " + endTime);
                durationTV.setText("Duration: " + duration);
                priceTV.setText("Price: " + price);
                statusTV.setText("Status: "+ status);

            }else {
                Toast.makeText(getApplicationContext(), "Cannot get requestor name!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.bottombar, menu);
        return true;
    }


    /**
     * Event Handling for Individual menu item selected
     * Identify single menu item by it's id
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.home_item:
                // Single menu item is selected do something
                // Ex: launching new activity/screen or show alert message
                Intent homeIntent = new Intent (this, HomeActivity.class);
                startActivity(homeIntent);
                Toast.makeText(this, "Redirecting to Home Page", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.search_item:
                Toast.makeText(this, "Search is selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.profile_item:
                //Toast.makeText(HomeActivity.this, "Search is Selected", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, ProfileActivity.class);
                startActivity(i);
                Toast.makeText(this, "Redirecting to Profile Page.", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.logout_item:

                Intent logoutIntent = new Intent (this, LoginActivity.class);
                logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(logoutIntent);
                finish();


            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
