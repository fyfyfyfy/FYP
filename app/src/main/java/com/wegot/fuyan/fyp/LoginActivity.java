package com.wegot.fuyan.fyp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {
    Button b1, b2;
    EditText ed1, ed2;
    Context ctx = this;
    String username, password, dbUsername, dbPassword, dbEmail, dbProfilePic, err;
    int dbID, dbContactNumber;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ImageView logo_iv = (ImageView)findViewById(R.id.applogo_imageview);
        logo_iv.setImageResource(R.drawable.weget_logo);


        b1 = (Button)findViewById(R.id.login_button);
        ed1 = (EditText)findViewById(R.id.login_text);
        ed2 = (EditText)findViewById(R.id.password_text);
        b2 = (Button)findViewById(R.id.register_button);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = ed1.getText().toString().trim();
                password = ed2.getText().toString().trim();

                if (username.length() > 0 && password.length() > 0) {

                    new getValues().execute(username);

                    /*
                    DatabaseOperations DOP = new DatabaseOperations(ctx);
                    Cursor CR = DOP.getInformation(DOP);
                    CR.moveToFirst();
                    boolean loginstatus = false;
                    String NAME = "";
                    if(CR.getCount() > 0) {
                        do {
                            if (username.equals(CR.getString(0)) && (password.equals(CR.getString(1)))) {
                                loginstatus = true;
                                NAME = CR.getString(0);
                            }

                        } while (CR.moveToNext());
                        if (loginstatus) {
                            Toast.makeText(getApplicationContext(), "Login Success! Redirecting...", Toast.LENGTH_SHORT).show();

                            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("user_name",NAME);
                            editor.commit();

                            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                            i.putExtra("username", NAME);
                            startActivity(i);


                        } else {
                            Toast.makeText(getApplicationContext(), "Login Failed! Wrong Credentials", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Empty User Database!", Toast.LENGTH_SHORT).show();
                    }
                    */

                } else {
                    Toast.makeText(getApplicationContext(), "Please Fill Up The Fields!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();

            }
        });



    }

    private class getValues extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(String... params) {

            boolean success = false;
            String url = "http://weget-2015is203g2t2.rhcloud.com/webservice/login?username="+params[0]+"&password="+ password;

            String rst = UtilHttp.doHttpGet(mContext, url);
            if (rst == null) {
                err = UtilHttp.err;
                success = false;
            } else {

                try {
                    JSONObject jso = new JSONObject(rst);
                    dbID = jso.getInt("id");
                    dbUsername = jso.getString("username");
                    dbPassword = jso.getString("password");
                    dbContactNumber = jso.getInt("contactNo");
                    dbEmail = jso.getString("email");
                    dbProfilePic = jso.getString("picture");


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

                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("id", dbID) ;
                editor.putString("username",dbUsername);
                editor.putString("password", dbPassword);
                editor.putInt ("contactnumber",dbContactNumber);
                editor.putString("email", dbEmail);
                editor.putString("picture", dbProfilePic);
                editor.commit();

                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(i);

            }else {
               Toast.makeText(getApplicationContext(), "Login Failed! Wrong Credentials", Toast.LENGTH_SHORT).show();
           }

        }
    }

    public static String md5(String s)
    {
        MessageDigest digest;
        try
        {
            digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes(Charset.forName("US-ASCII")),0,s.length());
            byte[] magnitude = digest.digest();
            BigInteger bi = new BigInteger(1, magnitude);
            String hash = String.format("%0" + (magnitude.length << 1) + "x", bi);
            return hash;
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return "";
    }
}
