package com.palmbell.iworship;

import android.*;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

public class LoginActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;

    private Context mContext;
    private Activity mActivity;

    private String nameToPass;
    private String passwordToPass;

    private Handler mHandler;
    private Runnable mRunnable;
    private boolean currently_processing_request = false;
    private int request_sent_time;
    private int request_termination_time;


    SharedPreferences settingsSharedPref;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);

        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int millisecond = calendar.get(Calendar.MILLISECOND);
        int am_pm = calendar.get(Calendar.AM_PM);



        Date dateObject = new Date();
        long dateInMillis = dateObject.getTime();

//        Toast.makeText(getApplicationContext(), String.valueOf(dateInMillis), Toast.LENGTH_SHORT).show();


        // Get the application context
        mContext = getApplicationContext();
        mActivity = LoginActivity.this;

        mHandler = new Handler();

        settingsSharedPref = getSharedPreferences("PREFS", 0);
        if (settingsSharedPref.getBoolean("user_logged_in", false)) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        }


        final EditText usernameET = (EditText) findViewById(R.id.signInUsername);
        final EditText passwordET = (EditText) findViewById(R.id.signInPassword);
        Button signinBTN = (Button) findViewById(R.id.signInButton);

        signinBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // doing the sign in when the two edittexts are passed to it
                main_login(usernameET, passwordET);
            }
        });


        // Initializing the runnable
        initializeRunnable();

        TextView skip_signup = (TextView) findViewById(R.id.skipsignup_textview);
        skip_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    public void main_login(EditText usernameField, EditText passwordField) {
        nameToPass = usernameField.getText().toString();
        passwordToPass = passwordField.getText().toString();
        BackGround b = new BackGround();
        b.execute(nameToPass, passwordToPass);
    }

    // This background class handles the sign in
    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String name = params[0];
            String password = params[1];
            String data = "";
            int tmp;

            try {
                URL url = new URL(NelsonComponents.baseUrl + "sign_in.php");
                String urlParams = "usernameSent=" + name + "&passwordSent=" + password;

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();
                InputStream is = httpURLConnection.getInputStream();
                while ((tmp = is.read()) != -1) {
                    data += (char) tmp;
                }
                is.close();
                httpURLConnection.disconnect();

                return data;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            String err = null;
            // We just trim the value to avoid spaces
            s = s.trim();

//            Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
//            Log.d("MSGGG",s);

            TextView errorTV;

            try {
                JSONArray jsonArray = new JSONArray(s);

                String[] username = new String[jsonArray.length()];
                String[] email = new String[jsonArray.length()];
                String[] userid = new String[jsonArray.length()];

                settingsSharedPref = getSharedPreferences("PREFS", 0);
                SharedPreferences.Editor editor = settingsSharedPref.edit();
                editor.putBoolean("user_logged_in", true);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    username[i] = obj.getString("Username");
                    email[i] = obj.getString("Email");
                    userid[i] = obj.getString("User_Id");


                    editor.putString("username", username[i]);
                    editor.putString("email", email[i]);
                    editor.putString("userid", userid[i]);

                }

                editor.commit();

                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
                finish();


            } catch (JSONException e) {
                e.printStackTrace();
                errorTV = (TextView) findViewById(R.id.errorMsgTV);
                errorTV.setText("Username or password incorrect!");
                errorTV.setVisibility(View.VISIBLE);
                err = "Exception: " + e.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
                errorTV = (TextView) findViewById(R.id.errorMsgTV);
                errorTV.setText("Username or password incorrect!");
            }

//            Intent i = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(i);

        }
    }


    protected void checkPermission() {
        if (ContextCompat.checkSelfPermission(mActivity, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                + ContextCompat.checkSelfPermission(
                mActivity, android.Manifest.permission.INTERNET) + ContextCompat.checkSelfPermission(
                mActivity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Do something, when permissions not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    mActivity, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    mActivity, android.Manifest.permission.INTERNET)
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    mActivity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // If we should give explanation of requested permissions

                // Show an alert dialog here with request explanation
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mActivity);
                builder.setMessage("Please the permissions requested are needed for proper functionality of this app");
                builder.setTitle("Please grant this permission");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(
                                mActivity,
                                new String[]{
                                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                        android.Manifest.permission.INTERNET,
                                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                                },
                                MY_PERMISSIONS_REQUEST_CODE
                        );
                    }
                });
                builder.setNeutralButton("Cancel", null);
                android.app.AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                // Directly request for required permissions, without explanation
                ActivityCompat.requestPermissions(
                        mActivity,
                        new String[]{
                                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                android.Manifest.permission.INTERNET,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },
                        MY_PERMISSIONS_REQUEST_CODE
                );
            }
        } else {
            // Do something, when permissions are already granted

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CODE: {
                // When request is cancelled, the results array are empty
                if (
                        (grantResults.length > 0) &&
                                (grantResults[0]
                                        + grantResults[1]
                                        + grantResults[2]
                                        == PackageManager.PERMISSION_GRANTED
                                )
                        ) {
                    // Permissions are granted
//                    Toast.makeText(mContext,"Permissions granted.",Toast.LENGTH_SHORT).show();
                } else {
                    // Permissions are denied
//                    Toast.makeText(mContext,"Permissions denied.",Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


    protected void initializeRunnable() {

        mRunnable = new Runnable() {
            @Override
            public void run() {



//                final Calendar calendar = Calendar.getInstance();
//                int year = calendar.get(Calendar.YEAR);
//                int month = calendar.get(Calendar.MONTH);
//                int day = calendar.get(Calendar.DAY_OF_MONTH);
//                int hour = calendar.get(Calendar.HOUR);
//                int minute = calendar.get(Calendar.MINUTE);
//                int second = calendar.get(Calendar.SECOND);
//                int millisecond = calendar.get(Calendar.MILLISECOND);
//                int am_pm = calendar.get(Calendar.AM_PM);
//
//                int current_time = hour+minute+second;
//                int ten_seconds = current_time+10;
//                Log.d("CURRENT",current_time+"");
//                Log.d("TEN_SEC",ten_seconds+"");




                mHandler.postDelayed(mRunnable, 1000);
            }
        };
        mHandler.postDelayed(mRunnable, 1000);
    }

}
