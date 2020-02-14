package com.palmbell.iworship;

import android.content.Context;
//import android.support.annotation.NonNull;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Nelson on 4/7/2018.
 */

public class CustomAdapter extends ArrayAdapter {

    ArrayList<String> titleArray;
    ArrayList<String> artistArray;

    public CustomAdapter(Context context, ArrayList<String> songTitle, ArrayList<String> songArtist) {
        //Overriding Default Constructor off ArratAdapter
        super(context, R.layout.song_listview_single_row, songTitle);

        this.titleArray = songTitle;
        this.artistArray = songArtist;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Inflating the layout
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.song_listview_single_row, parent, false);

        //Get the reference to the view objects
//        ImageView myImage = (ImageView) row.findViewById(R.id.idPic);
        TextView myTitle = (TextView) row.findViewById(R.id.songname_listview_row);
        TextView myDescription = (TextView) row.findViewById(R.id.songartist_listview_row);

        //Providing the element of an array by specifying its position
//        myImage.setImageResource(imageArray[position]);
        myTitle.setText(titleArray.get(position));
        myDescription.setText(artistArray.get(position));
        return row;
    }


}
