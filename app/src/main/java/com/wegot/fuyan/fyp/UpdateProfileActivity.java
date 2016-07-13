package com.wegot.fuyan.fyp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class UpdateProfileActivity extends AppCompatActivity {

    String username;
    TextView updateTitle;
    Button updatePasswordBtn, updateEmailBtn, updateContactBtn,uploadImageBtn;

    /*
    EditText updateEmail, updateContact, updateOldPassword, updateNewPassword, updateConfirmPassword;
    private Context mContext;
    Button updateButton;
    int contactNo,updatedContactNo;
    String password,email,err, updatedEmail, updatedContact,
            updatedOldPassword, updatedNewPassword, updatedConfirmPassword;

    final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        final SharedPreferences.Editor editor = pref.edit();
        username = pref.getString("username", null);

        updatePasswordBtn = (Button)findViewById(R.id.update_password_btn);
        updateEmailBtn = (Button)findViewById(R.id.update_email_btn);
        updateContactBtn = (Button)findViewById(R.id.update_contact_btn);
        uploadImageBtn = (Button)findViewById(R.id.upload_image_btn);
        updateTitle = (TextView)findViewById(R.id.update_username);
        updateTitle.setText("User Name: " + username);

        updatePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (UpdateProfileActivity.this, UpdatePasswordActivity.class);
                startActivity(i);
            }
        });

        updateEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (UpdateProfileActivity.this, UpdateEmailActivity.class);
                startActivity(i);
            }
        });

        updateContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (UpdateProfileActivity.this, UpdateContactActivity.class);
                startActivity(i);
            }
        });

        uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (UpdateProfileActivity.this, UploadImageActivity.class);
                startActivity(i);
            }
        });

        //password = pref.getString("password", null);
        //email = pref.getString("email",null);
        //contactNo = pref.getInt("contactnumber",0);
        //String contactNoS = String.valueOf(contactNo);
/*
        updateTitle = (TextView)findViewById(R.id.update_username);
        updateEmail = (EditText)findViewById(R.id.update_email);
        updateContact = (EditText)findViewById(R.id.update_contact);
        updateOldPassword = (EditText)findViewById(R.id.update_old_password);
        updateNewPassword = (EditText)findViewById(R.id.update_new_password);
        updateConfirmPassword = (EditText)findViewById(R.id.update_confirm_password);
        updateButton = (Button)findViewById(R.id.update_btn);

        updateTitle.setText("User Name: " + username);
        updateEmail.setHint(email);
        updateContact.setHint(contactNoS);



        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updatedEmail = updateEmail.getText().toString();
                updatedContact = updateContact.getText().toString();
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
                                            if(updatedEmail != null && updatedEmail.trim().length() >0){

                                                if(EMAIL_ADDRESS_PATTERN.matcher(updatedEmail).matches()){
                                                    updatedEmail = updatedEmail.toLowerCase();

                                                    if(!updatedEmail.equals(email)){

                                                        if(updatedContact != null && updatedContact.trim().length() > 0){
                                                            updatedContact = updatedContact.trim().replaceAll("\\s", "");
                                                            if(updatedContact.matches("[0-9]+")){
                                                                updatedContactNo = Integer.parseInt(updatedContact);
                                                                new updateValue().execute(username);
                                                                Toast.makeText(getBaseContext(), "Update Success!", Toast.LENGTH_LONG).show();
                                                                editor.putString("email",updatedEmail);
                                                                editor.putInt("contactnumber", updatedContactNo);
                                                                editor.commit();
                                                                Intent i = new Intent(UpdateProfileActivity.this, ProfileActivity.class);
                                                                startActivity(i);
                                                            }else{
                                                                updateContact.setError("Invalid contact number!");
                                                            }

                                                        }else{
                                                            updateContact.setError("Contact number is required!");
                                                        }
                                                    }else{
                                                        updateEmail.setError("Same as old email!");
                                                    }


                                                }else{
                                                    updateEmail.setError("Invalid email format!");
                                                }

                                            }else{
                                                updateEmail.setError("Email is required!");
                                            }

                                        }else{
                                            updateConfirmPassword.setError("Password mismatch!");
                                        }
                                    }else{
                                        updateConfirmPassword.setError("Confirm password is required!");
                                    }
                                }else{
                                    updateNewPassword.setError("Please choose a new password!");
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




*/
    }


/*
    private class updateValue extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean success = false;
            String url = "http://weget-2015is203g2t2.rhcloud.com/webservice/account/" +params[0] +"?&password="+ Uri.encode(updatedNewPassword)+"&contactNo="+ updatedContactNo
                    +"&email="+Uri.encode(updatedEmail)+"&img="+Uri.encode("updated picture");
//http://weget-2015is203g2t2.rhcloud.com/webservice/account/lala?password=12345677&contactNo=12313&email=asd@qwe.com&img=11231asda
            String rst = UtilHttp.doHttpPost(mContext, url);
            if (rst == null) {
                err = UtilHttp.err;
            } else {
                success = true;

            }
            return success;
        }
        @Override
        protected void onPostExecute(Boolean result) {

        }
    }

*/
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
