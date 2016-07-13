package com.wegot.fuyan.fyp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdatePasswordActivity extends AppCompatActivity {

    String username;
    String password,email,err, updatedOldPassword, updatedNewPassword,
            updatedConfirmPassword, contactNoS, idString, picture;
    int contactNo, id;
    private Context mContext;
    TextView updateTitle;
    Button updatePasswordBtn;
    EditText updateOldPassword,updateNewPassword, updateConfirmPassword;
    SharedPreferences.Editor editor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        username = pref.getString("username", null);
        password = pref.getString("password", null);
        email = pref.getString("email",null);
        contactNo = pref.getInt("contactnumber",0);
        contactNoS = String.valueOf(contactNo);
        id = pref.getInt("id",0);
        idString = String.valueOf(id);
        picture = pref.getString("picture", null);


        updateOldPassword = (EditText)findViewById(R.id.update_old_password);
        updateNewPassword = (EditText)findViewById(R.id.update_new_password);
        updateConfirmPassword = (EditText)findViewById(R.id.update_confirm_password);
        updatePasswordBtn = (Button)findViewById(R.id.confirm_update_password_btn);
        updateTitle = (TextView)findViewById(R.id.update_username);
        updateTitle.setText("User Name: " + username);
        updateOldPassword.setHint("Old password");
        updateNewPassword.setHint("New password");
        updateConfirmPassword.setHint("Confirm password");

        updatePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatedOldPassword = updateOldPassword.getText().toString();
                updatedNewPassword = updateNewPassword.getText().toString();
                updatedConfirmPassword = updateConfirmPassword.getText().toString();


                if(updatedOldPassword != null && updatedOldPassword.trim().length() >0){
                    if(updatedOldPassword.equals(password)){

                        if(updatedNewPassword != null && updatedNewPassword.trim().length() > 0){

                            if(updatedNewPassword.trim().length() >= 8 && updatedNewPassword.indexOf(" ") == -1){
                                if(!updatedNewPassword.equals(password)){
                                    if(updatedConfirmPassword != null && updatedConfirmPassword.trim().length()>0){
                                        if(updatedConfirmPassword.equals(updatedNewPassword)){
                                            new updateValue().execute(idString);

                                        }else{
                                        updateConfirmPassword.setError("Password mismatch!");
                                    }
                                }else{
                                    updateConfirmPassword.setError("Confirm password is required!");
                                }
                            }else{
                                updateNewPassword.setError("Same as old password!");
                            }

                        }else{
                            updateNewPassword.setError("Invalid password!");
                        }

                    }else{
                        updateNewPassword.setError("New password is required!");
                    }

                }else{
                    updateOldPassword.setError("Invalid password!");
                }

            }else{
                updateOldPassword.setError("Old password is required!");
            }
            }
        });
    }


    //HTTP Connection

    private class updateValue extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String authString  = username + ":" + password;
            //authString = "admin:password";
            final String basicAuth = "Basic " + Base64.encodeToString(authString.getBytes(), Base64.NO_WRAP);
            Log.d ("Basic Authentitaion", basicAuth);

            boolean success = false;
            String url = "http://weget-2015is203g2t2.rhcloud.com/webservice/account/" +params[0] +"/";
            JSONObject jsoin = null;

            try {
                jsoin = new JSONObject();
                jsoin.put("password", updatedNewPassword);
                jsoin.put("contactNo", contactNo);
                jsoin.put("email", email);
                jsoin.put("fulfiller", "yes");
                jsoin.put("picture", picture);


            } catch(JSONException e) {
                e.printStackTrace();
                err = e.getMessage();
            }

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
            if(result){
                Toast.makeText(getBaseContext(), "Password Update Success!", Toast.LENGTH_LONG).show();
                editor.putString("password",updatedNewPassword);
                editor.commit();
                Intent i = new Intent(UpdatePasswordActivity.this, ProfileActivity.class);
                startActivity(i);
            }else{
                Toast.makeText(getBaseContext(), err, Toast.LENGTH_LONG).show();
            }

        }
    }


    //Menu Bar

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.bottombar, menu);
        return true;
    }


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
