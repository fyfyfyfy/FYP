package com.wegot.fuyan.fyp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.regex.Pattern;


public class RegisterActivity extends AppCompatActivity {

    Button b1, b2,dpBtn;
    EditText t1, t2, t3, contact, email1;
    Context ctx = this;
    String user_name, user_pass, confirm_pass, email, contactNumS, err;
    Integer contactNum;
    private Context mContext;
    ImageView dpIV;
    byte[] byteArray;
    String encodedImage;
    static final int RC_PERMISSION_READ_EXTERNAL_STORAGE = 666;

    final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        b1 = (Button) findViewById(R.id.button4);
        b2 = (Button) findViewById(R.id.button5);
        dpBtn = (Button)findViewById(R.id.register_image_btn);
        t1 = (EditText) findViewById(R.id.username);
        t2 = (EditText) findViewById(R.id.password);
        t3 = (EditText) findViewById(R.id.Cpassword);
        contact = (EditText) findViewById(R.id.contact_num);
        email1 = (EditText) findViewById(R.id.email);
        dpIV = (ImageView)findViewById(R.id.profile_pic_iv);
        dpIV.setImageResource(R.drawable.ic_profile);

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user_name = t1.getText().toString();
                user_pass = t2.getText().toString();
                confirm_pass = t3.getText().toString();
                contactNumS = contact.getText().toString();

                email = email1.getText().toString();
                DatabaseOperations DB = new DatabaseOperations(ctx);

                if(user_name != null && user_name.trim().length()> 0){ //check if username is filled in

                    if(user_pass != null && user_pass.trim().length() > 0){ //check if password is filled in


                        if(user_pass.trim().length() >= 8 && user_pass.indexOf(" ") == -1){ // check if password is at least 8 characters long


                            if(confirm_pass != null && confirm_pass.trim().length() > 0){// check empty confirmed password


                                if(confirm_pass.equals(user_pass)){ // check if confirmed password matches password


                                    if(contactNumS != null && contactNumS.trim().length() > 0) { // Check if contact number is empty


                                        contactNumS = contactNumS.trim().replaceAll("\\s", "");//Remove all white spaces in contact number

                                        if(contactNumS.matches("[0-9]+")){ // check if contact number only contains integer

                                            contactNum = Integer.parseInt(contactNumS);//convert input into integers

                                            if(email != null && email.trim().length() >0){//check empty email
                                                email = email.toLowerCase();

                                                if(EMAIL_ADDRESS_PATTERN.matcher(email).matches()){ // email pattern validation

                                                    new storeValue().execute(user_name); //execute webservice register!

                                                    /*
                                                    Cursor CR = DB.getInformation(DB);
                                                    CR.moveToFirst();

                                                    if (CR.getCount() > 0) {
                                                        boolean registerStatus = true;
                                                        do {
                                                            if (user_name.equals(CR.getString(0))) {
                                                                registerStatus = false;
                                                            }

                                                        } while (CR.moveToNext());
                                                        if (!registerStatus) {
                                                            Toast.makeText(getBaseContext(), "Existing User Name!", Toast.LENGTH_LONG).show();
                                                        } else {
                                                            DB.putInformation(DB, user_name, user_pass, contactNum, email);
                                                            Toast.makeText(getBaseContext(), "Registration Success!", Toast.LENGTH_LONG).show();
                                                            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                                            startActivity(i);
                                                        }
                                                    }else{
                                                        DB.putInformation(DB, user_name, user_pass, contactNum, email);
                                                        Toast.makeText(getBaseContext(), "Registration Success!", Toast.LENGTH_LONG).show();
                                                        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                                        startActivity(i);
                                                    }
                                                    */

                                                }else{
                                                    email1.setError("Invalid email format!");
                                                }

                                            }else{
                                                email1.setError("Email is required!");
                                            }

                                        }else{
                                            contact.setError("Invalid contact number!");
                                        }
                                    }else{
                                        contact.setError("Contact number is required!");
                                    }
                                }else{
                                    t3.setError("Password mismatch!");
                                }
                            }else{
                                t3.setError("Confirm password is required!");
                            }

                        }else{
                            t2.setError("Invalid password!");
                        }

                    }else{
                        t2.setError("Password is required!");
                    }

                }else{
                    t1.setError("User name is required!");
                }



            }
        });

        dpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(RegisterActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {


                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(RegisterActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, RC_PERMISSION_READ_EXTERNAL_STORAGE);



                }else {

                    ChooseImage();
                }

            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);

            }
        });



        //upload image testing
        //initView();
    }

    public void ChooseImage() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)
                && !Environment.getExternalStorageState().equals(
                Environment.MEDIA_CHECKING)) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 1);

        } else {
            Toast.makeText(RegisterActivity.this,
                    "No activity found to perform this task",
                    Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if (resultCode == RESULT_OK) {
            Bitmap originBitmap = null;
            Bitmap resized = null;
            Uri selectedImage = data.getData();
            Toast.makeText(RegisterActivity.this, selectedImage.toString(),
                    Toast.LENGTH_SHORT).show();
            //txtmsg.setText(selectedImage.toString());
            InputStream imageStream;
            try {

                imageStream = getContentResolver().openInputStream(
                        selectedImage);
                originBitmap = BitmapFactory.decodeStream(imageStream);
                resized = Bitmap.createScaledBitmap(originBitmap, 100, 100, true);

            } catch (FileNotFoundException e) {

                //txtmsg.setText(e.getMessage().toString());
            }
            if (originBitmap != null) {
                this.dpIV.setImageBitmap(originBitmap);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                if (resized.compress(Bitmap.CompressFormat.JPEG, 100, stream)){
                    byteArray = stream.toByteArray();

                    encodedImage = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                    Toast.makeText(RegisterActivity.this, "Conversion Done",
                            Toast.LENGTH_SHORT).show();
                    Log.d("BASE46: ", encodedImage);
                }else if (resized.compress(Bitmap.CompressFormat.PNG, 100, stream)){
                    byteArray = stream.toByteArray();

                    encodedImage = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                    Toast.makeText(RegisterActivity.this, "Conversion Done",
                            Toast.LENGTH_SHORT).show();
                    Log.d("BASE46: ", encodedImage);
                }


            }
        }
    }


    private class storeValue extends AsyncTask<String, Void, Boolean>{

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(String... params) {

            boolean success = false;
            String url = "http://weget-2015is203g2t2.rhcloud.com/webservice/account/";
            JSONObject jsoin = null;

            try {
                if(encodedImage!= null && encodedImage.length() >0) {
                    jsoin = new JSONObject();
                    jsoin.put("username", user_name);
                    jsoin.put("password", user_pass);
                    jsoin.put("contactNo", contactNum);
                    jsoin.put("email", email);
                    jsoin.put("fulfiller", "yes");
                    jsoin.put("picture", encodedImage);
                }else{

                    jsoin = new JSONObject();
                    jsoin.put("username", user_name);
                    jsoin.put("password", user_pass);
                    jsoin.put("contactNo", contactNum);
                    jsoin.put("email", email);
                    jsoin.put("picture", "");

                }


            } catch(JSONException e) {
                e.printStackTrace();
                err = e.getMessage();
            }

            String rst = UtilHttp.doHttpPostJson(mContext, url,jsoin.toString());
            if (rst == null) {
                err = UtilHttp.err;
            } else {
                success = true;

            }
            return success;

            /*
            boolean success = false;
            String url;
            if(encodedImage != null && encodedImage.length() > 0) {
                 url = "http://weget-2015is203g2t2.rhcloud.com//webservice/account?username=" + params[0] + "&password=" + Uri.encode(user_pass) + "&contactNo=" + contactNumS
                        + "&email=" + Uri.encode(email) + "&img=" + Uri.encode(encodedImage);
            }else{
                 url = "http://weget-2015is203g2t2.rhcloud.com//webservice/account?username=" + params[0] + "&password=" + Uri.encode(user_pass) + "&contactNo=" + contactNumS
                        + "&email=" + Uri.encode(email) + "&img=" + Uri.encode("default image");
            }

            String rst = UtilHttp.doHttpPost(mContext, url);
            if (rst == null) {
                err = UtilHttp.err;
            } else {
                success = true;

            }
            return success;
            */
        }
        @Override
        protected void onPostExecute(Boolean result) {

            if(result) {
                Toast.makeText(getBaseContext(), "Registration Success!", Toast.LENGTH_LONG).show();
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }else{
                Toast.makeText(getBaseContext(), err, Toast.LENGTH_LONG).show();
            }

        }
    }




}
