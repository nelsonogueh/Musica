package com.palmbell.iworship;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class I_Zone extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> titleArrayList, durationArrayList, locationArrayList, artistArrayList, overrallSongDetailsArrayList;

    NelsonComponents nelsonComponents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.i_zone);

        nelsonComponents = new NelsonComponents();

        LinearLayout onlinePlaylist_btn = (LinearLayout) findViewById(R.id.onlinePlaylists_button);
        onlinePlaylist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(I_Zone.this, PlaylistOnline.class);
                startActivity(intent);
            }
        });

        // just calling the method
        doStuff();
    }


    // Putting all the songs in the listview
    public void doStuff() {
        listView = (ListView) findViewById(R.id.onlinesongs_songslistview);
        // get the musics from the getMusic() method
        NelsonComponents nelsonComponents = new NelsonComponents();

        titleArrayList = new ArrayList<String>();
        durationArrayList = new ArrayList<String>();
        locationArrayList = new ArrayList<String>();
        artistArrayList = new ArrayList<String>();
        overrallSongDetailsArrayList = new ArrayList<String>();

        // The data has to come before the custom adapter

        // Getting the details from the internet
        query_online_songs();

        CustomAdapter customAdapter = new CustomAdapter(I_Zone.this, titleArrayList, artistArrayList);

//        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, customAdapter);
        listView.setAdapter(customAdapter);

        final ArrayList<String> finalArrayListTitle = titleArrayList;
        final ArrayList<String> finalArrayListLocation = locationArrayList;
        final ArrayList<String> finalArrayListArtist = artistArrayList;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(I_Zone.this, NowPlaying.class);
                intent.putExtra("song_location", finalArrayListLocation.get(i));
                intent.putExtra("song_title", finalArrayListTitle.get(i));
                intent.putExtra("song_artist", finalArrayListArtist.get(i));
                intent.putExtra("song_host", "online");
                startActivity(intent);
            }
        });


    }


    public void query_online_songs() {
        I_Zone.BackGround b = new I_Zone.BackGround();
        b.execute();
    }

    // This background class handles the sign in
    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            String data = "";
            int tmp;

            try {
                URL url = new URL(NelsonComponents.baseUrl + "music_list.php");
                String urlParams = "usernameSent=myvalue";

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

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    locationArrayList.add(obj.getString("Song_Path"));
                    artistArrayList.add(obj.getString("Song_Genre"));
                    titleArrayList.add(obj.getString("Song_Title"));

                }

            } catch (JSONException e) {
                e.printStackTrace();
                err = "Exception: " + e.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
            }

//            Intent i = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(i);

        }
    }
}
