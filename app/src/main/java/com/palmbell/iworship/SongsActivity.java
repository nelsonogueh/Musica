package com.palmbell.iworship;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SongsActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> titleArrayList, locationArrayList, artistArrayList, durationArrayList;
    private ArrayList<String> overrallSongDetailsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songs_activity);

        // list all the songs in the device
        doStuff();
    }


    // Putting all the songs in the listview
    public void doStuff() {
        listView = (ListView) findViewById(R.id.songsactivity_songslistview);
        // get the musics from the getMusic() method
        NelsonComponents nelsonComponents = new NelsonComponents();

        titleArrayList = new ArrayList<String>();
        durationArrayList = new ArrayList<String>();
        locationArrayList = new ArrayList<String>();
        artistArrayList = new ArrayList<String>();
        overrallSongDetailsArrayList = new ArrayList<String>();

        // Holding the arraylist returned with a new arraylist
        overrallSongDetailsArrayList = nelsonComponents.getSongInformation(SongsActivity.this);
        titleArrayList = nelsonComponents.getSongsTitle(SongsActivity.this);
        durationArrayList = nelsonComponents.getSongsArtist(SongsActivity.this);
        locationArrayList = nelsonComponents.getSongsLocationLocal(SongsActivity.this);
        artistArrayList = nelsonComponents.getSongsArtist(SongsActivity.this);

        CustomAdapter customAdapter = new CustomAdapter(SongsActivity.this,titleArrayList,artistArrayList);

//        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, customAdapter);
        listView.setAdapter(customAdapter);

        final ArrayList<String> finalArrayListTitle = titleArrayList;
        final ArrayList<String> finalArrayListDuration = durationArrayList;
        final ArrayList<String> finalArrayListLocation = locationArrayList;
        final ArrayList<String> finalArrayListArtist = artistArrayList;
        final ArrayList<String> finalOverallDetails = overrallSongDetailsArrayList;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(SongsActivity.this, NowPlaying.class);
                intent.putExtra("song_location",finalArrayListLocation.get(i));
                intent.putExtra("song_title",finalArrayListTitle.get(i));
                intent.putExtra("song_artist",finalArrayListArtist.get(i));
                intent.putExtra("song_duration",finalArrayListDuration.get(i));
                intent.putExtra("song_host", "offline");
                startActivity(intent);
            }
        });


    }
}

