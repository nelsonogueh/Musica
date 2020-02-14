package com.palmbell.iworship;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    NelsonComponents nelsonComponents;
    private SharedPreferences settingsSharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setIcon(R.mipmap.iworship_icon);

        LinearLayout userHeader = (LinearLayout) findViewById(R.id.header);

        settingsSharedPref = getSharedPreferences("PREFS", 0);

        if(settingsSharedPref.getBoolean("user_logged_in",false)){
            userHeader.setVisibility(View.VISIBLE);
            String newUsername = settingsSharedPref.getString("username","");
            TextView usernameDisplayTV = (TextView)findViewById(R.id.usernameDisplayTV);
            usernameDisplayTV.setText(newUsername.toUpperCase());

        }
        else {
            userHeader.setVisibility(View.GONE);
        }

        // Linking to SongsActivity
        LinearLayout songs_activity = (LinearLayout) findViewById(R.id.songs_button);
        songs_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SongsActivity.class);
                startActivity(intent);
            }
        });

        // This will just do the counting of the songs on the device and display it.
        TextView song_textview = (TextView)findViewById(R.id.mainactivity_song_button_textview);
        nelsonComponents = new NelsonComponents();
        song_textview.append("("+nelsonComponents.getMusicCount(MainActivity.this) + ""+")");




        // Linking to i - zone
        LinearLayout i_zone_btn = (LinearLayout) findViewById(R.id.i_zone_button);
        i_zone_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // if the user is already signed in
                if(settingsSharedPref.getBoolean("user_logged_in",false)){

                    Intent intent = new Intent(MainActivity.this, I_Zone.class);
                    startActivity(intent);

                }
                else {
                    Snackbar.make(view,"You must sign in first before you can access this zone",Snackbar.LENGTH_SHORT).show();

                    // to be removed later
                    Intent intent = new Intent(MainActivity.this, I_Zone.class);
                    startActivity(intent);
                }


//                Intent intent = new Intent(MainActivity.this, SongsActivity.class);
//                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
// Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
